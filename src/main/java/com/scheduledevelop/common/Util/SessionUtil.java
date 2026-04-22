package com.scheduledevelop.common.Util;

import com.scheduledevelop.common.config.SessionKey;
import com.scheduledevelop.common.exception.ApiException;
import jakarta.servlet.http.HttpSession;

/**
 * 세션에서 로그인한 유저 ID를 조회하는 유틸 클래스.
 * Controller의 중복 로그인 확인 로직을 공통화하기 위해 사용한다.
 */
public final class SessionUtil {

    private SessionUtil() {}

    public static Long getLoginUserId(HttpSession session) {

        if (session == null || session.getAttribute(SessionKey.LoginUserId) == null) {
            throw ApiException.unauthorized("로그인이 필요한 요청입니다.");
        }

        return (Long) session.getAttribute(SessionKey.LoginUserId);
    }


}
