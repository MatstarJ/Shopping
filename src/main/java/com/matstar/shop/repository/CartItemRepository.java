package com.matstar.shop.repository;

import com.matstar.shop.dto.CartDetailDto;
import com.matstar.shop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    // 장바구니 페이지에 전달할 CartDetailDto 리스트를 조회하는 쿼리문 작성
    // CartDetailDto의 생성자를 이용하여 DTO를 반환할 때는 new 키워드와 함께 패키지와 클래스 명을 적어준다.
    // 또한 생성자의 파라미터 순서를 지켜야 한다.
@Query("select new com.matstar.shop.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) " +
        "from CartItem ci, ItemImg im " +
        "join ci.item i " +
        "where ci.cart.id = :cartId " +
        "and im.item.id = ci.item.id " +
        "and im.repimgYn = 'Y' " +
        "order by ci.regTime desc"
)
List<CartDetailDto> findCartDetailDtoList(Long cartId);

}
