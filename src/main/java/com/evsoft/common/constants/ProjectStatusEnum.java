package com.evsoft.common.constants;

/**
 * 标的状态
 * Created by yaodingguo on 2017/9/7.
 */
public enum ProjectStatusEnum {
    NOPASS(1,"未通过审核"),

    PASSED1(12, "审核通过"),

    PASSED2(15, "审核通过待投资"),

    PASSED(2, "审核通过"),

    PAYING(3, "满标"),

    PAYING1(7, "下架"),

    FINISH(6, "项目完成"),

    LOST(4,"流标");

    private int code;
    private String message;

    private ProjectStatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    public static String getMessageByCode(int code) {
        ProjectStatusEnum v = get(code);
        return v == null ? "" : v.message;
    }

    private static ProjectStatusEnum get(int code) {
        for(ProjectStatusEnum v : values()) {
            if(v.code == code) {
                return v;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }
}
