package com.khuthon.voidteam.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.khuthon.voidteam.domain.*;
import com.khuthon.voidteam.dto.*;
import com.khuthon.voidteam.repository.MemberRepository;
import com.khuthon.voidteam.repository.RecruitCommentRepository;
import com.khuthon.voidteam.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecruitCommentService {

    private final RecruitRepository recruitRepository;
    private final RecruitCommentRepository recruitCommentRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public RecruitComment create(Long recruitId, RecruitCommentRequestDto.CreateRecruitCommentDto request, Principal principal) throws Exception {

        Member member = memberRepository.findByEmail(principal.getName())
                .orElseThrow(()-> new NotFoundException("유저를 찾을 수 없습니다."));
        Recruit recruit = recruitRepository.findById(recruitId)
                .orElseThrow(() -> new NotFoundException("모집글을 찾을 수 없습니다"));

        RecruitComment recruitComment = RecruitComment.builder()
                .content(request.getContent())
                .recruit(recruit)
                .member(member)
                .build();
        return recruitCommentRepository.save(recruitComment);
    }

    @Transactional
    public RecruitComment update(Long recruitId, Long commentId, RecruitCommentRequestDto.CreateRecruitCommentDto request){
        Recruit recruit = recruitRepository.findById(recruitId)
                .orElseThrow(()-> new NotFoundException("모집글을 찾을 수 없습니다."));
        RecruitComment recruitComment = recruitCommentRepository.findByIdAndRecruit(commentId, recruit);
        recruitComment.update(request.getContent());
        return recruitComment;
    }

    @Transactional
    public void delete(Long recruitId, Long commentId){
        Recruit recruit = recruitRepository.findById(recruitId)
                .orElseThrow(() -> new NotFoundException("모집글을 찾을 수 없습니다"));
        RecruitComment recruitComment = recruitCommentRepository.findByIdAndRecruit(commentId, recruit);
        recruitCommentRepository.delete(recruitComment);
    }

    public List<RecruitCommentResponseDto.RecruitCommentDto> getCommentList(Long recruitId, Principal principal) {
        Recruit recruit = recruitRepository.findById(recruitId)
                .orElseThrow(() -> new NotFoundException("모집글이 존재하지 않습니다."));
        List<RecruitComment> recruitCommentList = recruitCommentRepository.findAllByRecruit(recruit);
        List<RecruitCommentResponseDto.RecruitCommentDto> resultList = new ArrayList<>();
        Member nowMember = memberRepository.findByEmail(principal.getName())
                .orElseThrow(()-> new NotFoundException("사용자가 존재하지 않습니다."));
        for (RecruitComment recruitComment : recruitCommentList) {
            Boolean isMyPost = recruitRepository.existsByMember(nowMember);
            RecruitCommentResponseDto.RecruitCommentDto result = RecruitCommentResponseDto.RecruitCommentDto.builder()
                    .commentId(recruitComment.getId())
                    .isMyPost(isMyPost)
                    .content(recruitComment.getContent())
                    .member(MemberResponseDto.MemberRecruitDto.builder().memberId(recruitComment.getMember().getId())
                            .profileImgUrl(recruitComment.getMember().getProfileImgURL()).nickname(recruitComment.getMember().getNickname())
                            .grade(recruitComment.getMember().getGrade()).temperature(recruitComment.getMember().getTemperature())
                            .university(recruitComment.getMember().getUniversity()).build())
                    .createdDate(recruitComment.getCreateAt())
                    .updatedDate(recruitComment.getModifiedAt())
                    .build();
            resultList.add(result);
        }
        return resultList;
    }



}
