package com.ae.community.repository;

import com.ae.community.domain.Posting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PostingRepository extends JpaRepository<Posting, Long>, CrudRepository<Posting, Long> {


    @Transactional
    void deleteByIdx(Long postIdx);

    Long countByUserIdx(Long userIdx);

    Page<Posting> findAllByUserIdx(Long userIdx, Pageable pageable);

    @Query(value = "select p from Posting p where p.idx IN (select sc.postIdx from Scrap sc " +
            "join CommunityUser u on sc.userIdx = u.userIdx where u.userIdx = :userIdx)",
    countQuery = "select COUNT(p) from Posting p where p.idx IN (select sc.postIdx from Scrap sc " +
            "join CommunityUser u on sc.userIdx = u.userIdx where u.userIdx = :userIdx)")
    Page<Posting> findAllWithScrap(@Param("userIdx") Long userIdx, Pageable pageable);

    @Query(value = "select p from Posting p ORDER BY p.idx")
    Page<Posting> findAllPostingWithPagination(Pageable pageable);

    Page<Posting> findByBoardName(String boardName, Pageable pageable);

    Long countByBoardName(String boardName);
}
