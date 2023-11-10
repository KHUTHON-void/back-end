package com.khuthon.voidteam.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.khuthon.voidteam.domain.*;
import com.khuthon.voidteam.dto.CommentRequestDto;
import com.khuthon.voidteam.dto.CommentResponseDto;
import com.khuthon.voidteam.dto.MemberResponseDto;
import com.khuthon.voidteam.repository.*;
import com.khuthon.voidteam.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final CommentFileRepository commentFileRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final MemberRepository memberRepository;
    private final S3Util s3Util;

    @Transactional
    public Comment create(List<MultipartFile> mediaList, Long boardId, CommentRequestDto.CreateCommentDto request, Principal principal) throws Exception {

        Member member = memberRepository.findByEmail(principal.getName())
                .orElseThrow(()-> new NotFoundException("유저를 찾을 수 없습니다."));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException("Board를 찾을 수 없습니다"));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .board(board)
                .member(member)
                .build();
        commentRepository.save(comment);
        for(MultipartFile media: mediaList){
            CommentFile commentFile = CommentFile.builder()
                    .url(s3Util.uploadPostObjectToS3(media, comment.getId()))
                    .comment(comment)
                    .build();
            commentFileRepository.save(commentFile);
        }
        return comment;
    }

    @Transactional
    public Comment update(Long boardId, Long commentId, CommentRequestDto.CreateCommentDto request){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(()-> new NotFoundException("Board를 찾을 수 없습니다."));
        Comment comment = commentRepository.findByIdAndBoard(commentId, board);
        comment.update(request.getContent());
        return comment;
    }

    @Transactional
    public void delete(Long boardId, Long commentId){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException("Board를 찾을 수 없습니다"));
        Comment comment = commentRepository.findByIdAndBoard(commentId, board);
        commentRepository.delete(comment);
    }

    public List<CommentResponseDto.CommentDto> getCommentList(Long boardId, Principal principal) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException("Board가 존재하지 않습니다."));
        List<Comment> CommentList = commentRepository.findAllByBoard(board);
        List<CommentResponseDto.CommentDto> resultList = new ArrayList<>();
        Member nowMember = memberRepository.findByEmail(principal.getName())
                .orElseThrow(()-> new NotFoundException("사용자가 존재하지 않습니다."));
        for (Comment comment : CommentList) {
            Boolean isLiked = commentLikeRepository.existsCommentLikeByMemberAndBoardAndComment(nowMember, board, comment);
            Boolean isMyPost = boardRepository.existsByMember(nowMember);
            CommentResponseDto.CommentDto result = CommentResponseDto.CommentDto.builder()
                    .commentId(comment.getId())
                    .isLiked(isLiked)
                    .isMyPost(isMyPost)
                    .likecount(comment.getLikeCount())
                    .content(comment.getContent())
                    .member(MemberResponseDto.MemberDto.builder().memberId(comment.getMember().getId()).profileImgUrl(comment.getMember().getProfileImgURL()).nickname(comment.getMember().getNickname()).build())
                    .createdDate(comment.getCreatedDate())
                    .updatedDate(comment.getUpdatedDate())
                    .build();

            List<CommentFile> commentFileList = commentFileRepository.findAllByComment(comment);
            if(commentFileList == null){
                resultList.add(result);
            }else{
                List<String> list = new ArrayList<>();
                for(CommentFile file: commentFileList){
                    String url = file.getUrl();
                    list.add(url);
                }
                String listString = String.join(",", list);
                result.setMediaList(listString);
                resultList.add(result);
            }
        }
        return resultList;
    }

    @Transactional
    public void updateGrade(String username){
        Member member = memberRepository.findByEmail(username).get();
        int size = member.getComment().size();
        if(size == 1){
            member.setGrade(Grade.BRONZE);
        }
        else if(size == 2){
            member.setGrade(Grade.SILVER);
        } else if (size > 3) {
            member.setGrade(Grade.GOLD);
        }
    }

}
