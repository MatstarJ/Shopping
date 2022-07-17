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

}
