package com.ae.community.repository;

import com.ae.community.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    void deleteByIdx(Long idx);

    Optional<Comment> findByUserIdxAndIdx(Long userIdx, Long idx);
}
