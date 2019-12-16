package com.evsoft.common.constants;

/**
 * Created by huangwenchang on 2017/8/15.
 */
public enum BorrowInfoStatusEnum {

    /**
     * 发起借款
     */
    INITIATE(1, "审核中","发起借款", "已发起借款，待审核"),
    /**
     * 已审核
     */
    ACTIVED(2, "审核通过","已审核", "审核通过"),
    /**
     * 未通过
     */
    NOT_PASS(3, "审核未通过","未通过", "审核未通过，感谢您的申请"),
    /**
     * 已放款
     */
    SECURED_LOAN(4, "放款中","放款中", "已放款，还款日%s"),
    /**
     * 待还款
     */
    PENDING_REPAYMENT(5, "待还款","待还款", "待还款"),
    /**
     * 已完成
     */
    SUCCESS(6, "已完成","已完成", "还款成功"),
    /**
     * 已逾期
     */
    OVERDUE(7, "已逾期","已逾期", "已逾期1天，产生违约金20元，每日将另外收取本金1%的逾期滞纳金，请尽快确保银行卡内余额足够还款，避免影响信用。"),
    /**
     * 严重逾期
     */
    SERIOUS_OVERDUE(8, "严重逾期", "严重逾期","已逾期16天，产生违约金20元，每日将另外收取本金2%的逾期滞纳金，请尽快确保银行卡内余额足够还款，避免影响信用。"),

    LOAN_EXCEPTION(9, "放款异常","放款异常", "您的借款因银行卡异常导致放款失败，请尽快联系客服。"),

//    ABNORMAL_DEBIT(10, "扣款异常","扣款异常", ""),

    SYS_CLOSE(99, "借款失效","借款失效", "借款失效");

    private Integer code;
    private String text;
    private String textForPage;
    private String desc;

    BorrowInfoStatusEnum(Integer code, String text, String textForPage, String desc) {
        this.code = code;
        this.text = text;
        this.textForPage = textForPage;
        this.desc = desc;
    }

    public String getText() {
        return text;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getTextForPage() {
        return textForPage;
    }

    /**
     * <p> 通过可以获取枚举的值</p>
     * @param code
     * @return
     * @throws
     */
    public static String getTextByCode(int code) {
        for (BorrowInfoStatusEnum en : BorrowInfoStatusEnum.values()){
            if ( en.code == code) {
                return en.text;
            }
        }
        return "未定义";
    }

    /**
     * <p> 通过可以获取枚举的值</p>
     * @param code
     * @return
     * @throws
     */
    public static String getTextForPageByCode(int code) {
        for (BorrowInfoStatusEnum en : BorrowInfoStatusEnum.values()){
            if ( en.code == code) {
                return en.textForPage;
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
    public static BorrowInfoStatusEnum newInstance(int code) {
        for (BorrowInfoStatusEnum en : BorrowInfoStatusEnum.values()){
            if ( en.code == code ){
                return en;
            }
        }
        return null;
    }

}
