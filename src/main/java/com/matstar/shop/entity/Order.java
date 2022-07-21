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


    // 주문 상품 정보를 담는다. orderitem 객체를 oder 객체의 orderitems에 추가한다.
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList) {
        Order order = new Order();
        order.setMember(member);
        //장바구니페이지에서는 한 번에 여러 개의 상품을 주문할 수 있다.
        //따라서 여러 개의 주문 상품을 담을 수 있도록 리스트 형태로 파라미터 값을
        // 받으며 주문 객체에 orderItem 객체를 추가한다.
        for(OrderItem orderItem : orderItemList) {
            order.addOrderItem(orderItem);
        }

        //주문 상태 변경
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    // 총 금액 계산
    public int getTotalPrice() {
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCEL;

        for(OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

}
