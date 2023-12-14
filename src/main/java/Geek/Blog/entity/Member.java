package Geek.Blog.entity;

import Geek.Blog.dto.MemberDTO;
import jakarta.persistence.*;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
@Getter
@Setter
@Table(name = "Member")
public class Member {
    private static final Logger log = LoggerFactory.getLogger(Member.class);

    @Id @Column (name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;

    @Column(unique = true, length = 20)
    private String account;

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Authority> roles = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "owner", orphanRemoval = true)
    private Blog blog;


    private void setRoles(List<Authority> role){
        this.roles = role;
        role.forEach(o -> o.setMember(this));
    }

    public Member(MemberDTO memberDTO){
        this.setId(memberDTO.getId());
        this.setAccount(memberDTO.getAccount());
        this.setRoles(memberDTO.getRoles());
        this.setBlog(memberDTO.getBlog());
    }
}