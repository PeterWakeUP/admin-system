package com.evsoft.common.constants;

/**
 * 意向度枚举
 * @Author chenmingqing
 * @Date 2018/4/20 10:04
 */
public enum  IntentionEnum {
    HIGH(1,"意向度高"),
    MIDDLE(2,"意向度中"),
    LOW(3,"意向度低"),
    NO(4,"无意向");

    private int value;
    private String label;

    IntentionEnum(int value, String label) {
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
        if (HIGH.value == value) {
            returnLabel = HIGH.label;
        } else if (MIDDLE.value == value) {
            returnLabel = MIDDLE.label;
        }else if (LOW.value == value) {
            returnLabel = LOW.label;
        }else if (NO.value == value) {
            returnLabel = NO.label;
        }
        return returnLabel;
    }
}
