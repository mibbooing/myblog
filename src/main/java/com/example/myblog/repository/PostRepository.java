package com.example.myblog.repository;

import com.example.myblog.dto.HomePostPreviewDto;
import com.example.myblog.dto.PostDto;
import com.example.myblog.dto.PostPreviewDto;
import com.example.myblog.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

//    @Query("select new com.example.myblog.dto.CategoryDto(c.id, c.categoryNm, c.depth, c.blog.id, c.parentCategory.id, c.sortNum ) " +
//                "from Category c join c.blog b " +
//                "where b.id = c.blog.id " +
//                "and c.blog.id = :blogId " +
//                "order by c.depth asc, c.parentCategory.id asc, c.sortNum asc"
//    )
//    List<PostPreviewDto> findByBlogNm(String blogNm);
//    @Query("select new com.example.myblog.dto.PostDto(p.id, p.title, p.blog.id, p.contentUrl, p.postStatus, c.id, c.categoryNm) " +
//            "from Post p join p.category c " +
//            "where c.id = p.category.id " +
//            "and p.id = :id")
//    PostDto findByPostId(Long id);
    @Query("select new com.example.myblog.dto.PostPreviewDto(p.id, p.title, p.contentUrl, case when p.postStatus=com.example.myblog.constant.PostStatus.PUBLIC then SUBSTRING(p.previewContents, 1, 200) else null end, img.id, img.imgUrl, img.imgName, p.regTime) " +
            "from PostImg img right join img.post p " +
            "on p.blog.blogNm = :blogNm " +
            "and p.id = img.post.id " +
            "and img.repimgYn = 'Y'" +
            "where p.postStatus in(com.example.myblog.constant.PostStatus.PUBLIC, com.example.myblog.constant.PostStatus.PERMITTED)")
    List<PostPreviewDto> findByBlogNm(String blogNm);

    @Query("select new com.example.myblog.dto.HomePostPreviewDto(b.id, b.blogNm, bimg.imgUrl, p.id, p.title, SUBSTRING(p.previewContents, 1, 200), pimg.imgUrl, p.regTime) " +
            "from Post p left join p.blog b on b.id = p.blog.id " +
            "left join BlogImg bimg on bimg.blog.id = b.id " +
            "left join PostImg pimg on pimg.post.id = p.id " +
            "and pimg.repimgYn = 'Y' " +
            "and bimg.repimgYn = 'Y' " +
            "where b.topic.id = :topicId and p.postStatus = com.example.myblog.constant.PostStatus.PUBLIC")
    Page<HomePostPreviewDto> findByTopicId(Pageable pageable, Long topicId);
}
