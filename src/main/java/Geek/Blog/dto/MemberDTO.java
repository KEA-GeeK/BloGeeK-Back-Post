package Geek.Blog.dto;

import Geek.Blog.entity.Authority;
import Geek.Blog.entity.Blog;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

@ToString
public class MemberDTO { //회원 정보를 필드로 정의

    private Long id;
    private String account;
    private List<Authority> roles;
    private Blog blog;

    public MemberDTO() {
        this.roles = new ArrayList<>();
    }
}
//MemberDto Class`