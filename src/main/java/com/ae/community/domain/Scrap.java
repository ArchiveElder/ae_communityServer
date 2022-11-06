package com.ae.community.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "scrap")
@Getter
@Setter
@NoArgsConstructor
public class Scrap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    private Long postIdx;
    private Long userIdx;
}
