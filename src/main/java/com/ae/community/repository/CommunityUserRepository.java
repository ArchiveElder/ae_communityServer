package com.ae.community.repository;

import com.ae.community.domain.CommunityUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface CommunityUserRepository extends JpaRepository<CommunityUser, Long> {

    Optional<CommunityUser> findByUserIdx(Long userIdx);








}
