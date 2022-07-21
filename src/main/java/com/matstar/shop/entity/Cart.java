package com.matstar.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="cart")
@Getter
@Setter
@ToString
public class Cart extends BaseEntity {

    @Id
    @Column(name="cart_id")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    //매핑 외래키 지정
    @JoinColumn(name="member_id")
    private Member member;

    //회원의 장바구니를 만드는 메소드
    public static Cart createCart(Member member) {
        Cart cart = new Cart();

        cart.setMember(member);

        return cart;
    }

}