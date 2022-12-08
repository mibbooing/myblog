package com.example.myblog.service;

import com.example.myblog.entity.MemberImg;
import com.example.myblog.repository.MemberImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.File;

@Service
@RequiredArgsConstructor
@Transactional
public class ImgService {

    @Value("${memberImgLocation}")
    private String memberImgLocation;

    private final MemberImgRepository memberImgRepository;

    private final FileService fileService;

    public void saveMemberImg(MemberImg memberImg, MultipartFile memberImgFile)throws Exception{
        String oriImgName = memberImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";
        File folder = new File(memberImgLocation + "/" +memberImg.getMember().getId());
        if(!folder.exists()){
            try{
                folder.mkdir();
                System.out.println(memberImg.getMember().getName() + "의 신규폴더 생성");
            } catch (Exception e){
                e.getStackTrace();
            }
        }

        if(!StringUtils.isEmpty(oriImgName)){
            imgName = fileService.uploadFile(folder.getPath(), oriImgName, memberImgFile.getBytes());
            imgUrl = "/images/member/" + memberImg.getMember().getId() + "/" + imgName;
        }

        memberImg.updateMemberImg(oriImgName, imgName, imgUrl);
        memberImgRepository.save(memberImg);
    }

    public void updateMemberImg(MemberImg savedMemberImg, MultipartFile memberImgFile)throws Exception{
        if(!memberImgFile.isEmpty()){
            String imgPath = memberImgLocation + "/" +savedMemberImg.getMember().getId();
            if(!StringUtils.isEmpty(savedMemberImg.getImgName())){
                fileService.deleteFile(imgPath+savedMemberImg.getImgName());
            }
            String oriImgName = memberImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(imgPath, oriImgName, memberImgFile.getBytes());
            String imgUrl = "/images/member/" + savedMemberImg.getMember().getId() + "/" +imgName;
            savedMemberImg.updateMemberImg(oriImgName, imgName, imgUrl);
        }

    }
}
