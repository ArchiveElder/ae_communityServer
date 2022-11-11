package com.ae.community.service;



import com.ae.community.domain.*;

import com.ae.community.dto.request.PostingDto;
import com.ae.community.dto.response.*;
import com.ae.community.repository.PostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    public Posting create(Long userIdx, String content, String title, String boardName) {
        Posting post = new Posting();
        post.setUserIdx(userIdx);
        post.setContent(content);
        post.setTitle(title);
        post.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        post.setBoardName(boardName);

        return post;
    }
    public Optional<Posting> findById(Long postIdx) {return  postingRepository.findById(postIdx); }


    public void deletePost(Long postIdx) {
        imagesService.deleteByPostIdx(postIdx);
        postingRepository.deleteByIdx(postIdx);
    }

    public Posting update(Posting post, PostingDto updatePostDto) {
        Long postIdx = post.getIdx();
        post.setContent(updatePostDto.getTitle());
        post.setTitle(updatePostDto.getTitle());
        post.setBoardName(updatePostDto.getBoardName());

        postingRepository.save(post);
        return post;

    }

    public CheckMyPostsDto checkMyPosts(Long userIdx) {
        CheckMyPostsDto checkMyPosts = new CheckMyPostsDto();
        Long postsCount = getPostsCount(userIdx);
        checkMyPosts.setPostCount(postsCount);
        if(postsCount > 0) {
            List<Posting> postings = postingRepository.findAllByUserIdx(userIdx);
            List<PostsListDto> postsLists = postings.stream()
                    .map(m-> {
                        Optional<CommunityUser> communityUser = userService.findByUserIdx(m.getUserIdx());
                        List<Thumbup> thumbups = thumbupService.findAllByPostIdx(m.getIdx());
                        List<Comment> comments = commentService.findAllByPostIdx(m.getIdx());
                        return new PostsListDto(m.getIdx(), m.getUserIdx(), (int) (Math.random() *10), communityUser.get().getNickname(), m.getTitle(), m.getContent(), new SimpleDateFormat("yyyy.MM.dd HH:mm").format(m.getCreatedAt()), thumbups.size(), comments.size());
                    })
                    .collect(Collectors.toList());
            checkMyPosts.setPostsLists(postsLists);

        } else checkMyPosts.setPostsLists(null);

        return checkMyPosts;


    }

    public CheckMyScrapsDto checkMyScraps(Long userIdx) {
        CheckMyScrapsDto checkMyScraps = new CheckMyScrapsDto();

        List<Posting> postings = postingRepository.findAllWithScrap(userIdx);
        List<PostsListDto> postsLists = postings.stream()
                .map(m-> {
                    Optional<CommunityUser> communityUser = userService.findByUserIdx(m.getUserIdx());
                    List<Thumbup> thumbups = thumbupService.findAllByPostIdx(m.getIdx());
                    List<Comment> comments = commentService.findAllByPostIdx(m.getIdx());
                    if(communityUser.isPresent()) {

                        return new PostsListDto(m.getIdx(), m.getUserIdx(), (int) (Math.random() *10), communityUser.get().getNickname(), m.getTitle(), m.getContent(), new SimpleDateFormat("yyyy.MM.dd HH:mm").format(m.getCreatedAt()), thumbups.size(), comments.size());
                    }

                    return new PostsListDto(m.getIdx(), m.getUserIdx(), (int) (Math.random() *10), "", m.getTitle(), m.getContent(), new SimpleDateFormat("yyyy.MM.dd HH:mm").format(m.getCreatedAt()), thumbups.size(), comments.size());
                })
                .collect(Collectors.toList());
        checkMyScraps.setPostCount(postsLists.stream().count());
        checkMyScraps.setPostsLists(postsLists);


        return checkMyScraps;
    }


    public Long getPostsCount(Long userIdx) { return postingRepository.countByUserIdx(userIdx);}

    public Long getAllPostCount() {
        return postingRepository.count();
    }
    private Long getGroupPostsCount(String boardName) {
        return postingRepository.countByBoardName(boardName);
    }

    public Page<Posting> getAllPosts(Pageable pageable) {
        //return postingRepository.findAllPostingWithPagination(pageable);
        return postingRepository.findAll(pageable);
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
        postDetailDto.setUserIdx(user.getUserIdx());
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
                Long writerUserIdx = writer.get().getUserIdx();
                commentsListDtos.add(new CommentsListDto(comment.getIdx(), writerUserIdx, writerNickname, (int) (Math.random() * 10)
                        , new SimpleDateFormat("yyyy.MM.dd HH:mm").format(comment.getCreatedAt())
                        ,comment.getContent()));
            }
            postDetailDto.setCommentsLists(commentsListDtos);

        } else postDetailDto.setCommentsLists(null);

        return postDetailDto;

    }
    public List<AllPostsListDto> getAllPostsInBoard(CommunityUser user, Pageable pageable, String boardName) {
        if(boardName.equals("all")) {
            return allPostsList(user, pageable);
        }
        switch (boardName) {
            case "daily":
                boardName = "일상";
                break;
            case "recipe":
                boardName = "레시피 ";
                break;
            case "question":
                boardName = "질문";
                break;
            case "honeytip":
                boardName = "꿀팁";
                break;
            case "notice":
                boardName = "공지";
                break;
            default: boardName="일상"; break;
        }
        return boardPostsList(user,pageable, boardName);
    }

    private List<AllPostsListDto> boardPostsList(CommunityUser user, Pageable pageable, String boardName) {
        List<AllPostsListDto> allPostsList = new ArrayList<>();
        Long groupPostsCnt = getGroupPostsCount(boardName);
        if(groupPostsCnt == 0) return allPostsList;
        Page<Posting> groupPostsList = postingRepository.findByBoardName(boardName, pageable);
        for(Posting post : groupPostsList){
            AllPostsListDto allPostsListDto = new AllPostsListDto();

            allPostsListDto.setPostIdx(post.getIdx());
            allPostsListDto.setBoardName(post.getBoardName());
            allPostsListDto.setTitle(post.getTitle());

            Long writerIdx = post.getUserIdx();
            Optional<CommunityUser> writer = userService.findByUserIdx(writerIdx);
            allPostsListDto.setUserIdx(writerIdx);
            allPostsListDto.setIcon((int) (Math.random()*10));
            allPostsListDto.setNickname(writer.get().getNickname());

            allPostsListDto.setCreatedAt(new SimpleDateFormat("yyyy.MM.dd HH:mm").format(post.getCreatedAt()));

            Long likeCnt = thumbupService.getThumbupCount(post.getIdx());
            Long commentCnt = commentService.getCommentCnt(post.getIdx());
            if(commentCnt >0) allPostsListDto.setHasImg(1);
            else allPostsListDto.setHasImg(0);

            allPostsListDto.setLikeCnt(likeCnt);
            allPostsListDto.setCommentCnt(commentCnt);

            Long isScraped = scrapService.countByUserIdxAndPostIdx(user.getUserIdx(), post.getIdx());
            if(isScraped >0) allPostsListDto.setIsScraped(1);
            else allPostsListDto.setIsScraped(0);
            allPostsList.add(allPostsListDto);
        }

        return allPostsList;
    }



    public List<AllPostsListDto> allPostsList(CommunityUser user, Pageable pageable) {
        List<AllPostsListDto> allPostsList = new ArrayList<>();
        // 게시글이 0개면 이후 로직 없이 return
        Long postsCount = getAllPostCount();
        if (postsCount == 0) {
            return allPostsList;
        } else {

            Page<Posting> postingList = getAllPosts(pageable);
            for (Posting post : postingList) {
                AllPostsListDto allPostsListDto = new AllPostsListDto();

                allPostsListDto.setPostIdx(post.getIdx());
                allPostsListDto.setBoardName(post.getBoardName());
                allPostsListDto.setTitle(post.getTitle());

                Long writerIdx = post.getUserIdx();
                Optional<CommunityUser> writer = userService.findByUserIdx(writerIdx);
                allPostsListDto.setUserIdx(writerIdx);
                allPostsListDto.setIcon((int) (Math.random()*10));
                allPostsListDto.setNickname(writer.get().getNickname());

                allPostsListDto.setCreatedAt(new SimpleDateFormat("yyyy.MM.dd HH:mm").format(post.getCreatedAt()));

                Long likeCnt = thumbupService.getThumbupCount(post.getIdx());
                Long commentCnt = commentService.getCommentCnt(post.getIdx());
                if(commentCnt >0) allPostsListDto.setHasImg(1);
                else allPostsListDto.setHasImg(0);

                allPostsListDto.setLikeCnt(likeCnt);
                allPostsListDto.setCommentCnt(commentCnt);

                Long isScraped = scrapService.countByUserIdxAndPostIdx(user.getUserIdx(), post.getIdx());
                if(isScraped >0) allPostsListDto.setIsScraped(1);
                else allPostsListDto.setIsScraped(0);
                allPostsList.add(allPostsListDto);
            }
        }
        return allPostsList;
    }


}
