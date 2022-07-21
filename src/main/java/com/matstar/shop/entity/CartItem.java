package com.matstar.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Getter
@Setter
@Table(name="cart_item")
public class CartItem  extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name="cart_item_id")
    private Long id;

    //하나의 장바구니에는 여러 개의 장바구니 상품을 담을 수 있다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cart_id")
    private Cart cart;

    //하나의 아이템은 여러 장바구니 상품에 담김 수 있다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    private int count;

    //장바구니에 담을 상품 엔티티를 생성
    public static CartItem createCartItem(Cart cart, Item item, int count) {

        CartItem cartItem = new CartItem();

        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(count);

        return cartItem;
    }

    //기존에 담겨 있는 상품의 개수를 더해줄 때 사용
    public void addCount(int count) {
        this.count += count;
    }

}
