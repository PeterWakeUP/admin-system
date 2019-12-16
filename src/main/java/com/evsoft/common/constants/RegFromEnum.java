package com.evsoft.common.constants;


import java.util.Arrays;
import java.util.List;

/**
 * 注册来源
 */
public enum RegFromEnum {

    /**
     * 苹果
     */
    IOS(7, "IOS"),
    /**
     * 安卓
     */
    ANDROID(8, "Andriod"),
    /**
     * PC
     */
    PC(9, "PC"),
    /**
     * 触屏版
     */
    WAP(10, "WAP"),
    /**
     * 微信
     */
    WECHAT(11, "微信");

    private int code;
    private String desc;

    RegFromEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * <p> 通过可以获取枚举的值</p>
     * @param desc
     * @return
     * @throws
     */
    public static int getCodeByDesc(String desc) {
        for (RegFromEnum en : RegFromEnum.values()){
            if ( en.desc.equals(desc)) {
                return en.code;
            }
        }
        return -1;
    }

    /**
     * <p> 通过可以获取枚举的值</p>
     * @param code
     * @return
     * @throws
     */
    public static String getDescByCode(Integer code) {
        if(code == null){
            return "";
        }
        for (RegFromEnum en : RegFromEnum.values()){
            if ( en.code == code) {
                return en.desc;
            }
        }
        return "未定义";
    }

    /**
     * <p>通过key获取类型对象</p>
     * @param code
     * @return
     * @throws
     */
    public static RegFromEnum newInstance(Integer code) {
        for (RegFromEnum en : RegFromEnum.values()){
            if ( en.code == code ){
                return en;
            }
        }
        return null;
    }

    /**
     * <p>获取枚举对象list</p>
     * @return
     * @throws
     */
    public static List<RegFromEnum> getEnumList() {
        return Arrays.asList(RegFromEnum.values());
    }

}
