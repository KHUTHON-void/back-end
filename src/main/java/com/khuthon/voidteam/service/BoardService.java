package com.khuthon.voidteam.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.khuthon.voidteam.domain.Board;
import com.khuthon.voidteam.domain.BoardFile;
import com.khuthon.voidteam.domain.Member;
import com.khuthon.voidteam.dto.BoardRequestDto;
import com.khuthon.voidteam.dto.BoardResponseDto;
import com.khuthon.voidteam.dto.MemberResponseDto;
import com.khuthon.voidteam.repository.MemberRepository;
import com.khuthon.voidteam.util.JacksonUtil;
import com.khuthon.voidteam.repository.BoardFileRepository;
import com.khuthon.voidteam.repository.BoardLikeRepository;
import com.khuthon.voidteam.repository.BoardRepository;
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
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final MemberRepository memberRepository;
    private final S3Util s3Util;

    @Transactional
    public Board create(List<MultipartFile> mediaList, String boardCreateDto, Principal principal) throws Exception {

        JacksonUtil jacksonUtil = new JacksonUtil();
        BoardRequestDto.CreateBoardDto request = (BoardRequestDto.CreateBoardDto) jacksonUtil.strToObj(boardCreateDto, BoardRequestDto.CreateBoardDto.class);
        Member member = memberRepository.findByEmail(principal.getName()).orElseThrow(()-> new NotFoundException("유저를 찾을 수 없습니다."));
        Board board = Board.builder()
                .title(request.getTitle())
                .content(request.getContent())
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
                .content(board.getContent())
                .createdDate(board.getCreatedDate())
                .modifiedDate(board.getUpdatedDate())
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
        result.setMediaList(list);
        board.increaseHit();
        return result;
    }
}
