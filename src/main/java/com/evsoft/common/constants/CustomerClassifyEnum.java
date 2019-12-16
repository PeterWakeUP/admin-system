package com.evsoft.common.constants;

/**
 * 客户分类
 * @Author chenmingqing
 * @Date 2018/4/20 10:16
 */
public enum CustomerClassifyEnum {
    NOCLASSIFY(1, "未分类"),
    INVALIDCUSTOMER(2, "无效客户"),
    FOLLOWUPCUSTOMER(3, "正在跟进客户"),
    HASINVESTED(4, "已投资"),
    KEEPCUSTOMER(5, "保留客户"),
    TURNINTRODUCE(6, "转介绍"),
    TWOFOLLOWUP(7, "二次跟进"),
    OTHERFIRST(8, "其他一"),
    OTHERTWO(9, "其他二"),
    OTHERTHREE(10, "其他三"),
    PREALLOCATIONINVES(11, "分配前投资"),
    YELLOWDOG(12, "小黄狗");

    private int value;
    private String label;

    CustomerClassifyEnum(int value, String label) {
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
        if (NOCLASSIFY.value == value) {
            returnLabel = NOCLASSIFY.label;
        } else if (INVALIDCUSTOMER.value == value) {
            returnLabel = INVALIDCUSTOMER.label;
        }else if (FOLLOWUPCUSTOMER.value == value) {
            returnLabel = FOLLOWUPCUSTOMER.label;
        }else if (HASINVESTED.value == value) {
            returnLabel = HASINVESTED.label;
        }
        else if (KEEPCUSTOMER.value == value) {
            returnLabel = KEEPCUSTOMER.label;
        }
        else if (TURNINTRODUCE.value == value) {
            returnLabel = TURNINTRODUCE.label;
        }
        else if (TWOFOLLOWUP.value == value) {
            returnLabel = TWOFOLLOWUP.label;
        }
        else if (OTHERFIRST.value == value) {
            returnLabel = OTHERFIRST.label;
        }
        else if (OTHERTWO.value == value) {
            returnLabel = OTHERTWO.label;
        }
        else if (OTHERTHREE.value == value) {
            returnLabel = OTHERTHREE.label;
        }
        else if (PREALLOCATIONINVES.value == value) {
            returnLabel = PREALLOCATIONINVES.label;
        }
        else if (YELLOWDOG.value == value) {
            returnLabel = YELLOWDOG.label;
        }
        return returnLabel;
    }
}

