package com.example.myblog.entity;

import com.example.myblog.constant.PostStatus;
import com.example.myblog.dto.HomePostPreviewDto;
import com.example.myblog.dto.PostDto;
import com.example.myblog.dto.PostPreviewDto;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@SqlResultSetMappings(
        value = {
                @SqlResultSetMapping(
                        name = "HomePostPreviewMapping",
                        classes = @ConstructorResult(
                                targetClass = HomePostPreviewDto.class,
                                columns = {
                                        @ColumnResult(name = "blogId", type = Long.class),
                                        @ColumnResult(name = "blogNm", type = String.class),
                                        @ColumnResult(name = "blogImgUrl", type = String.class),
                                        @ColumnResult(name = "postId", type = Long.class),
                                        @ColumnResult(name = "title", type = String.class),
                                        @ColumnResult(name = "previewContents", type = String.class),
                                        @ColumnResult(name = "postImgUrl", type = String.class),
                                        @ColumnResult(name = "regTime", type = String.class),
                                        @ColumnResult(name = "topicId", type = Long.class),
                                        @ColumnResult(name = "commentCount", type = Long.class),
                                }
                        )
                ),
                @SqlResultSetMapping(
                        name = "PostPreviewMapping",
                        classes = @ConstructorResult(
                                targetClass = PostPreviewDto.class,
                                columns = {
                                        @ColumnResult(name = "postId", type = Long.class),
                                        @ColumnResult(name = "title", type = String.class),
                                        @ColumnResult(name = "contents", type = String.class),
                                        @ColumnResult(name = "previewContents", type = String.class),
                                        @ColumnResult(name = "postImgId", type = Long.class),
                                        @ColumnResult(name = "postImgUrl", type = String.class),
                                        @ColumnResult(name = "postImgNm", type = String.class),
                                        @ColumnResult(name = "regTime", type = String.class),
                                        @ColumnResult(name = "commentCount", type = Long.class),
                                }
                        )
                ),
        }
)

@NamedNativeQueries(
        value = {
                @NamedNativeQuery(
                        name = "HomePostPreviewPaging",
                        query = "select b.blog_id as blogId, b.blog_nm as blogNm, bimg.img_url as blogImgUrl, p.post_id as postId, p.title as title, SUBSTRING(p.preview_contents, 1, 200) as previewContents, img.img_url as postImgUrl, date_format(p.reg_time, '%Y-%m-%d') as regTime, b.topic_id as topicId, (select COUNT(*) from Comment c where c.post_id = p.post_id) as commentCount " +
                                "from post p left join blog b on b.blog_id = p.blog_id " +
                                "left join blog_img bimg on bimg.blog_id = b.blog_id and bimg.repimg_yn = 'y' " +
                                "left join post_img img on img.post_id = p.post_id and img.repimg_yn = 'y' " +
                                "where b.topic_id = :topicId and p.post_status = 'PUBLIC'",
                        resultSetMapping = "HomePostPreviewMapping"
                ),
                @NamedNativeQuery(
                        name = "BlogPostPreviewPaging",
                        query = "select p.post_id as postId, p.title as title, p.content_url as contents, case when p.post_status = 'public' then SUBSTRING(p.preview_contents, 1, 200) else null end as previewContents, " +
                                "img.post_img_id as postImgId, img.img_url as postImgUrl, img.img_name as postImgNm, date_format(p.reg_time, '%Y-%m-%d') as regTime, (select COUNT(*) from Comment c where c.post_id = p.post_id) as commentCount " +
                                "from post p left join blog b on b.blog_id = p.blog_id " +
                                "left join post_img img on p.post_id = img.post_id and img.repimg_yn = 'y' " +
                                "where b.blog_nm = :blogNm and p.post_status in('public', 'permitted')",
                        resultSetMapping = "PostPreviewMapping"
                ),
                @NamedNativeQuery(
                        name = "CategoryPostPreviewPaging",
                        query = "select p.post_id as postId, p.title as title, p.content_url as contents, case when p.post_status = 'public' then SUBSTRING(p.preview_contents, 1, 200) else null end as previewContents, " +
                                "img.post_img_id as postImgId, img.img_url as postImgUrl, img.img_name as postImgNm, date_format(p.reg_time, '%Y-%m-%d') as regTime, (select COUNT(*) from Comment c where c.post_id = p.post_id) as commentCount " +
                                "from post p left join blog b on b.blog_id = p.blog_id " +
                                "left join post_img img on p.post_id = img.post_id and img.repimg_yn = 'y' " +
                                "where p.category_id = :categoryId and p.post_status in('public', 'permitted')",
                        resultSetMapping = "PostPreviewMapping"
                ),
        }
)
@Entity
@Table(name = "post")
@Getter
@ToString
public class Post extends BaseEntity {
    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_id")
    private Blog blog;

    @Lob
    private String contentUrl;

    @Lob
    private String previewContents;

    @Enumerated(EnumType.STRING)
    private PostStatus postStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public void createEmptyPost(Blog blog, Category category) {
        this.blog = blog;
        this.category = category;
    }

    public void createPost(PostDto postDto, Blog blog, Category category) {
        this.title = postDto.getTitle();
        this.contentUrl = postDto.getContentUrl();
        this.postStatus = postDto.getPostStatus();
        this.blog = blog;
        this.category = category;
    }

    public void updatePost(PostDto postDto, Blog blog, Category category) {
        this.id = postDto.getId();
        this.title = postDto.getTitle();
        this.contentUrl = postDto.getContentUrl();
        this.previewContents = postDto.getPreviewContents();
        this.postStatus = postDto.getPostStatus();
        this.blog = blog;
        this.category = category;
    }
    public void deletePost(){
        this.postStatus = PostStatus.DELETE;
    }
}
