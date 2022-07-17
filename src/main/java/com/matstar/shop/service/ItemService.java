package com.matstar.shop.service;

import com.matstar.shop.dto.ItemFormDto;
import com.matstar.shop.dto.ItemImgDto;
import com.matstar.shop.dto.ItemSearchDto;
import com.matstar.shop.dto.MainItemDto;
import com.matstar.shop.entity.Item;
import com.matstar.shop.entity.ItemImg;
import com.matstar.shop.repository.ItemImgRepository;
import com.matstar.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor

// 아이템 엔티티 저장 및 파일 업로드 실행
// 아이템 엔티티 저장(ItemService) -> 아이템 이미지 엔티티 저장 & 업로드 실행 (ItemImgService)
public class ItemService {

    private final ItemRepository itemRepository;

    private final ItemImgService itemImgService;

    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{

        //상품 등록 dto -> entity
        Item item = itemFormDto.createItem();
        itemRepository.save(item);

        //이미지 등록
        for(int i=0;i<itemImgFileList.size();i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);

            if(i == 0) {
                //첫 번째 이미지일 경우 대표 상품 이미지 여부 값을 Y로 준다.
                itemImg.setRepimgYn("Y");
            }else {
                itemImg.setRepimgYn("N");
            }

            //파일 엔티티를 저장하고 업로드 하는 메소드
            //아이템 엔티티와 파일리스트 전달
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }

        return item.getId();
    }


    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId) {

        //상품 이미지 조회
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);

        List<ItemImgDto> itemImgDtoList = new ArrayList<>();

        //조회한  ItemImg 엔티티를 ItemImgDto 객체로 만들어서 리스트에 추가
        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

//        상품의 Id를 통해 상품 엔티티 조회, 존재하지 않을 때는 Exception
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);

        ItemFormDto itemFormDto = ItemFormDto.of(item);

        itemFormDto.setItemImgDtoList(itemImgDtoList);

        return itemFormDto;

    }


    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

        //상품 수정

        //상품 아이디를 이용하여 상품 엔티티 조회
        Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityNotFoundException::new);

        //전달받은 FormDto를 통해 상품 엔티티 업데이트트
        item.updateItem(itemFormDto);

        //상품 이미지 아이디 리스트 조회
        List<Long> itemImgIds = itemFormDto.getItemImgIds();

        //이미지 등록
        //상품 이미지를 업데이트 하기 위해서 updateItemImg() 메소드 상에 상품 이미지 아이디와 상품
        // 이미지 파일 정보를 파라미터로 전달
        for(int i=0;i<itemImgFileList.size();i++){
            itemImgService.updateItemImg(itemImgIds.get(i),
                    itemImgFileList.get(i));
        }
        return item.getId();
    }


    // 상품 조회 조건과 페이지 정보를 파라미터로 받아서
    // 상품 데이터를 조회하는 메소드
    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getAdminItemPage(itemSearchDto,pageable);
    }

    @Transactional(readOnly=true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {

        return itemRepository.getMainItemPage(itemSearchDto,pageable);
    }

}
