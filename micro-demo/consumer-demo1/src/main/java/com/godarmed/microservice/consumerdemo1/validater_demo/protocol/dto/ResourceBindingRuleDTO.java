package com.godarmed.microservice.consumerdemo1.validater_demo.protocol.dto;

import com.godarmed.microservice.consumerdemo1.validater_demo.config.NumIn;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Arrays;

@Data
public class ResourceBindingRuleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    public interface AddOne {
    }

    public interface UpdateOne {
    }

    public interface DeleteOne {
    }


    @NotNull(message = "唯一标识不能为空", groups = {DeleteOne.class, UpdateOne.class})
    @ApiModelProperty(value = "唯一标识")
    private Long id;

    @ApiModelProperty(value = "商家名称")
    private String merchantName;

    @NotNull(message = "服务号Id不能为空", groups = {AddOne.class, UpdateOne.class})
    @ApiModelProperty(value = "服务号id", example = "0,1,2,3")
    private Long pubId;

    @ApiModelProperty(value = "服务号名称")
    private String pubName;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "签名列表")
    private String signs;

    @NotBlank(message = "规则名称不能为空", groups = {AddOne.class, UpdateOne.class})
    @ApiModelProperty(value = "规则名称")
    private String ruleName;

    @ApiModelProperty(value = "规则描述")
    private String note;

    @NumIn(message = "规则状态超出限制",name = "规则状态",nums = {0,1,2}, groups = {AddOne.class, UpdateOne.class})
    @NotNull(message = "规则状态不能为空", groups = {AddOne.class, UpdateOne.class})
    @ApiModelProperty(value = "规则状态", example = "0 - 启用 1 - 不启用")
    private Integer status;

    @NumIn(message = "规则类型超出限制",name = "规则类型",nums = {0,1}, groups = {AddOne.class, UpdateOne.class})
    @NotNull(message = "规则类型不能为空", groups = {AddOne.class, UpdateOne.class})
    @ApiModelProperty(value = "规则类型", example = "0,1,2,3")
    private Integer type;

    @ApiModelProperty(value = "分页页数")
    private Integer page = 0;

    @ApiModelProperty(value = "每页大小")
    private Integer size = 10;
}