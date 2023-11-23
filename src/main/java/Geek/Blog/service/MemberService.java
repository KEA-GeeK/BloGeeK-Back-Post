package Geek.Blog.service;

import Geek.Blog.Response.SignInResponse;
import Geek.Blog.dto.MemberDto;
import Geek.Blog.dto.SignInRequestDTO;

public interface MemberService {

    public SignInResponse login(SignInRequestDTO request) throws Exception;
    public String signUp(MemberDto requestDto) throws Exception;

}