package com.scheduledevelop.schedule.service;

import com.scheduledevelop.common.exception.ApiException;
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
        User user = userRepository.findById(loginUserId).orElseThrow(() -> ApiException.notFound("존재하지 않는 유저입니다."));

        Schedule schedule = new Schedule(user, request.getTitle(), request.getContent());

        Schedule saved = scheduleRepository.save(schedule);

        return new ScheduleResponseDto(saved);
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponseDto> getAll() {
        return scheduleRepository.findAll().stream().map(ScheduleResponseDto::new).toList();
    }


    @Transactional(readOnly = true)
    public ScheduleResponseDto getOne(Long scheduleId) {
        Schedule schedule = findSchedule(scheduleId);

        return new ScheduleResponseDto(schedule);
    }

    @Transactional
    public ScheduleResponseDto update(Long scheduleId, Long loginUserId, ScheduleUpdateRequest request) {

        Schedule schedule = findSchedule(scheduleId);

        validateScheduleOwner(schedule, loginUserId);

        schedule.updateSchedule(request.getTitle(), request.getContent());

        return new ScheduleResponseDto(schedule);

    }


    @Transactional
    public void delete(Long scheduleId, Long loginUserId) {
        Schedule schedule = findSchedule(scheduleId);

        validateScheduleOwner(schedule, loginUserId);

        scheduleRepository.delete(schedule);
    }

    // 일정 ID로 일정 조회
    // 존재하지 않으면 404 예외 발생
    private Schedule findSchedule(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> ApiException.notFound("존재하지 않는 일정입니다."));
    }

    // 로그인한 유저가 해당 일정의 작성자인지 확인
    // 작성자가 아니면 수정/삭제 권한 없음
    private void validateScheduleOwner(Schedule schedule, Long loginUserId) {
        if (!schedule.getUser().getId().equals(loginUserId)) {
            throw ApiException.forbidden("본인이 작성한 일정만 수정 또는 삭제할 수 있습니다.");
        }
    }


}
