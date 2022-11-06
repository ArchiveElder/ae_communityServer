package com.ae.community.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "thumbup")
@Getter
@Setter
@NoArgsConstructor
public class Thumbup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;

    @Column(name = "post_idx")
    private int postIdx;

    @Column(name = "user_idx")
    private int userIdx;

    public static Thumbup createThumbup(int userIdx, int postIdx) {
        Thumbup thumbup = new Thumbup();
        thumbup.setUserIdx(userIdx);
        thumbup.setPostIdx(postIdx);
        return thumbup;
    }
}
