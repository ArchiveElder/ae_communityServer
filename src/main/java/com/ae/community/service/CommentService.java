package com.ae.community.service;

import com.ae.community.domain.Comment;
import com.ae.community.dto.response.CommentsListDto;
import com.ae.community.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    @Transactional
    public Long createComment(Comment comment) {
        commentRepository.save(comment);
        return comment.getIdx();
    }

    @Transactional
    public void deleteComment(Long commentIdx) {
        commentRepository.deleteByIdx(commentIdx);
    }

    public Optional<Comment> findByUserIdxAndIdx(Long userIdx, Long idx) {
        return commentRepository.findByUserIdxAndIdx(userIdx, idx);
    }

    public Long getCommentCnt(Long postIdx) {
        return commentRepository.countByPostIdx(postIdx);
    }

    public List<Comment> getCommentList(Long postIdx) {
        return commentRepository.findByPostIdx(postIdx);
    }
}
