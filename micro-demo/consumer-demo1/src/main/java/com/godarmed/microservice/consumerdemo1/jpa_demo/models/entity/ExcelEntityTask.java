package com.godarmed.microservice.consumerdemo1.jpa_demo.models.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@ApiModel(value = "ExcelEntityTask",description = "端口号zip批量申请任务表")
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@Table(name = "SERV_EXCELENTITY_TASK")
public class ExcelEntityTask implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "myIdStrategy")
	@GenericGenerator(name = "myIdStrategy", strategy = "com.godarmed.microservice.consumerdemo1.jpa_demo.config.CustomIDGenerator")
	private String id;

	private String taskName;

	@ApiModelProperty(value = "创建时间(第一次入库)")
	@CreatedDate
	private Date createTime;

	@ApiModelProperty(value = "修改时间(最后一次次入库)")
	@LastModifiedDate
	private Date updateTime;

	@ApiModelProperty(value = "操作人名称")
	private String createName;
	
	private Integer total;

	@OneToMany(mappedBy="excelEntityTask",cascade=CascadeType.ALL,fetch=FetchType.LAZY,orphanRemoval = true)
	//级联保存、更新、删除、刷新;延迟加载。当删除用户，会级联删除该用户的所有文章
	//拥有mappedBy注解的实体类为关系被维护端
	//mappedBy="author"中的author是Article中的author属性
	private List<ExcelEntityDetail> excelEntityDetailList;//任务详情
}
