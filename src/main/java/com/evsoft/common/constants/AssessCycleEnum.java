package com.evsoft.common.constants;

/**
 * 考核周期
 * @Author chenmingqing
 * @Date 2018/4/20 11:09
 */
public enum AssessCycleEnum {
    RECENT(1,"近效期"),
    OVERDUE(2,"逾期");

    private int value;
    private String label;

    AssessCycleEnum(int value, String label) {
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
        if (RECENT.value == value) {
            returnLabel = RECENT.label;
        } else if (OVERDUE.value == value) {
            returnLabel = OVERDUE.label;
        }
        return returnLabel;
    }
}
