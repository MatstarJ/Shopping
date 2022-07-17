package com.matstar.shop.service;

import com.matstar.shop.entity.ItemImg;
import com.matstar.shop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
//이미지 업로드 및 엔티티 저장 클래스
public class ItemImgService {

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception{
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        //파일 업로드
        if(!StringUtils.isEmpty(oriImgName)){
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            imgUrl = "/images/item/" + imgName;
        }

        //상품 이미지 정보 엔티티 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }


    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {

        if(!itemImgFile.isEmpty()){
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
                    .orElseThrow(EntityNotFoundException::new);

            //기존 이미지 파일이 존재할 경우 파일 삭제
            if(!StringUtils.isEmpty(savedItemImg.getImgName())) {
                fileService.deleteFile(itemImgLocation+"/"+ savedItemImg.getImgName());
            }

            String oriImgName = itemImgFile.getOriginalFilename();

            //파일 업로드
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());

            String imgUrl = "/images/item/" + imgName;

            //변경된 상품 이미지 정보 저장
            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
        }
    }

}
