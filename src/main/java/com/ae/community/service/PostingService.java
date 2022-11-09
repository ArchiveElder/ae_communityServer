package com.ae.community.service;

import com.ae.community.domain.Comment;
import com.ae.community.domain.CommunityUser;
import com.ae.community.domain.Images;
import com.ae.community.domain.Posting;
import com.ae.community.dto.response.*;
import com.ae.community.repository.PostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostingService {
    private final PostingRepository postingRepository;
    private final ImagesService imagesService;
    private final CommunityUserService userService;
    private final ThumbupService thumbupService;
    private final CommentService commentService;
    private final ScrapService scrapService;
    public Posting save(Posting post) {  return postingRepository.save(post); }

    public Posting create(Long userIdx, String content, String title, String groupName) {
        Posting post = new Posting();
        post.setUserIdx(userIdx);
        post.setContent(content);
        post.setTitle(title);
        post.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        post.setGroupName(groupName);

        return post;
    }
    public Optional<Posting> findById(Long postIdx) {return  postingRepository.findById(postIdx); }


    public void deletePost(Long postIdx) {
        imagesService.deleteByPostIdx(postIdx);
        postingRepository.deleteByIdx(postIdx);
    }

    public Posting update(Posting post, String content, String title) {
        Long postIdx = post.getIdx();
        post.setContent(content);
        post.setTitle(title);
        //post.setCreatedAt(new Timestamp(System.currentTimeMillis())); 수정일자를 보일지 작성일자를 보일지 고민

        postingRepository.save(post);
        return post;

    }

    public Long getPostsCount(Long userIdx) { return postingRepository.countByUserIdx(userIdx);}

    public Long getAllPostCount() {
        return postingRepository.count();
    }

    public List<Posting> getAllPosts() {
        return postingRepository.findAll();
    }

    public PostDetailDto detailPost(Long userIdx, Long postIdx, Posting post, List<Images> imageList){

        PostDetailDto postDetailDto = new PostDetailDto();
        postDetailDto.setPostIdx(post.getIdx());
        postDetailDto.setTitle(post.getTitle());
        postDetailDto.setContent(post.getContent());

        Long writerIdx = findById(postIdx).get().getUserIdx();
        CommunityUser user = userService.findByUserIdx(writerIdx).get();

        String nickname = user.getNickname();
        postDetailDto.setNickname(nickname);
        postDetailDto.setIcon((int) (Math.random() *10));
        postDetailDto.setCreatedAt(new SimpleDateFormat("yyyy.MM.dd HH:mm").format(post.getCreatedAt()));
        postDetailDto.setImagesCount(imageList.size());
        if(imageList.size() != 0) {
            List<ImagesListDto> dtoList = imageList.stream()
                    .map(m -> new ImagesListDto(m.getImgUrl(), m.getImgRank()))
                    .collect(Collectors.toList());
            postDetailDto.setImagesLists(dtoList);

        } else postDetailDto.setImagesLists(null);

        Long thumbupCnt = thumbupService.getThumbupCount(postIdx);
        postDetailDto.setThumbupCount(thumbupCnt);
        Long commentCount = commentService.getCommentCnt(postIdx);

        Long isLiked = thumbupService.isThumbedUp(userIdx);
        Long isScraped = scrapService.isScraped(userIdx);
        postDetailDto.setIsLiked(isLiked.intValue());
        postDetailDto.setIsScraped(isScraped.intValue());

        postDetailDto.setCommentCount(commentCount);
        List<CommentsListDto> commentsListDtos = new ArrayList<>();
        if(commentCount >0) {
            List<Comment> comments = commentService.getCommentList(postIdx);
            for(Comment comment: comments) {
                Optional<CommunityUser> writer = userService.findByUserIdx(comment.getUserIdx());
                String writerNickname = writer.get().getNickname();
                commentsListDtos.add(new CommentsListDto(comment.getIdx(), writerNickname, (int) (Math.random() * 10)
                        , new SimpleDateFormat("yyyy.MM.dd HH:mm").format(comment.getCreatedAt())
                        ,comment.getContent()));
            }
            postDetailDto.setCommentsLists(commentsListDtos);

        } else postDetailDto.setCommentsLists(null);

        return postDetailDto;

    }

}
