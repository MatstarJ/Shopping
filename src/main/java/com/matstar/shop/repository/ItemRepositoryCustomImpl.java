package com.matstar.shop.repository;

import com.matstar.shop.constant.ItemSellStatus;
import com.matstar.shop.dto.ItemSearchDto;
import com.matstar.shop.dto.MainItemDto;
import com.matstar.shop.dto.QMainItemDto;
import com.matstar.shop.entity.Item;
import com.matstar.shop.entity.QItem;
import com.matstar.shop.entity.QItemImg;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

//사용자 정의 인터페이스 구현, 구현 클래스는 반드시 클래스 이름+Impl 이어야 한다.
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

    // 동적 쿼리 생성을 위한 JPAQueryFactory 클래스 사용
    private JPAQueryFactory queryFactory;

    //JPAQuery의 생성자로 EntityManager 클래스를 사용
    public ItemRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    // 상품 판매 조건이 null일 경우 null을 리턴, 결과값이 null이면 where 절에서 해당 조건은 무시된다.
    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus) {
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    //시간에 따른 조회 설정
    //BooleanExpression : where 절에서 사용할 값 지정
    private BooleanExpression regDtsAfter(String searchDateType) {
        LocalDateTime dateTime = LocalDateTime.now();

        if (StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null;
        }else if(StringUtils.equals("id",searchDateType)) {
            dateTime = dateTime.minusDays(1);
        }else if(StringUtils.equals("1w",searchDateType)){
            dateTime = dateTime.minusWeeks(1);
        }else if(StringUtils.equals("1m",searchDateType)){
            dateTime = dateTime.minusMonths(1);
        } else if (StringUtils.equals("6m", searchDateType)) {
            dateTime = dateTime.minusMonths(6);
        }
        return QItem.item.regTime.after(dateTime);
    }


    //searchBy 값에 따라 상품명에 검색어를 포함하고 있는 상품 또는
    // 상품 생성자의 아이디에 검색어를 포함하고 있는 상품을 조회하도록 조건값을 반환
    private BooleanExpression searchByLike(String searchBy, String searchQuery){

        if(StringUtils.equals("itemNm", searchBy)){
            return QItem.item.itemNm.like("%" + searchQuery + "%");
        } else if(StringUtils.equals("createdBy", searchBy)){
            return QItem.item.createdBy.like("%" + searchQuery + "%");
        }

        return null;
    }

    // queryFactory를 이용해서 쿼리 생성
    // selectForm : 상품 데이터를 조회하기 위해서 QItem에 item을 지정
    // where : BooleanExpression을 반환하는 조건문들을 넣는다. ","단위로 넣을 경우 and 조건으로 인식
    //offset : 데이터를 가지고 올 시작 인덱스 지정
    //limit : 한 번에 가지고 올 수 있는 최대 개수 지정
    //fetchResult() : 조회한 리스트 및 전체 개수를 포함하는 QueryResults를 반환.
    // 상품 데이터 리스트 조회 및 상품 데이터 전체 개수를 조회하는 2번의 쿼리문이 실행된다.
    //조회한 데이터를 Page 클래스의 구현체인 PageImpl 객체로 반환
    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {

        // 데이터 리스트
        List<Item> content = queryFactory
                .selectFrom(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(),
                                itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        // 전체 갯수
        long total = queryFactory.select(Wildcard.count).from(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    //검색어가 null이 아니면 상품명에 해당 검색어가 포함되는 상픔을 조회하는 조건 반환
    private BooleanExpression itemNmLike(String searchQuery) {

        return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemNm.like("%"+searchQuery+"%");

    }


    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        List<MainItemDto> content = queryFactory
                .select(
                        new QMainItemDto(
                                item.id,
                                item.itemNm,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price)
                )
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))  // 대표 이미지만 가져옴
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .fetchOne()
                ;

        return new PageImpl<>(content, pageable, total);
    }
}
