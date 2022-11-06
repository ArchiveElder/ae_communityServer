package com.ae.community.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "community_user")
@Getter
@Setter
@NoArgsConstructor
public class CommunityUser {
    @Id
    private Long idx;
    private String nickname;
    private Long userIdx;
}
