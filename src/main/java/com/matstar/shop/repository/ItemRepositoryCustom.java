package com.matstar.shop.repository;

import com.matstar.shop.dto.ItemSearchDto;
import com.matstar.shop.dto.MainItemDto;
import com.matstar.shop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    //사용자 정의 인터페이스
    //검색 조건을 담고 있는 ItemSearchDto 와 페이징 정보를 담고 있는 Pageable 객체를 파라미터로 받는다.

    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

}
