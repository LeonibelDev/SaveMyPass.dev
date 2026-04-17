package com.dac.passwordmanager.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDTO<T> {

    private boolean success;
    private String message;
    private T data;

    public static <T> ApiResponseDTO<T> ok(T data) {
        return new ApiResponseDTO<>(true, "OK", data);
    }

    public static <T> ApiResponseDTO<T> ok(String message, T data) {
        return new ApiResponseDTO<>(true, message, data);
    }

    public static <T> ApiResponseDTO<T> error(String message) {
        return new ApiResponseDTO<>(false, message, null);
    }
}
