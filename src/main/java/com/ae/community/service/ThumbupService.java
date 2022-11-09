package com.ae.community.service;

import com.ae.community.domain.Thumbup;
import com.ae.community.repository.ThumbupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class ThumbupService {
    private final ThumbupRepository thumbupRepository;

    @Transactional
    public Long createThumbup(Thumbup thumbup) {
        thumbupRepository.save(thumbup);
        return thumbup.getIdx();
    }

    @Transactional
    public void deleteThumbup(Long userIdx, Long postIdx) {
        thumbupRepository.deleteByUserIdxAndPostIdx(userIdx, postIdx);
    }

    public Optional<Thumbup> findByUserIdxAndPostIdx(Long userIdx, Long postIdx) {
        return thumbupRepository.findByUserIdxAndPostIdx(userIdx, postIdx);
    }

    public Long getThumbupCount(Long postIdx) {
        return thumbupRepository.countByPostIdx(postIdx);
    }

    public Long isThumbedUp(Long userIdx) {
        return thumbupRepository.countByUserIdx(userIdx);
    }

    public List<Thumbup> findAllByPostIdx(Long postIdx) {
        return thumbupRepository.findAllByPostIdx(postIdx);

    }
}
