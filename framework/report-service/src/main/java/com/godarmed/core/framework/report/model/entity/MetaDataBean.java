package com.godarmed.core.framework.report.model.entity;

import io.swagger.annotations.Api;
import lombok.Data;

/**
 * @class: MetaDataBean
 * @description: 元数据对象
 * @author: fs
 * @create: 2019/12/6 12:03
 **/
@Data
public class MetaDataBean {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 列英文名
     */
    private String columnEngName;

    /**
     * 列中文名
     */
    private String columnChnName;

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 数据长度
     */
    private Integer dataLength;

    /**
     * 是否为空
     */
    private String isNull;

    /**
     * 列id
     */
    private Integer columnId;

    /**
     * 是否主键
     */
    private String isPrimaryKey;
}

