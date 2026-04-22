package com.scheduledevelop.user.service;

import com.scheduledevelop.comment.repository.CommentRepository;
import com.scheduledevelop.common.config.PasswordEncoder;
import com.scheduledevelop.common.exception.ApiException;
import com.scheduledevelop.schedule.repository.ScheduleRepository;
import com.scheduledevelop.user.dto.UserCreateRequestDto;
import com.scheduledevelop.user.dto.UserResponseDto;
import com.scheduledevelop.user.dto.UserUpdateRequestDto;
import com.scheduledevelop.user.entity.User;
import com.scheduledevelop.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDto create(UserCreateRequestDto request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw ApiException.conflict("이미 사용 중인 이메일입니다.");
        }

        // 비밀번호 암호화
        String encoderPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(request.getUsername(), request.getEmail(), encoderPassword);

        User saved = userRepository.save(user);

        return new UserResponseDto(saved);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getAll() {
        return userRepository.findAll().stream()
                .map(UserResponseDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserResponseDto getOne(Long userId) {
        User user = findUser(userId);

        return new UserResponseDto(user);
    }

    @Transactional
    public UserResponseDto update(Long userId,Long loginUserId, UserUpdateRequestDto request) {
        User user = findUser(userId);
        validateUserOwner(userId, loginUserId);

        if (userRepository.existsByEmailAndIdNot(request.getEmail(), userId)) {
            throw ApiException.conflict("이미 사용 중인 이메일입니다.");
        }

        // 더티 체킹
        user.updateUser(request.getUsername(), request.getEmail());

        return new UserResponseDto(user);
    }

    @Transactional
    public void delete(Long userId, Long loginUserId) {
        User user = findUser(userId);
        validateUserOwner(userId, loginUserId);

        // 유저를 바로 삭제하면 댓글/일정이 외래 키로 묶여 있어 DB가 삭제를 막는다.
        // 그래서 자식 데이터부터 정리한 뒤 마지막에 유저를 삭제한다.
        commentRepository.deleteAllByUserId(userId);

        commentRepository.deleteAllByScheduleUserId(userId);

        scheduleRepository.deleteAllByUserId(userId);

        userRepository.delete(user);
    }

    // 유저 조회
    // 존재하지 않으면 404 예외 발생
    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> ApiException.notFound("존재하지 않는 유저입니다."));
    }

    // 로그인한 유저가 본인 계정인지 확인
    // 본인계정이 아니면 403 예외 발생
    private void validateUserOwner(Long userId, Long loginUserId) {
        if (!userId.equals(loginUserId)) {
            throw ApiException.forbidden("본인 계정만 수정 또는 삭제할 수 있습니다.");
        }
    }

}
