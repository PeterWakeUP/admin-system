package com.evsoft.common.constants;

/**
 * 会员等级枚举
 * @Author chenmingqing
 * @Date 2018/4/20 11:00
 */
public enum MemberLevel {
    FIRST(1,"V1"),
    TWO(2,"V2"),
    THIRD(3,"V3"),
    FORURTH(4,"V4"),
    FIVE(5,"V5"),
    SIX(6,"V6"),
    SEVEN(7,"V7"),
    EIGHT(8,"V8"),
    NINE(9,"V9");

    private int value;
    private String label;

    MemberLevel(int value, String label) {
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
        if (FIRST.value == value) {
            returnLabel = FIRST.label;
        } else if (TWO.value == value) {
            returnLabel = TWO.label;
        }else if (THIRD.value == value) {
            returnLabel = THIRD.label;
        }else if (FORURTH.value == value) {
            returnLabel = FORURTH.label;
        }else if (FIVE.value == value) {
            returnLabel = FIVE.label;
        }else if (SIX.value == value) {
            returnLabel = SIX.label;
        }else if (SEVEN.value == value) {
            returnLabel = SEVEN.label;
        }else if (EIGHT.value == value) {
            returnLabel = EIGHT.label;
        }else if (NINE.value == value) {
            returnLabel = NINE.label;
        }
        return returnLabel;
    }
}
