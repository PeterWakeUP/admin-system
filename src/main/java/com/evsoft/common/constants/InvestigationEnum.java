package com.evsoft.common.constants;

public enum InvestigationEnum {
    CALL_STATUS("callStatusList","callStatus"),
    OTHER_ONE("otherOneList","otherOne"),
    OTHER_TWO("otherTwoList","otherTwo"),
    TASK_TYPE("taskTypeList","taskType"),
    FIRST_INVITE("firstInviteList","firstInvite"),
    LAST_INVITE("lastInviteList","lastInvite"),
    VIP_LEVEL("vipLevelList","vipLevel")
    ;

    private String key;
    private String dicKey;

    InvestigationEnum(String key, String dicKey) {
        this.key = key;
        this.dicKey = dicKey;
    }

    public String getKey() {
        return key;
    }

    public String getDicKey() {
        return dicKey;
    }
}
