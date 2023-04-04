package com.example.myblog.service;


import com.example.myblog.constant.CategoryType;
import com.example.myblog.constant.PostStatus;
import com.example.myblog.dto.*;
import com.example.myblog.entity.*;
import com.example.myblog.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;

    private final MemberRepository memberRepository;
    private final BlogRepository blogRepository;
    private final CategoryRepository categoryRepository;
    private final PostImgRepository postImgRepository;
    private final ImgService imgService;

    public PostFormDto getPostForm(String blogNm) {
        PostFormDto postFormDto = new PostFormDto();
        postFormDto.setPostForm(blogNm, categoryService.getCategory(blogNm), new TypeSet().createPostStatusSet());
//        postFormDto.setCategoryDtoList(categoryService.getCategory(blogNm));
//        postFormDto.setBlogNm(blogNm);
//        postFormDto.setPostStatusList(new TypeSet().createPostStatusSet());
        return postFormDto;
    }


    public Post createPost(String blogNm) {
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
        if (!postFormDto.getPostImgDtoList().isEmpty()) {
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

    public void updatePost(PostFormDto postFormDto) throws Exception {
        Blog blog = blogRepository.findByBlogNm(postFormDto.getBlogNm());
        Category category = categoryRepository.findById(postFormDto.getPostDto().getCategoryId()).orElseThrow(EntityExistsException::new);
        Post post = postRepository.findById(postFormDto.getPostDto().getId()).orElseThrow(EntityExistsException::new);
        if (!postFormDto.getPostImgDtoList().isEmpty()) {
            updatePostImg(postFormDto, blog, post);
        }
        post.updatePost(postFormDto.getPostDto(), blog, category);
    }

    public void updatePostImg(PostFormDto postFormDto, Blog blog, Post post) throws Exception {
        ImgSaveTypeDto imgSaveTypeDto = new ImgSaveTypeDto(blog.getId(), "post", blog.getBlogNm());
        Map<String, Object> map = new HashMap<>();
        map.put("post", post);
        map.put("postImgDtoList", postFormDto.getPostImgDtoList());
        List<Long> newPostImgIdList = new ArrayList<>();
        List<Long> savedPostImgIdList = new ArrayList<>();
        for(PostImgDto postImgDto : postFormDto.getPostImgDtoList()){
            if(postImgDto.getPostId() != null){
                newPostImgIdList.add(postImgDto.getId());
            }
        }
        List<PostImg> savedPostImgList = postImgRepository.findAllByPostId(post.getId());
        if(savedPostImgList != null) {
            for (PostImg postImg : savedPostImgList) {
                savedPostImgIdList.add(postImg.getId());
            }
            List<Long> deleteList = savedPostImgIdList.stream().filter(i -> !newPostImgIdList.contains(i)).collect(Collectors.toList());
            for (Long deletePostImgId : deleteList) {
                for (PostImg postImg : savedPostImgList) {
                    if (postImg.getId() == deletePostImgId) {
                        postImgRepository.delete(postImg);
                    }
                }
            }
        }
        imgService.saveImg(imgSaveTypeDto, null, map);
    }

    public PostFormDto getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("ERR_POST_NUM"));
        List<CategoryDto> categoryDtoList = categoryRepository.findByBlogId(post.getBlog().getId());
        PostFormDto postFormDto = new PostFormDto();
        postFormDto.setPostReadData(post.getBlog().getMember().getEmail(), new PostDto(post), post.getBlog(), categoryDtoList, postImgRepository.findOneByPostId(postId));
        return postFormDto;
    }

    public List<PostImgDto> getPostImgList(Long postId) {
        return postImgRepository.findAllByPostIdd(postId);
    }

    public String deletePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(()-> new EntityExistsException("ERR_POST_NUM"));
        PostDto postDto = new PostDto(post);
        postDto.setPostStatus(PostStatus.DELETE);
        post.updatePost(postDto, post.getBlog(), post.getCategory());
        return post.getBlog().getBlogNm();
    }
}
