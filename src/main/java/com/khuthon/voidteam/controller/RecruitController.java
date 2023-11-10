package com.khuthon.voidteam.controller;

import com.khuthon.voidteam.domain.Recruit;
import com.khuthon.voidteam.dto.RecruitRequestDto;
import com.khuthon.voidteam.dto.RecruitResponseDto;
import com.khuthon.voidteam.service.RecruitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/recruit")
@RequiredArgsConstructor
public class RecruitController {

    private final RecruitService recruitService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value ="")
    public ResponseEntity<RecruitResponseDto.CreateRecruitDto> createRecruit(@RequestPart(value = "data") RecruitRequestDto.CreateRecruitDto request, Principal principal) throws Exception {

        Recruit recruit = recruitService.create(request, principal);
        return ResponseEntity.ok(RecruitResponseDto.CreateRecruitDto.builder().recruitId(recruit.getId()).build());
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{recruitId}")
    public ResponseEntity<RecruitResponseDto.CreateRecruitDto> updateRecruit(@PathVariable(name = "recruitId")Long recruitId, @RequestBody RecruitRequestDto.CreateRecruitDto data){
        Recruit recruit = recruitService.update(recruitId, data);
        return ResponseEntity.ok(RecruitResponseDto.CreateRecruitDto.builder().recruitId(recruit.getId()).build());
    }

    // 개별 조회
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{recruitId}")
    public ResponseEntity<RecruitResponseDto.RecruitDto> getRecruit(@PathVariable(name = "recruitId") Long recruitId, Principal principal){
        return ResponseEntity.ok(recruitService.findRecruit(recruitId, principal));
    }

    // 리스트 조회
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public ResponseEntity<List<RecruitResponseDto.RecruitDto>> getRecruitList(Principal principal, @RequestParam(name = "category", required = false) String category,
                                                                              @RequestParam(name = "sort", defaultValue = "createdAt") String sort){
        return ResponseEntity.ok(recruitService.getRecruitList(principal, category, sort));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{recruitId}")
    public ResponseEntity<Void> deleteRecruit(@PathVariable(name = "recruitId")Long recruitId){
        recruitService.delete(recruitId);
        return ResponseEntity.ok().build();
    }


}
