package com.ae.community.repository;

import com.ae.community.domain.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ImagesRepository extends JpaRepository<Images, Long> {
    @Transactional
    void deleteByPostIdx(Long postIdx);

    List<Images> findByPostIdx(Long postIdx);

    Long countByPostIdx(Long postIdx);
}
