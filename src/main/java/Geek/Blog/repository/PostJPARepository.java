package Geek.Blog.repository;

import Geek.Blog.dto.PostDTO;
import Geek.Blog.entity.Category;
import Geek.Blog.entity.Member;
import Geek.Blog.entity.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PostJPARepository implements PostRepository{

    private final EntityManager em;
    private final MemberRepository memberRepository;

    public PostJPARepository(EntityManager em, MemberRepository memberRepository) {
        this.em = em;
        this.memberRepository = memberRepository;
    }

    @Override
    public Optional<Post> upload(PostDTO postDTO) {
        try {
            Member author = memberRepository.findById(postDTO.getClaimer_id())
                    .orElseThrow(() -> new EntityNotFoundException("Member not found with ID: " + postDTO.getClaimer_id()));

            Post post = Post.builder()
                    .post_title(postDTO.getPost_title())
                    .contents(postDTO.getContents())
                    .author(author)
                    .category(postDTO.getCategory())
                    .build();


            em.persist(post);
            return Optional.of(post);
        }catch(EntityNotFoundException e){
            return Optional.empty();
        }
    }

    @Override
    public Optional<Post> findById(Long id) {
        Post post = em.find(Post.class, id);
        return Optional.ofNullable(post);
    }

    @Override
    public List<Post> findAll() {
        return em.createQuery("SELECT p FROM Post p", Post.class).getResultList();
    }

    @Override
    public List<Post> findCategoryPost(Category category) {
        return em.createQuery("SELECT p FROM Post p WHERE p.category = :category", Post.class)
                .setParameter("category", category).getResultList();
    }

    @Override
    public Post edit(Post post) {
        Query query =  em.createQuery("UPDATE Post p SET p.post_title = :newTitle, p.contents = :newContent, p.category = :category WHERE p.post_id = :id");
        query.setParameter("newTitle", post.getPost_title());
        query.setParameter("newContent", post.getContents());
        query.setParameter("category", post.getCategory());
        query.setParameter("id", post.getPost_id());

        query.executeUpdate();

        return post;
    }

    @Override
    public Integer deleteById(Long id) {
        Query query = em.createQuery("DELETE FROM Post p WHERE p.post_id = :id");
        query.setParameter("id", id);

        return query.executeUpdate();
    }
}
