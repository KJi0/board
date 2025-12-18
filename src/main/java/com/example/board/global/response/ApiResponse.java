package com.example.board.global.response;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

    private final boolean success;
    private final T data;
    private final Error error;

    private ApiResponse(boolean success, T data, Error error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static ApiResponse<Void> ok() {
        return new ApiResponse<>(true, null, null);
    }

    public static ApiResponse<Void> fail(String code, String message) {
        return new ApiResponse<>(false, null, new Error(code, message));
    }

    @Getter
    public static class Error {
        private final String code;
        private final String message;

        private Error(String code, String message) {
            this.code = code;
            this.message = message;
        }
    }
}
