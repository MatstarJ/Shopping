package com.matstar.shop.controller;

import com.matstar.shop.dto.ItemDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value="/thymeleaf")
public class ThymeleafExController {

    @GetMapping(value="/ex01")
    public String thymeleafExample01(Model model) {
        model.addAttribute("data","타임리프!");
        return "thymeleafEx/thymeleafEx01";
    }

    @GetMapping(value="/ex02")
    public String thymeleafExample02(Model model) {

        ItemDto itemDto = new ItemDto();
        itemDto.setItemDetail("상세 설명");
        itemDto.setItemNm("테스트 상품1");
        itemDto.setPrice(10000);
        itemDto.setRegTime(LocalDateTime.now());

        model.addAttribute("itemDto",itemDto);
        return "thymeleafEx/thymeleafEx02";

    }

    @GetMapping(value="ex03")
    public String thymeleaf03(Model model){

        List<ItemDto> itemDtoList = new ArrayList<>();

        for(int i =1; i<=10; i++) {

            ItemDto itemDto = new ItemDto();

            itemDto.setItemDetail("상세 설명"+i);
            itemDto.setItemNm("테스트 상품"+i);
            itemDto.setPrice(10000+i);
            itemDto.setRegTime(LocalDateTime.now());

            itemDtoList.add(itemDto);
        }

        model.addAttribute("itemDtoList",itemDtoList);

        return "thymeleafEx/thymeleafEx03";
    }

    @GetMapping("ex04")
    public String thymeleaf04(Model model) {

        List<ItemDto> itemDtoList = new ArrayList<>();

        for(int i =1; i<=10; i++) {

            ItemDto itemDto = new ItemDto();

            itemDto.setItemDetail("상세 설명"+i);
            itemDto.setItemNm("테스트 상품"+i);
            itemDto.setPrice(10000+i);
            itemDto.setRegTime(LocalDateTime.now());

            itemDtoList.add(itemDto);
        }

        model.addAttribute("itemDtoList",itemDtoList);

        return "thymeleafEx/thymeleafEx04";
    }

    @GetMapping(value="/ex05")
    public String thymeleaf07(){

        return "thymeleafEx/thymeleafEx05";

    }
}
