package com.matstar.shop.dto;

import com.matstar.shop.constant.ItemSellStatus;
import com.matstar.shop.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto {

    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemNm;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "상품 상세는 필수 입력 값입니다.")
    private String itemDetail;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;

    //상품 이미지 저장하는 리스트
    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    //상품 이미지 아이디를 저장하는 리스트, 등록시에는 상품의 이미지를 저장하지 않았기 때문에
    // 아무 값도 없고 수정시에 이미지 아이디를 담을 용도로 사용
    private List<Long> itemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem(){
        return modelMapper.map(this, Item.class);
    }

    public static ItemFormDto of(Item item){
        return modelMapper.map(item,ItemFormDto.class);
    }

}
