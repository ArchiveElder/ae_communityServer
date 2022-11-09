package com.ae.community.repository;

import com.ae.community.domain.Comment;
import com.ae.community.dto.response.CommentsListDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    void deleteByIdx(Long idx);

    Optional<Comment> findByUserIdxAndIdx(Long userIdx, Long idx);

    Long countByPostIdx(Long postIdx);

    List<Comment> findByPostIdx(Long postIdx);
}
