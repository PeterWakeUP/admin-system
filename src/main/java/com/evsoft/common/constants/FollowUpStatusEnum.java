package com.evsoft.common.constants;

/**
 * 跟进状态
 * @Author chenmingqing
 * @Date 2018/4/20 9:43
 */
public enum  FollowUpStatusEnum {
    YESLINK(1,"已联系"),
    NOLINK(2,"未联系");

    private int value;
    private String label;

    FollowUpStatusEnum(int value, String label) {
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
        if (YESLINK.value == value) {
            returnLabel = YESLINK.label;
        } else if (NOLINK.value == value) {
            returnLabel = NOLINK.label;
        }
        return returnLabel;
    }
}
