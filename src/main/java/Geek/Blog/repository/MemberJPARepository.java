package Geek.Blog.repository;

import Geek.Blog.entity.Member;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberJPARepository implements MemberRepository{

    private final EntityManager em;

    public MemberJPARepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Member> findAll() {
        return em.createQuery("SELECT m FROM Member m", Member.class).getResultList();
    }

    @Override
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findByAccount(String account) {
        List<Member> results = em.createQuery("SELECT m from Member m WHERE m.account = :account", Member.class)
                .setParameter("account", account)
                .getResultList();
        return results.stream().findFirst();
    }

}
