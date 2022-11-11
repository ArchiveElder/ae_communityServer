package com.ae.community.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "posting")
@Getter
@Setter
@NoArgsConstructor
public class Posting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private Long userIdx;

    private String content;
    private String title;
    private Timestamp createdAt;
    private String boardName;



}
