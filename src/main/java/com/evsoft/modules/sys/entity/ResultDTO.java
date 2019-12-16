package com.evsoft.modules.sys.entity;

/**
 * Created by steven.xiao on 2017/7/13.
 */
public class ResultDTO<T> {
    private String code;
    private String message;
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static ResultDTO buildSuccessResult() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode("SUCCESS");
        resultDTO.setMessage("服务正常调用");
        return resultDTO;
    }

    public static ResultDTO buildSuccessResult(Object o) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode("SUCCESS");
        resultDTO.setMessage("服务正常调用");
        resultDTO.setData(o);
        return resultDTO;
    }

    public static ResultDTO buildFailedResult(String code, String message, Object o) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        resultDTO.setData(o);
        return resultDTO;
    }

    public static ResultDTO buildFailedResult(String message) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode("FAIL");
        resultDTO.setMessage(message);
        return resultDTO;
    }

    @Override
    public String toString() {
        return "ResultDTO{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
