package com.example.BlogPost.service;

import com.example.BlogPost.DTO.ReplyDTO;
import com.example.BlogPost.entity.Reply;
import com.example.BlogPost.repository.ReplyRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class ReplyService {

    private final ReplyRepository replyRepository;

    public ReplyService(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    /**
     * 기능
     **/
    public Integer upload(ReplyDTO replyDTO) {
        Reply reply = replyRepository.upload(replyDTO);

        if (reply.getReply_id() != null){
            return reply.getReply_id();
        }
        else {
            return -1;
        }
    }

    public Optional<Reply> viewReply(Integer replyId) {
            return replyRepository.findById(replyId);
    }

    public List<Reply> listReplies() {
        return replyRepository.findAll();
    }

    public void editReply(Reply reply){
        replyRepository.edit(reply);
    }

    public void deleteReply(Reply reply){
        replyRepository.deleteById(reply.getReply_id());
    }
}
