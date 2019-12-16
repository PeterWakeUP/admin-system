package com.evsoft.common.constants;

/**
 *接通状态
 * @Author chenmingqing
 * @Date 2018/4/20 10:36
 */
public enum CallThroughStatusEnum {
    VALID(1,"有效"),
    INVALID(2,"无效"),
    PENDING(3,"待审批");

    private int value;
    private String label;

    CallThroughStatusEnum(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public int getValue() {

        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public static String getLabelByValue(int value) {
        String returnLabel = "";
        if (VALID.value == value) {
            returnLabel = VALID.label;
        } else if (INVALID.value == value) {
            returnLabel = INVALID.label;
        }else if (PENDING.value == value) {
            returnLabel = PENDING.label;
        }
        return returnLabel;
    }
}
