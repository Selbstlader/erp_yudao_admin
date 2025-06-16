package cn.iocoder.yudao.module.infra.framework.dify.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Dify 同步状态枚举
 */
@Getter
@AllArgsConstructor
public enum DifySyncStatusEnum {

    /**
     * 未同步
     */
    UNSYNC(0, "未同步"),

    /**
     * 待同步
     */
    TO_SYNC(4, "待同步"),

    /**
     * 同步中
     */
    SYNCING(1, "同步中"),

    /**
     * 已同步
     */
    SYNCED(2, "已同步"),

    /**
     * 同步失败
     */
    SYNC_FAILED(3, "同步失败");

    /**
     * 状态值
     */
    private final Integer status;

    /**
     * 状态名
     */
    private final String name;
} 