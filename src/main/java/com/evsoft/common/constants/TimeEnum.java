package com.evsoft.common.constants;

/**
 * 时间枚举
 * @Author chenmingqing
 * @Date 2018/4/20 10:51
 */
public enum TimeEnum {
    ALLOTTIME(1,"分配时间"),
    DIALTIME(2,"拨打时间"),
    REGTIME(3,"注册时间"),
    CALLTIME(4,"通话时间"),
    WARNTIME(5,"预警时间");

    private int value;
    private String label;

    TimeEnum(int value, String label) {
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
        if (ALLOTTIME.value == value) {
            returnLabel = ALLOTTIME.label;
        } else if (DIALTIME.value == value) {
            returnLabel = DIALTIME.label;
        }else if (REGTIME.value == value) {
            returnLabel = REGTIME.label;
        }else if (CALLTIME.value == value) {
            returnLabel = CALLTIME.label;
        }else if (WARNTIME.value == value) {
            returnLabel = WARNTIME.label;
        }
        return returnLabel;
    }
}
