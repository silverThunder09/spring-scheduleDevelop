package com.scheduledevelop.schedule.service;

import com.scheduledevelop.schedule.dto.ScheduleCreateRequestDto;
import com.scheduledevelop.schedule.dto.ScheduleResponseDto;
import com.scheduledevelop.schedule.dto.ScheduleUpdateRequest;
import com.scheduledevelop.schedule.entity.Schedule;
import com.scheduledevelop.schedule.repository.ScheduleRepositoty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepositoty scheduleRepositoty;

    @Transactional
    public ScheduleResponseDto create(ScheduleCreateRequestDto request) {
        Schedule schedule = new Schedule(
                request.getUsername(),
                request.getTitle(),
                request.getContent()
        );

        Schedule saved = scheduleRepositoty.save(schedule);

        return new ScheduleResponseDto(saved);

    }

    @Transactional(readOnly = true)
    public List<ScheduleResponseDto> getAll() {
        return scheduleRepositoty.findAll().stream()
                .map(ScheduleResponseDto::new)
                .toList();
    }


    @Transactional(readOnly = true)
    public ScheduleResponseDto getOne(Long scheduleId) {
        Schedule schedule = scheduleRepositoty.findById(scheduleId).orElseThrow(
                () -> new IllegalStateException("해당 id에 대한 일정이 없습니다. id = " + scheduleId));

        return new ScheduleResponseDto(schedule);
    }

    @Transactional
    public ScheduleResponseDto update(Long scheduleId, ScheduleUpdateRequest request) {
        Schedule schedule = scheduleRepositoty.findById(scheduleId).orElseThrow(
                () -> new IllegalStateException("해당 id에 대한 일정이 없습니다. id = " + scheduleId));

        schedule.updateSchedule(request.getTitle(), request.getContent());

        return new ScheduleResponseDto(schedule);

    }

    @Transactional
    public void delete(Long scheduleId) {
        Schedule schedule = scheduleRepositoty.findById(scheduleId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 대한 일정이 없습니다. id = " + scheduleId));

        scheduleRepositoty.delete(schedule);
    }

}
