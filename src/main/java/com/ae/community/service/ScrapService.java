package com.ae.community.service;

import com.ae.community.domain.Scrap;
import com.ae.community.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class ScrapService {
    private final ScrapRepository scrapRepository;

    @Transactional
    public Long createScrap(Scrap scrap) {
        scrapRepository.save(scrap);
        return scrap.getIdx();
    }

    @Transactional
    public void deleteScrap(Long userIdx, Long postIdx) {
        scrapRepository.deleteByUserIdxAndPostIdx(userIdx, postIdx);
    }

    public Optional<Scrap> findByUserIdxAndPostIdx(Long userIdx, Long postIdx) {
        return scrapRepository.findByUserIdxAndPostIdx(userIdx, postIdx);
    }

    public Long isScraped(Long userIdx) {
        return scrapRepository.countByUserIdx(userIdx);
    }
}
