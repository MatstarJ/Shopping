package com.matstar.shop.service;

import com.matstar.shop.dto.OrderDto;
import com.matstar.shop.entity.Item;
import com.matstar.shop.entity.Member;
import com.matstar.shop.entity.Order;
import com.matstar.shop.entity.OrderItem;
import com.matstar.shop.repository.ItemRepository;
import com.matstar.shop.repository.MemberRepository;
import com.matstar.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;

    private final MemberRepository memberRepository;

    private final OrderRepository orderRepository;

    public Long order(OrderDto orderDto, String email) {

        // 주문할 상품 정보 조회
        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);

        //회원 정보 조회
        Member member = memberRepository.findByEmail(email);


        List<OrderItem> orderItemList = new ArrayList<>();

        //주문할 상품 엔티티와 주문 수량을 이용하여 엔티티 생성
        OrderItem orderItem = OrderItem.createOrderItem(item,orderDto.getCount());


        orderItemList.add(orderItem);

        // 주문 엔티티 생성
        Order order = Order.createOrder(member,orderItemList);

        orderRepository.save(order);

        return order.getId();
    }
}
