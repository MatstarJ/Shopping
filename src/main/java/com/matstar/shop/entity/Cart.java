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
    private Long Id;

    @OneToOne(fetch = FetchType.LAZY)
    //매핑 외래키 지정
    @JoinColumn(name="member_id")
    private Member member;

}