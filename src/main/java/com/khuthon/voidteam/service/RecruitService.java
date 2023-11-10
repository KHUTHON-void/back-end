package com.khuthon.voidteam.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.khuthon.voidteam.domain.*;
import com.khuthon.voidteam.dto.*;
import com.khuthon.voidteam.repository.MemberRepository;
import com.khuthon.voidteam.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecruitService {

    private final MemberRepository memberRepository;
    private final RecruitRepository recruitRepository;

    @Transactional
    public Recruit create(RecruitRequestDto.CreateRecruitDto request, Principal principal) throws Exception {

        Member member = memberRepository.findByEmail(principal.getName())
                .orElseThrow(()-> new NotFoundException("유저를 찾을 수 없습니다."));
        Recruit recruit = Recruit.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .category(request.getCategory())
                .member(member)
                .build();
        return recruitRepository.save(recruit);
    }

    @Transactional
    public Recruit update(Long recruitId, RecruitRequestDto.CreateRecruitDto request) {
        Recruit recruit = recruitRepository.findById(recruitId)
                .orElseThrow(() -> new NotFoundException("모집글을 찾을 수 없습니다."));
        recruit.update(request.getTitle(), request.getContent());
        return recruit;
    }

    @Transactional
    public void delete(Long recruitId){
        recruitRepository.deleteById(recruitId);
    }

    // 개별 조회
    @Transactional
    public RecruitResponseDto.RecruitDto findRecruit(Long recruitId, Principal principal){
        Recruit recruit = recruitRepository.findById(recruitId).orElseThrow(() -> new NotFoundException("Recruit를 찾을 수 없습니다."));
        Member nowMember = memberRepository.findByEmail(principal.getName()).orElseThrow(()-> new NotFoundException("유저를 찾을 수 없습니다."));
        Member recruitMember = recruit.getMember();
        Boolean isMyPost = recruitRepository.existsByMember(nowMember);
        RecruitResponseDto.RecruitDto result = RecruitResponseDto.RecruitDto.builder()
                .recruitId(recruit.getId())
                .member(MemberResponseDto.MemberRecruitDto.builder().memberId(recruitMember.getId()).profileImgUrl(recruitMember.getProfileImgURL())
                        .nickname(recruitMember.getNickname()).university(recruitMember.getUniversity()).temperature(recruitMember.getTemperature())
                        .grade(recruitMember.getGrade()).build())
                .title(recruit.getTitle())
                .category(recruit.getCategory())
                .content(recruit.getContent())
                .createdDate(recruit.getCreateAt())
                .modifiedDate(recruit.getModifiedAt())
                .viewCount(recruit.getViewCount())
                .isMyPost(isMyPost)
                .commentCount(recruit.getCommentCount())
                .build();
        recruit.increaseHit();
        return result;
    }

    public List<RecruitResponseDto.RecruitDto> getRecruitList(Principal principal, String category, String sort){
        Member nowMember = memberRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NotFoundException("유저를 찾을 수 없습니다."));
        List<Recruit> recruitList = new ArrayList<>();
        if(category==null && sort.equals("createdAt")){
            recruitList = recruitRepository.findAll(Sort.by(Sort.Direction.DESC, "createAt"));
        } else if (category != null && sort.equals("createdAt")){
            recruitList = recruitRepository.findAllByCategoryOrderByCreateAtDesc(getInterestType(category));
        } else if (category != null && sort.equals("viewCount")){
            recruitList = recruitRepository.findAllByCategoryOrderByViewCountDesc(getInterestType(category));

        }
        List<RecruitResponseDto.RecruitDto> resultList = new ArrayList<>();
        for(Recruit recruit: recruitList){
            Boolean isMyPost = recruitRepository.existsByMember(nowMember);
            RecruitResponseDto.RecruitDto result = RecruitResponseDto.RecruitDto.builder()
                    .recruitId(recruit.getId())
                    .title(recruit.getTitle())
                    .content(recruit.getContent())
                    .category(recruit.getCategory())
                    .viewCount(recruit.getViewCount())
                    .commentCount(recruit.getCommentCount())
                    .createdDate(recruit.getCreateAt())
                    .modifiedDate(recruit.getModifiedAt())
                    .isMyPost(isMyPost)
                    .member(MemberResponseDto.MemberRecruitDto.builder().memberId(recruit.getMember().getId()).nickname(recruit.getMember().getNickname())
                            .profileImgUrl(recruit.getMember().getProfileImgURL()).university(recruit.getMember().getUniversity())
                            .grade(recruit.getMember().getGrade()).temperature(recruit.getMember().getTemperature()).build())
                    .build();
            resultList.add(result);
        }
        return resultList;
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
