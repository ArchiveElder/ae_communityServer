package com.ae.community.repository;

import com.ae.community.domain.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    void deleteByUserIdxAndPostIdx(Long userIdx, Long postIdx);
    Optional<Scrap> findByUserIdxAndPostIdx(Long userIdx, Long postIdx);
}
