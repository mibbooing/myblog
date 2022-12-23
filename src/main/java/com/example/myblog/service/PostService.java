package com.example.myblog.service;


import com.example.myblog.dto.CategoryDto;
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
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;

    private final BlogRepository blogRepository;
    private final CategoryRepository categoryRepository;

    public PostFormDto getPostForm(String blogNm){
        PostFormDto postFormDto = new PostFormDto();
        postFormDto.setCategoryDtoList(categoryService.getCategory(blogNm));
        postFormDto.setBlogNm(blogNm);
        return postFormDto;
    }

    public void savePost(PostFormDto postFormDto, MultipartFile multipartFile){
        Blog blog = blogRepository.findByBlogNm(postFormDto.getBlogNm());
        Category category = categoryRepository.findById(postFormDto.getPostDto().getCategoryId()).orElseThrow(EntityExistsException::new);
        Post post = new Post();
        post.createPost(postFormDto.getPostDto(), blog, category);
        postRepository.save(post);
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
