package Geek.Blog.service;

import Geek.Blog.dto.LikeDTO;
import Geek.Blog.entity.Like;
import Geek.Blog.repository.LikeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class LikeService {

    private final LikeRepository likeRepository;

    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }


    public Like addLike(LikeDTO likeDTO) {
        try {
            Like like;
            Optional<Like> result = likeRepository.findByPostMember(likeDTO.getPost_id(), likeDTO.getClaimer_id());

            if (result.isPresent()) {
                like = result.get();
                likeRepository.deleteById(result.get().getLike_id());
                like.setLike_id((long)-1); //Id가 -1인 Like가 반환되면 삭제된 것

                return like;
            } else {
                like = likeRepository.add(likeDTO);
                return like; // 새로 추가된 좋아요 반환
            }
        } catch (Exception e) {
            // TODO 예외 처리 (로깅, 사용자 정의 예외 발생 등)
            return null; // 에러가 발생한 경우 null 반환
        }
    }

    public List<Like> listLikesOfPost(Long postId) {
        return likeRepository.findPostLike(postId);
    }
}
