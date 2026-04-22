package com.scheduledevelop.schedule.controller;

import com.scheduledevelop.common.Util.SessionUtil;
import com.scheduledevelop.schedule.dto.ScheduleCreateRequestDto;
import com.scheduledevelop.schedule.dto.ScheduleResponseDto;
import com.scheduledevelop.schedule.dto.ScheduleUpdateRequest;
import com.scheduledevelop.schedule.service.ScheduleService;
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
            HttpSession session) {

        Long loginUserId = SessionUtil.getLoginUserId(session);
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
            HttpSession session) {

        Long loginUserId = SessionUtil.getLoginUserId(session);
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.update(scheduleId, loginUserId, request));
    }

    @DeleteMapping("{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long scheduleId, HttpSession session) {

        Long loginUserId = SessionUtil.getLoginUserId(session);
        scheduleService.delete(scheduleId, loginUserId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
