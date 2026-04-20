package com.scheduledevelop.schedule.controller;

import com.scheduledevelop.common.SessionKey;
import com.scheduledevelop.common.exception.ApiException;
import com.scheduledevelop.schedule.dto.ScheduleCreateRequestDto;
import com.scheduledevelop.schedule.dto.ScheduleResponseDto;
import com.scheduledevelop.schedule.dto.ScheduleUpdateRequest;
import com.scheduledevelop.schedule.service.ScheduleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(
            @Valid @RequestBody ScheduleCreateRequestDto request,
            HttpServletRequest httpServletRequest) {

        Long loginUserId = getLoginUserId(httpServletRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.create(loginUserId, request));
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> getAllSchedule() {
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.getAll());
    }

    @GetMapping("{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> getOneSchedule(@PathVariable Long scheduleId) {
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.getOne(scheduleId));
    }

    @PutMapping("{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(
            @PathVariable Long scheduleId,
            @Valid @RequestBody ScheduleUpdateRequest request,
            HttpServletRequest httpServletRequest) {

        Long loginUserId = getLoginUserId(httpServletRequest);
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.update(scheduleId, loginUserId, request));
    }

    @DeleteMapping("{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long scheduleId, HttpServletRequest httpServletRequest) {

        Long loginUserId = getLoginUserId(httpServletRequest);
        scheduleService.delete(scheduleId, loginUserId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 로그인 유저 확인 메서드 (생성, 수정, 삭제해서 필요)
    // interceptor로 구현할 예정
    private Long getLoginUserId(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession(false);

        if (session == null || session.getAttribute(SessionKey.LoginUserId) == null) {
            throw ApiException.unauthorized("로그인이 필요한 요청입니다.");
        }

        return (Long) session.getAttribute(SessionKey.LoginUserId);
    }

}
