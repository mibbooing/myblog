package com.example.myblog.service;

import com.example.myblog.dto.*;
import com.example.myblog.entity.*;
import com.example.myblog.repository.BlogImgRepository;
import com.example.myblog.repository.MemberImgRepository;
import com.example.myblog.repository.MemberRepository;
import com.example.myblog.repository.PostImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class ImgService {

    @Value("${memberImgLocation}")
    private String memberImgLocation;
    @Value("${blogImgLocation}")
    private String blogImgLocation;
    @Value("${postImgLocation}")
    private String postImgLocation;

    private String imgLocation;

    private final MemberImgRepository memberImgRepository;

    private final FileService fileService;

    private final BlogImgRepository blogImgRepository;

    private final PostImgRepository postImgRepository;


    public void saveImg(ImgSaveTypeDto imgSaveTypeDto, MultipartFile imgFile, Map<String, Object> map) throws Exception {
        if (imgFile == null) {
            for (PostImgDto postImgDto : (List<PostImgDto>) map.get("postImgDtoList")) {
                if (postImgDto.getId() == null) {
                    ImgDto imgDto = new ImgDto(null, postImgDto.getImgName(), postImgDto.getOriImgName(), postImgDto.getImgUrl(), postImgDto.getRepimgYn());
                    saveExec(imgSaveTypeDto.getType(), imgDto, map);
                } else {
                    ImgDto imgDto = new ImgDto(postImgDto.getId(), postImgDto.getImgName(), postImgDto.getOriImgName(), postImgDto.getImgUrl(), postImgDto.getRepimgYn());
                    updateImg(imgDto, imgSaveTypeDto, null, map);
                }
            }
        } else {
            String oriImgName = imgFile.getOriginalFilename();
            String imgName = "";
            String imgUrl = "";
            typeCheck(imgSaveTypeDto.getType());
            File folder = fileService.makePath(imgLocation + "/" + imgSaveTypeDto.getName());
            if (!StringUtils.isEmpty(oriImgName)) {
                imgName = fileService.uploadFile(folder.getPath(), oriImgName, imgFile.getBytes());
                imgUrl = "/images/" + imgSaveTypeDto.getType() + "/" + imgSaveTypeDto.getName() + "/" + imgName;
            }
            ImgDto imgDto = new ImgDto(null, imgName, oriImgName, imgUrl, "Y");
            saveExec(imgSaveTypeDto.getType(), imgDto, map);
        }
    }

    public void updateImg(ImgDto imgDto, ImgSaveTypeDto imgSaveTypeDto, MultipartFile imgFile, Map<String, Object> map) throws Exception {
        if (imgFile == null) {
            updateExec(imgSaveTypeDto.getType(), imgDto, map);
        } else {
            if (!imgFile.isEmpty()) {
                typeCheck(imgSaveTypeDto.getType());
                String imgPath = imgLocation + "/" + imgSaveTypeDto.getName();
                if (!StringUtils.isEmpty(imgDto.getImgName())) {
                    fileService.deleteFile(imgPath + imgDto.getImgName());
                }
                String oriImgName = imgFile.getOriginalFilename();
                String imgName = fileService.uploadFile(imgPath, oriImgName, imgFile.getBytes());
                String imgUrl = "/images/" + imgSaveTypeDto.getType() + "/" + imgSaveTypeDto.getName() + "/" +imgName;
                imgDto.updateImgDto(imgName, oriImgName, imgUrl, "N");

                updateExec(imgSaveTypeDto.getType(), imgDto, map);
            }
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
            case "post":
                PostImg postImg = new PostImg(imgDto, (Post) map.get("post"));
                postImgRepository.save(postImg);
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
            case "post":
                PostImg postImg = postImgRepository.findById(imgDto.getId()).orElseThrow(IllegalStateException::new);
                postImg.updatePostImg(imgDto, (Post) map.get("post"));
                break;
        }
    }
}
