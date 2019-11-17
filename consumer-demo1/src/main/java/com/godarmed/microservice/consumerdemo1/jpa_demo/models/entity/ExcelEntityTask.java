package com.godarmed.microservice.consumerdemo1.jpa_demo.models.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@ApiModel(value = "ExcelEntityTask",description = "端口号zip批量申请任务表")
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@Table(name = "SERV_EXCELENTITY_TASK")
public class ExcelEntityTask implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private String taskName;
	
	@ApiModelProperty(value = "创建时间(第一次入库)")
	@CreatedDate
    private Date createTime;
	
	@ApiModelProperty(value = "操作人名称")
	private String createName;
	
	private Integer total;

}
