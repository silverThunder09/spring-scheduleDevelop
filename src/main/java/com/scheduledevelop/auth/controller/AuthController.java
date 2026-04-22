package com.scheduledevelop.auth.controller;

import com.scheduledevelop.auth.dto.LoginRequestDto;
import com.scheduledevelop.auth.dto.LoginResponseDto;
import com.scheduledevelop.auth.service.AuthService;
import com.scheduledevelop.common.config.SessionKey;
import com.scheduledevelop.common.Util.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @Valid @RequestBody LoginRequestDto request,
            HttpServletRequest httpServletRequest
    ) {
        LoginResponseDto response = authService.login(request);

        HttpSession session = httpServletRequest.getSession();
        session.setAttribute(SessionKey.LoginUserId, response.getUserId());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession seesion) {
        // 로그인 여부 먼저 검증
        SessionUtil.getLoginUserId(seesion);

        // 위 검증을 통과했다면 세션이 존재하므로 바로 무효화
        seesion.invalidate();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
