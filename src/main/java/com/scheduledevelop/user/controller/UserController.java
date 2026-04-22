package com.scheduledevelop.user.controller;

import com.scheduledevelop.common.Util.SessionUtil;
import com.scheduledevelop.user.dto.UserCreateRequestDto;
import com.scheduledevelop.user.dto.UserResponseDto;
import com.scheduledevelop.user.dto.UserUpdateRequestDto;
import com.scheduledevelop.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // 유저 생성 (회원가입)
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserCreateRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }

    // 유저 다건 조회
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAll());
    }

    // 유저 단건 조회
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getOneUser(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getOne(userId));
    }

    // 유저 수정
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long userId,
            @Valid @RequestBody UserUpdateRequestDto request,
            HttpSession session) {

        Long loginUserId = SessionUtil.getLoginUserId(session);

        return ResponseEntity.status(HttpStatus.OK).body(userService.update(userId, loginUserId, request));
    }

    // 유저 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId, HttpSession session) {

        Long loginUserId = SessionUtil.getLoginUserId(session);

        userService.delete(userId, loginUserId);

        // 유저 삭제 후 세션 무효화
        session.invalidate();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
