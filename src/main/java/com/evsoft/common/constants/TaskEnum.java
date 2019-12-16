package com.evsoft.common.constants;

/**
 * Created by huangwenchang on 2017/8/22.
 */
public class TaskEnum {

    /**
     * 分配日志无历史跟踪人的值
     */
    public static final long TRACKER_NOBODY = -1L;

    /**
     * 工作任务操作类型
     * Created by huangwenchang on 2017/8/22.
     */
    public enum OperateTypeEnum {
        /**
         * 工作分配
         */
        ALLOT(0),

        /**
         *工作移交
         */
        TRANSFER(1);

        private int value;

        private OperateTypeEnum(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }


    }

    public enum DeleteFlagEnum {
        /**
         * 未删除
         */
        NOT_DELETED(0),
        /**
         * 已删除
         */
        DELETED(1);

        private int value;

        private DeleteFlagEnum(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
