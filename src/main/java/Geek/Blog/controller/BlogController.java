package Geek.Blog.controller;

import Geek.Blog.dto.BlogDTO;
import Geek.Blog.dto.BlogDeleteDTO;
import Geek.Blog.dto.BlogEditDTO;
import Geek.Blog.entity.Blog;
import Geek.Blog.service.BlogService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/user/blog")
public class BlogController {

    private final BlogService blogService;

    @Autowired
    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createBlog(@RequestBody BlogDTO blogDTO) {

        Blog blog = blogService.create(blogDTO);

        if (blog == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("블로그 생성에 실패했습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(new BlogDTO(blog));
        }
    }

    @GetMapping("/{blogId}")
    public ResponseEntity<?> viewBlog(@PathVariable Long blogId) {
        try {
            Blog blog = blogService.viewBlog(blogId).orElseThrow(() -> new EntityNotFoundException("Blog not found with ID: " + blogId));
            return ResponseEntity.ok(new BlogDTO(blog));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/{blogId}")
    public ResponseEntity<?> editBlog(@PathVariable Long blogId, @RequestBody BlogEditDTO form) {
        try {
            Blog blog = blogService.viewBlog(blogId).orElseThrow(() -> new EntityNotFoundException("Invalid ID"));

            if (form.getBlog_name() == null || form.getBlog_name().isBlank()) {
                return ResponseEntity.badRequest().body("Invalid input");
            }

            if (Objects.equals(blog.getOwner().getId(), form.getClaimer_id())){
                blog.setBlog_name(form.getBlog_name());
                blogService.editBlog(blog);
                return ResponseEntity.ok(new BlogDTO(blog));
            }
            else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Delete Denied");
            }



        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found with ID: " + blogId);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /* 삭제 요청은 가능하면 컨트롤러 사용하지 않도록 할 예정 */
    @DeleteMapping("/{blogId}")
    public ResponseEntity<String> deleteBlog(@PathVariable Long blogId, @RequestBody BlogDeleteDTO blogDeleteDTO) {
        try {
            Blog blog = blogService.viewBlog(blogId).orElseThrow(() -> new EntityNotFoundException("Invalid ID"));

            //유저 ID 검사
            if (Objects.equals(blog.getOwner().getId(), blogDeleteDTO.getClaimer_id())){
                Integer result = blogService.deleteBlog(blog);
                if (result == -1){
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Delete Denied");
                } else if (result == 1) {
                    return ResponseEntity.ok("Deleted successfully");
                }
                else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Delete failed");
                }
            }
            else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Delete Denied");
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Blog not found with ID: " + blogId);
        }
    }
}
