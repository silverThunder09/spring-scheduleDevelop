package com.scheduledevelop.schedule.repository;

import com.scheduledevelop.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    void deleteAllByUserId(Long userId);
}
