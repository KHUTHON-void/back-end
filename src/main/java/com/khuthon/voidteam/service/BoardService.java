package com.khuthon.voidteam.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.khuthon.voidteam.domain.*;
import com.khuthon.voidteam.dto.BoardRequestDto;
import com.khuthon.voidteam.dto.BoardResponseDto;
import com.khuthon.voidteam.dto.MemberResponseDto;
import com.khuthon.voidteam.repository.MemberRepository;
import com.khuthon.voidteam.repository.BoardFileRepository;
import com.khuthon.voidteam.repository.BoardLikeRepository;
import com.khuthon.voidteam.repository.BoardRepository;
import com.khuthon.voidteam.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final MemberRepository memberRepository;
    private final S3Util s3Util;

    @Transactional
    public Board create(List<MultipartFile> mediaList, BoardRequestDto.CreateBoardDto request, Principal principal) throws Exception {

        Member member = memberRepository.findByEmail(principal.getName()).orElseThrow(()-> new NotFoundException("유저를 찾을 수 없습니다."));
        Board board = Board.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .category(request.getCategory())
                .member(member)
                .build();
        boardRepository.save(board);
        for(MultipartFile media: mediaList){
            BoardFile boardFile = BoardFile.builder()
                    .url(s3Util.uploadPostObjectToS3(media, board.getId()))
                    .board(board)
                    .build();
            boardFileRepository.save(boardFile);
        }
        return board;
    }


    @Transactional
    public Board update(Long boardId, BoardRequestDto.CreateBoardDto request) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException("Diary를 찾을 수 없습니다."));
        board.update(request.getTitle(), request.getContent());
        return board;
    }

    @Transactional
    public void delete(Long boardId){
        boardRepository.deleteById(boardId);
    }

    @Transactional
    public BoardResponseDto.BoardDto findBoard(Long boardId, Principal principal){
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NotFoundException("Board를 찾을 수 없습니다."));
        Member nowMember = memberRepository.findByEmail(principal.getName()).orElseThrow(()-> new NotFoundException("유저를 찾을 수 없습니다."));
        Member boardMember = board.getMember();
        Boolean isLiked = boardLikeRepository.existsBoardLikeByMemberAndBoard(nowMember, board);
        Boolean isMyPost = boardRepository.existsByMember(nowMember);
        BoardResponseDto.BoardDto result = BoardResponseDto.BoardDto.builder()
                .boardId(board.getId())
                .isLiked(isLiked)
                .member(MemberResponseDto.MemberDto.builder().memberId(boardMember.getId()).profileImgUrl(boardMember.getProfileImgURL()).nickname(boardMember.getNickname()).build())
                .title(board.getTitle())
                .category(board.getCategory())
                .content(board.getContent())
                .createdDate(board.getCreateAt())
                .modifiedDate(board.getModifiedAt())
                .viewCount(board.getViewCount())
                .isMyPost(isMyPost)
                .likeCount(board.getLikeCount())
                .commentCount(board.getCommentCount())
                .build();
        List<BoardFile> boardFileList = boardFileRepository.findAllByBoard(board);
        if(boardFileList == null){
            board.increaseHit();
            return result;
        }
        List<String> list = new ArrayList<>();
        for(BoardFile file: boardFileList){
            String url = file.getUrl();
            list.add(url);
        }
        String listString = String.join(",", list);
        result.setMediaList(listString);
        board.increaseHit();
        return result;
    }

    public ResponseEntity<?> getBoards(Principal principal, String category, String sort) {
        Long currentUserId = null;
        Member member = null;
        List<Board> boards = category == null ? boardRepository.findAll(getSortType(sort)) : boardRepository.findAllByCategory(getInterestType(category), getSortType(sort));
        if (principal != null) {
            member = memberRepository.findByEmail(principal.getName()).orElseThrow();
            currentUserId = member.getId();
        }
        final Member finalMember = member;

        final Long finalCurrentUserId = currentUserId;
        List<BoardResponseDto.BoardDto> responses = boards.stream().map(board -> {
            return BoardResponseDto.BoardDto.builder()
                    .boardId(board.getId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .category(board.getCategory())
                    .member(MemberResponseDto.MemberDto.builder()
                            .memberId(board.getMember().getId())
                            .nickname(board.getMember().getNickname())
                            .profileImgUrl(board.getMember().getProfileImgURL())
                            .build())
                    .commentCount(board.getCommentCount())
                    .isMyPost(Objects.equals(board.getMember().getId(), finalCurrentUserId))
                    .viewCount(board.getViewCount())
                    .isLiked(boardLikeRepository.existsBoardLikeByMemberAndBoard(finalMember, board))
                    .createdDate(board.getCreateAt())
                    .modifiedDate(board.getModifiedAt())
                    .likeCount(board.getLikeCount())
                    .build();
        }).toList();
        return new ResponseEntity<List<BoardResponseDto.BoardDto>>(responses, HttpStatus.OK);
    }

    private Sort getSortType(String sort){
        if(SortType.VIEW.getSortType().equals(sort)){
            return Sort.by(Sort.Order.desc("view"));
        }
        else if(SortType.LIKE.getSortType().equals(sort)){
            return Sort.by(Sort.Order.desc("likeCount"));
        }
        else{
            return Sort.by(Sort.Order.desc("createdAt"));
        }
    }
    private Category getInterestType(String requestCategoryType) {
        if (Category.KOREAN.toString().equalsIgnoreCase(requestCategoryType)) {
            return Category.KOREAN;
        } else if (Category.MATH.toString().equalsIgnoreCase(requestCategoryType)) {
            return Category.MATH;
        } else if (Category.ENGLISH.toString().equalsIgnoreCase(requestCategoryType)) {
            return Category.ENGINEERING;
        } else if (Category.LANGUAGE.toString().equalsIgnoreCase(requestCategoryType)) {
            return Category.LANGUAGE;
        } else if (Category.ENGINEERING.toString().equalsIgnoreCase(requestCategoryType)) {
            return Category.ENGINEERING;
        } else if (Category.EXAM.toString().equalsIgnoreCase(requestCategoryType)) {
            return Category.EXAM;
        } else if (Category.IT.toString().equalsIgnoreCase(requestCategoryType)) {
            return Category.IT;
        } else if (Category.JOBSEARCHING.toString().equalsIgnoreCase(requestCategoryType)) {
            return Category.JOBSEARCHING;
        } else {
            return Category.ETC;
        }
    }
}
