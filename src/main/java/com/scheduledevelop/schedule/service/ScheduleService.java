package com.scheduledevelop.schedule.service;

import com.scheduledevelop.schedule.dto.ScheduleCreateRequestDto;
import com.scheduledevelop.schedule.dto.ScheduleResponseDto;
import com.scheduledevelop.schedule.dto.ScheduleUpdateRequest;
import com.scheduledevelop.schedule.entity.Schedule;
import com.scheduledevelop.schedule.repository.ScheduleRepository;
import com.scheduledevelop.user.entity.User;
import com.scheduledevelop.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    @Transactional
    public ScheduleResponseDto create(Long loginUserId, ScheduleCreateRequestDto request) {
        User user = userRepository.findById(loginUserId).orElseThrow(
                () -> new IllegalStateException("해당 id에 대한 유저가 없습니다. id = " + loginUserId));

        Schedule schedule = new Schedule(
                user,
                request.getTitle(),
                request.getContent()
        );

        Schedule saved = scheduleRepository.save(schedule);

        return new ScheduleResponseDto(saved);
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponseDto> getAll() {
        return scheduleRepository.findAll().stream()
                .map(ScheduleResponseDto::new)
                .toList();
    }


    @Transactional(readOnly = true)
    public ScheduleResponseDto getOne(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalStateException("해당 id에 대한 일정이 없습니다. id = " + scheduleId));

        return new ScheduleResponseDto(schedule);
    }

    @Transactional
    public ScheduleResponseDto update(Long scheduleId, Long loginUserId, ScheduleUpdateRequest request) {

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalStateException("해당 id에 대한 일정이 없습니다. id = " + scheduleId));

        if (schedule.getUser().getId().equals(loginUserId)) {
            schedule.updateSchedule(request.getTitle(), request.getContent());
        } else {
            throw  new IllegalStateException("본인이 작성한 일정만 수정할 수 있습니다.");
        }

        return new ScheduleResponseDto(schedule);

    }


    @Transactional
    public void delete(Long scheduleId, Long loginUserId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 대한 일정이 없습니다. id = " + scheduleId));

        if (schedule.getUser().getId().equals(loginUserId)) {
            scheduleRepository.delete(schedule);
        } else {
            throw  new IllegalStateException("본인이 작성한 일정만 수정할 수 있습니다.");
        }
    }

}
