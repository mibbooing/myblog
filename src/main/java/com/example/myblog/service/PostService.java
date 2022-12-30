package com.example.myblog.service;


import com.example.myblog.constant.CategoryType;
import com.example.myblog.dto.ImgSaveTypeDto;
import com.example.myblog.dto.TypeSet;
import com.example.myblog.dto.PostFormDto;
import com.example.myblog.entity.Blog;
import com.example.myblog.entity.Category;
import com.example.myblog.entity.Post;
import com.example.myblog.repository.BlogRepository;
import com.example.myblog.repository.CategoryRepository;
import com.example.myblog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityExistsException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;

    private final BlogRepository blogRepository;
    private final CategoryRepository categoryRepository;
    private final ImgService imgService;

    public PostFormDto getPostForm(String blogNm){
        PostFormDto postFormDto = new PostFormDto();
        postFormDto.setCategoryDtoList(categoryService.getCategory(blogNm));
        postFormDto.setBlogNm(blogNm);
        postFormDto.setPostStatusList(new TypeSet().createPostStatusSet());
        return postFormDto;
    }


    public Post createPost(String blogNm){
        Blog blog = blogRepository.findByBlogNm(blogNm);
        Category category = categoryRepository.findByBlogIdAndDepth(blog.getId(), CategoryType.DEFAULT);
        Post post = new Post();
        post.createEmptyPost(blog, category);
        return postRepository.save(post);
    }

    public void savePost(PostFormDto postFormDto) throws Exception {
        Blog blog = blogRepository.findByBlogNm(postFormDto.getBlogNm());
        Category category = categoryRepository.findById(postFormDto.getPostDto().getCategoryId()).orElseThrow(EntityExistsException::new);
        Post post = postRepository.findById(postFormDto.getPostDto().getId()).orElseThrow(EntityExistsException::new);
        if(!postFormDto.getPostImgDtoList().isEmpty()){
            savePostImg(postFormDto, blog, post);
        }
        post.updatePost(postFormDto.getPostDto(), blog, category);
    }

    public void savePostImg(PostFormDto postFormDto, Blog blog, Post post) throws Exception {
        ImgSaveTypeDto imgSaveTypeDto = new ImgSaveTypeDto(blog.getId(), "post", blog.getBlogNm());
        Map<String, Object> map = new HashMap<>();
        map.put("post", post);
        map.put("postImgDtoList", postFormDto.getPostImgDtoList());
        imgService.saveImg(imgSaveTypeDto, null, map);
    }

    public void getPost(){

    }

    public List<Post> getPostList(){
        return null;
    }

    public void updatePost(){

    }

    public void deletePost(){

    }
}
