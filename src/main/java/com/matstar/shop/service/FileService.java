package com.matstar.shop.service;

import lombok.extern.java.Log;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
//이미지 파일 처리 클래스, 파일이름 정의 및 파일 업로드 실행
public class FileService {

    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData)
            throws Exception{


        UUID uuid = UUID.randomUUID();

        System.out.println("파일이름 : " + originalFileName);

        //확장자 추출
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

        // uuid + 확장자, 새로운 파일이름 정의
        String savedFileName = uuid.toString() + extension;

        // 최종 파일 업로드 경로
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;

        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);

        fos.write(fileData);
        fos.close();

        //저장된 파일 이름 반환
        return savedFileName;
    }

    public void deleteFile(String filePath) throws Exception{
        File deleteFile = new File(filePath);
        if(deleteFile.exists()) {
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }

}
