package com.khuthon.voidteam.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.khuthon.voidteam.domain.Board;
import com.khuthon.voidteam.domain.Comment;
import com.khuthon.voidteam.domain.CommentFile;
import com.khuthon.voidteam.dto.CommentRequestDto;
import com.khuthon.voidteam.dto.CommentResponseDto;
import com.khuthon.voidteam.util.JacksonUtil;
import com.khuthon.voidteam.repository.BoardRepository;
import com.khuthon.voidteam.repository.CommentFileRepository;
import com.khuthon.voidteam.repository.CommentRepository;
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
    //private final MemberRepository memberRepository;
    private final S3Util s3Util;

    @Transactional
    public Comment create(List<MultipartFile> mediaList, Long boardId, String commentCreateDto, Principal principal) throws Exception {

        JacksonUtil jacksonUtil = new JacksonUtil();
        CommentRequestDto.CreateCommentDto request = (CommentRequestDto.CreateCommentDto) jacksonUtil.strToObj(commentCreateDto, CommentRequestDto.CreateCommentDto.class);
//        Member member = memberRepository.findByEmail(principal.getName())
//                .orElseThrow(()-> new NotFoundException("유저를 찾을 수 없습니다."));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException("Board를 찾을 수 없습니다"));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .board(board)
                //.member(member)
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

    public List<CommentResponseDto.CommentDto> getCommentList(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException("Board가 존재하지 않습니다."));
        List<Comment> CommentList = commentRepository.findAllByBoard(board);
        List<CommentResponseDto.CommentDto> resultList = new ArrayList<>();
        for (Comment comment : CommentList) {
            CommentResponseDto.CommentDto result = CommentResponseDto.CommentDto.builder()
                    .commentId(comment.getId())
                    .content(comment.getContent())
                    //.user()
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
                result.setMediaList(list);
                resultList.add(result);
            }
        }
        return resultList;
    }

}
