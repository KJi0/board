package com.example.board.global.security;

import com.example.board.global.exception.CustomException;
import com.example.board.global.exception.ErrorCode;
import jakarta.servlet.http.HttpSession;
// 로그인 강제하는 유틸 (컨트롤러에서 1줄로 쓰려고)
public final class AuthUtil {

    private AuthUtil() {}

    public static SessionUser requireLogin(HttpSession session) {
        if (session == null) throw new CustomException(ErrorCode.UNAUTHORIZED);

        Object obj = session.getAttribute(SessionConst.LOGIN_USER);
        if (!(obj instanceof SessionUser su)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        return su;
    }

    public static void requireAdmin(SessionUser su) {
        if (!"ADMIN".equals(su.getRole())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }
}
