package top.sinkdev.rm.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleResponse<T> {
    private T data;
    private String message;
    private int code;


    public static <R> SimpleResponse<R> createSuccessResponse(R data, String message) {
        return new SimpleResponse<>(data, message, CODE_SUCCESS);
    }


    public static <R> SimpleResponse<R> createFailureResponse(String message) {
        return new SimpleResponse<>(null, message, CODE_FAILURE);
    }


    public boolean isSuccessful() {
        return code == CODE_SUCCESS;
    }

    private final static int CODE_SUCCESS = 0;
    private final static int CODE_FAILURE = -1;

}
