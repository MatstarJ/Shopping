package com.matstar.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class OrderItem extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name="order_item_id")
    private Long id;

    //하나의 상품은 여러 주문 상품으로 들어갈 수 있다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    //한 번의 주문에 여러 개의 상품을 주문할 수 있다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;

    private int orderPrice; // 주문 가격

    private int count; //주문 수량

    //private LocalDateTime regTime;

    //private LocalDateTime updateTime;

    public static OrderItem createOrderItem(Item item, int count) {

        OrderItem orderItem = new OrderItem();

        orderItem.setItem(item);

        orderItem.setCount(count);

        orderItem.setOrderPrice(item.getPrice());

        //주문 수량만큼 재고 감소
        item.removeStock(count);

        return orderItem;

    }


    public int getTotalPrice() {
        return orderPrice * count;
    }



    public void cancel() {
        this.getItem().addStock(count);
    }

}