package com.matstar.shop.controller;

import com.matstar.shop.dto.OrderDto;
import com.matstar.shop.dto.OrderHistDto;
import com.matstar.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    //RequesteBody : HTTP 요청의 본문 body에 담긴 내용을 자바 객체로 전달
    //ResponseBody : 자바 객체를 HTTP 요청의 body로 전달

    @PostMapping("/order")
    public @ResponseBody ResponseEntity order(@RequestBody @Valid OrderDto orderDto,
                         BindingResult bindingResult, Principal principal){

        //orderDto 객체에 데이터 바인딩 시 에러가 있는지 검사
        if(bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for(FieldError fieldError : fieldErrors) {
                 sb.append(fieldError.getDefaultMessage());
            }

            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        //로그인한 유저의 정보를 얻기 위해서 @Controller 어노테이션이 선언된 클래스에서
        // 메소드 인자로 principal 객체를 넘겨줄 경우 해당 객체에 직접 접근할 수 있다.
        String email = principal.getName();

        Long orderId;

        try{
            orderId = orderService.order(orderDto,email);

        }catch(Exception e) {
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(orderId,HttpStatus.OK);

    }

    @GetMapping(value = {"/orders", "/orders/{page}"})
    public String orderHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);
        Page<OrderHistDto> ordersHistDtoList = orderService.getOrderList(principal.getName(), pageable);

        model.addAttribute("orders", ordersHistDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);

        return "order/orderHist";
    }

    @PostMapping("/order/{orderId}/cancel")
    public @ResponseBody ResponseEntity cancelOrder (@PathVariable("orderId") Long orderId, Principal principal) {

        if(!orderService.validateOrder(orderId,principal.getName())){
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.",HttpStatus.BAD_REQUEST);
        }

        orderService.cancelOrder(orderId);

        return new ResponseEntity<Long>(orderId,HttpStatus.OK);
    }

}
