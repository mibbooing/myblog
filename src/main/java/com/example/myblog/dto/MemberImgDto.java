package com.example.myblog.dto;


import com.example.myblog.entity.MemberImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class MemberImgDto {

    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repimgYn;

    public MemberImgDto(Long id, String imgName, String oriImgName, String imgUrl, String repimgYn) {
        this.id = id;
        this.imgName = imgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
        this.repimgYn = repimgYn;
    }
    /*    private static ModelMapper modelMapper = new ModelMapper();

    public static MemberImgDto of(MemberImg memberImg){
        return modelMapper.map(memberImg,MemberImgDto.class);
    }*/
}
