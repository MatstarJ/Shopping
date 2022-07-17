package com.matstar.shop.entity;

import com.matstar.shop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter
@Setter
public class Order extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    //한 명의 회원은 여러 번 주문할 수 있다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate; //주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문상태

    //private LocalDateTime regTime;

   // private LocalDateTime updateTime;

    //하나의 주문에 여러 상품을 담을 수 있다.
    //연관관계의 주인은 외래키가 있는 쪽으로 한다.
    //부모(one) 엔티티의 영속성의 변화를 자식(many)엔티티에 모두 전이한다.
    //주문엔티티를 저장하면 주문 상품 엔티티도 함께 저장된다.
    //orphanRemoval, 부모 엔티티와의 연관 관계가 끊어진 자식 엔티티를 제거 하는 옵션
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();


}
