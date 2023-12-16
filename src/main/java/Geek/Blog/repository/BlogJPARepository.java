package Geek.Blog.repository;

import Geek.Blog.dto.BlogDTO;
import Geek.Blog.entity.Blog;
import Geek.Blog.entity.Member;
import Geek.Blog.util.ImageUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class BlogJPARepository implements BlogRepository{

    private final EntityManager em;
    private final MemberRepository memberRepository;

    public BlogJPARepository(EntityManager em, MemberRepository memberRepository) {
        this.em = em;
        this.memberRepository = memberRepository;
    }

    @Override
    public Blog create(BlogDTO blogDTO) {
        Member owner = memberRepository.findById(blogDTO.getOwner_id())
                .orElseThrow(() -> new EntityNotFoundException("Member not found with ID: " + blogDTO.getOwner_id()));
        if (owner.getBlog().getBlog_id() != null){
            throw new EntityNotFoundException("Blog already exists");
        }

        Blog blog = Blog.builder()
                .blog_name(blogDTO.getBlog_name())
                .about_blog(blogDTO.getAbout_blog())
                .profile_picture(blogDTO.getProfilePicture())
                .owner(owner)
                .build();

        em.persist(blog);
        return blog;
    }

    @Override
    public Optional<Blog> findById(Long id) {
        Blog blog = em.find(Blog.class, id);
        return Optional.ofNullable(blog);
    }

    @Override
    public Blog edit(Blog blog) {
        Query query = em.createQuery("UPDATE Blog b SET b.blog_name = :newBlogName, b.about_blog = :newAboutBlog, b.profile_picture = :newProfilePicture WHERE b.blog_id = :id");
        query.setParameter("newBlogName", blog.getBlog_name());
        query.setParameter("newAboutBlog", blog.getAbout_blog());
        if(blog.getProfile_picture() == null) { //이미지 삭제 요청이 들어올 경우
            query.setParameter("newProfilePicture", ImageUtil.convertImageToByteArray("src/main/resources/static/images/default-profile.png"));
        }
        else {
            query.setParameter("newProfilePicture", blog.getProfile_picture());
        }
        query.setParameter("id", blog.getBlog_id());

        query.executeUpdate();
        return blog;
    }

    @Override
    public Integer delete(Blog blog) {
        Member member = memberRepository.findById(blog.getOwner().getId()).orElse(null);

        if (member != null) {
            return -1;
        }

        Query query = em.createQuery("DELETE FROM Blog b WHERE b.blog_id = :id");
        query.setParameter("id", blog.getBlog_id());

        return query.executeUpdate();
    }
}
