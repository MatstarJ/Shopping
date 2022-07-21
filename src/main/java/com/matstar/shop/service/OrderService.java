package com.matstar.shop.service;

import com.matstar.shop.dto.OrderDto;
import com.matstar.shop.dto.OrderHistDto;
import com.matstar.shop.dto.OrderItemDto;
import com.matstar.shop.entity.*;
import com.matstar.shop.repository.ItemImgRepository;
import com.matstar.shop.repository.ItemRepository;
import com.matstar.shop.repository.MemberRepository;
import com.matstar.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

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

    private final ItemImgRepository itemImgRepository;

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


    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable) {

        // 주문 내역 리스트
        List<Order> orders = orderRepository.findOrders(email, pageable);

        //주문 총 횟수
        Long totalCount = orderRepository.countOrder(email);

        //Dto로 변환하기 위해 생성
        List<OrderHistDto> orderHistDtos = new ArrayList<>();

        for (Order order : orders) {
            OrderHistDto orderHistDto = new OrderHistDto(order);
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn
                        (orderItem.getItem().getId(), "Y");
                OrderItemDto orderItemDto =
                        new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistDto.addOrderItemDto(orderItemDto);
            }

            orderHistDtos.add(orderHistDto);
        }

        return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
    }

    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email) {
        Member curMember = memberRepository.findByEmail(email);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        Member savedMember = order.getMember();

        if(!StringUtils.equals(curMember.getEmail(),savedMember.getEmail())){
            return false;
        }

        return true;
    }

    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        order.cancelOrder();
    }

}
