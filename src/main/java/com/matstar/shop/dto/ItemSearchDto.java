package com.matstar.shop.dto;

import com.matstar.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchDto {

    //현재 시간과 상품 등록일을 비교해서 상품 데이터 조회
    // all: 상품 등록일 전체, 1d, 1w, 1m, 6m
    private String searchDateType;

    //판매상태 기준
    private ItemSellStatus searchSellStatus;

    // 검색 유형 : itemNm : 상품명,  createBy : 상품 등록자 아이디.
    private String searchBy;

    // 검색어 저장 변수
    private String searchQuery  = "";
}
