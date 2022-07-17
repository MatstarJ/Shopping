package com.matstar.shop.repository;

import com.matstar.shop.constant.ItemSellStatus;
import com.matstar.shop.entity.Item;
import com.matstar.shop.entity.QItem;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemListTest() {

        for (int i = 1; i <= 10; i++) {

            Item item = new Item();

            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());

            Item savedItem = itemRepository.save(item);
            System.out.println(savedItem.toString());
        }

    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNmTest() {

        this.createItemListTest();
        List<Item> itemList = itemRepository.findByItemNm("테스트 상품1");

        for (Item item : itemList) {

            System.out.println(item.toString());


        }
    }

    @Test
    @DisplayName("상품명, 상품상세설명 or 테스트")
    public void findByItemMnOrDetailTest() {

        this.createItemListTest();

        List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트 상품1", "상세 설명5");

        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 LessThan 테스트")
    public void findByPriceLessThanTest() {
        this.createItemListTest();
        List<Item> itemList = itemRepository.findByPriceLessThan(10005);

        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("@Query 테스트")
    public void findByItemDetailTest() {
        this.createItemListTest();
        List<Item> itemList = itemRepository.findByItemDetail("상세 설명1");

        for(Item item : itemList) {

            System.out.println(item.toString());
        }

    }

    @Test
    @DisplayName("NativeQ 테스트")
    public void findByItemByNativeQ() {

        this.createItemListTest();
        List<Item> itemList = itemRepository.findByItemDetailByNative("상세 설명1");
        for(Item item : itemList) {
            System.out.println(item.toString());
        }

    }

    @Test
    @DisplayName("Querydsl 조회 테스트")
    public void queryDslTest() {
        this.createItemListTest();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qItem = QItem.item;
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%"+"상세 설명"+"%"))
                .orderBy(qItem.price.desc());

        List<Item> itemList = query.fetch();

        for(Item item : itemList) {
            System.out.println(item.toString());
        }

    }

    public void createItemList2() {

        for(int i=1; i<=5; i++) {

            Item item = new Item();
            item.setItemNm("테스트 상품1"+i);
            item.setPrice(10000+i);
            item.setItemDetail("상세 설명"+i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }

        for(int i=6; i<=10; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상품"+i);
            item.setPrice(10000+i);
            item.setItemDetail("상세 설명"+i);
            item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            item.setStockNumber(0);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);

        }
    }

    @Test
    @DisplayName("상품 Querydsl 조회 테스트 2")
    public void queryDslTest2(){

        this.createItemList2();

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem item = QItem.item;
        String itemDetail = "상세 설명";
        int price = 10003;
        String itemSellStat = "SELL";

        booleanBuilder.and(item.itemDetail.like("%" + itemDetail + "%"));
        booleanBuilder.and(item.price.gt(price));
        System.out.println(ItemSellStatus.SELL);
        if(StringUtils.equals(itemSellStat, ItemSellStatus.SELL)){
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        Pageable pageable = PageRequest.of(0, 5);
        Page<Item> itemPagingResult = itemRepository.findAll(booleanBuilder, pageable);
        System.out.println("total elements : " + itemPagingResult. getTotalElements ());

        List<Item> resultItemList = itemPagingResult.getContent();
        for(Item resultItem: resultItemList){
            System.out.println(resultItem.toString());
        }
    }
}
