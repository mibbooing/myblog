package com.example.myblog.service;

import com.example.myblog.dto.BlogImgDto;
import com.example.myblog.dto.BlogInfoFormDto;
import com.example.myblog.dto.ImgDto;
import com.example.myblog.dto.ImgSaveTypeDto;
import com.example.myblog.entity.Blog;
import com.example.myblog.entity.BlogImg;
import com.example.myblog.entity.Member;
import com.example.myblog.entity.MemberImg;
import com.example.myblog.repository.BlogImgRepository;
import com.example.myblog.repository.MemberImgRepository;
import com.example.myblog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class ImgService {

    @Value("${memberImgLocation}")
    private String memberImgLocation;
    @Value("${blogImgLocation}")
    private String blogImgLocation;

    private String imgLocation;

    private final MemberImgRepository memberImgRepository;

    private final FileService fileService;

    private final BlogImgRepository blogImgRepository;


    public void saveImg(ImgSaveTypeDto imgSaveTypeDto, MultipartFile imgFile, Map<String, Object> map) throws Exception {
        String oriImgName = imgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";
        typeCheck(imgSaveTypeDto.getType());
        File folder = new File(imgLocation + "/" + imgSaveTypeDto.getName());
        if (!folder.exists()) {
            try {
                folder.mkdirs();
                System.out.println(imgSaveTypeDto.getName() + "의 신규폴더 생성");
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
        if (!StringUtils.isEmpty(oriImgName)) {
            imgName = fileService.uploadFile(folder.getPath(), oriImgName, imgFile.getBytes());
            imgUrl = "/images/" + imgSaveTypeDto.getType() + "/" + imgSaveTypeDto.getName() + "/" + imgName;
        }
        ImgDto imgDto = new ImgDto(null, imgName, oriImgName, imgUrl, "Y");
        saveExec(imgSaveTypeDto.getType(), imgDto, map);
    }

    public void updateImg(ImgDto imgDto, ImgSaveTypeDto imgSaveTypeDto, MultipartFile imgFile, Map<String, Object> map) throws Exception {
        if (!imgFile.isEmpty()) {
            typeCheck(imgSaveTypeDto.getType());
            String imgPath = imgLocation + "/" + imgSaveTypeDto.getName();
            if (!StringUtils.isEmpty(imgDto.getImgName())) {
                fileService.deleteFile(imgPath + imgDto.getImgName());
            }
            String oriImgName = imgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(imgPath, oriImgName, imgFile.getBytes());
            String imgUrl = "/images/" + imgSaveTypeDto.getType() + "/" + imgName;
            imgDto.updateImgDto(imgName, oriImgName, imgUrl, "N");

            updateExec(imgSaveTypeDto.getType(), imgDto, map);
        }
    }

    private void typeCheck(String type) {
        switch (type) {
            case "member":
                imgLocation = memberImgLocation;
                break;
            case "blog":
                imgLocation = blogImgLocation;
                break;
        }
    }

    private void saveExec(String requestType, ImgDto imgDto, Map<String, Object> map) {
        switch (requestType) {
            case "member":
                MemberImg memberImg = new MemberImg(imgDto, (Member) map.get("member"));
                memberImgRepository.save(memberImg);
                break;
            case "blog":
                BlogImg blogImg = new BlogImg(imgDto, (Blog) map.get("blog"));
                blogImgRepository.save(blogImg);
                break;
        }
    }

    private void updateExec(String requestType, ImgDto imgDto, Map<String, Object> map) {
        switch (requestType) {
            case "member":
                MemberImg memberImg = memberImgRepository.findById(imgDto.getId()).orElseThrow(IllegalStateException::new);
                memberImg.updateMemberImg(imgDto, (Member) map.get("member"));
                break;
            case "blog":
                BlogImg blogImg = blogImgRepository.findById(imgDto.getId()).orElseThrow(IllegalStateException::new);
                blogImg.updateBlogImg(imgDto, (Blog) map.get("blog"));
                break;
        }
    }
}
