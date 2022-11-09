package com.ae.community.repository;

import com.ae.community.domain.Thumbup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ThumbupRepository extends JpaRepository<Thumbup, Long> {
    void deleteByUserIdxAndPostIdx(Long userIdx, Long postIdx);

    Optional<Thumbup> findByUserIdxAndPostIdx(Long userIdx, Long postIdx);

    List<Thumbup> findAllByPostIdx(Long postIdx);
}
