package Geek.Blog.repository;

import Geek.Blog.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Optional<Member> findByAccount(String account);
    Optional<Member> findById(Long id);
    List<Member> findAll();
}
