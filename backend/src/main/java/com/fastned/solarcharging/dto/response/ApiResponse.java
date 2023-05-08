package com.fastned.solarcharging.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents the API response in a  format with  message and data
 *
 * @param <T>
 */
@Data
@AllArgsConstructor
public class ApiResponse<T> {

    //private Long timestamp;
    private final String message;
    private final T data;

    public ApiResponse(Long timestamp, String message) {
        //this.timestamp = timestamp;
        this.message = message;
        this.data = null;
    }
}
