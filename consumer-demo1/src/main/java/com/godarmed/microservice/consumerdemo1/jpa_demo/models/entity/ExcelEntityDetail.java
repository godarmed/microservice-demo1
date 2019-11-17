package com.godarmed.microservice.consumerdemo1.jpa_demo.models.entity;

import com.alibaba.excel.metadata.BaseRowModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author zhanghai by 2019/9/12
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ExcelEntityDetail",description = "端口号zip批量申请")
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@Table(name = "SERV_EXCELENTITY_DETAIL")
public class ExcelEntityDetail extends BaseRowModel implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	private Long taskId;
	
	private Integer rows;

    private Long merchantId;

    private String merchantName;

    private String port;

    private String sign;

    @Column(name = "`usage`")
    private String usage;

    private String province;

    private String authorizationFiles;
    
    private Date validUntil;

    private Date validStart;

    private String status;

    private String message;
    
	@ApiModelProperty(value = "创建时间(第一次入库)")
	@CreatedDate
    private Date createTime;
    
	@ApiModelProperty(value = "操作时间(变更时间)")
    @LastModifiedDate
    private Date operTime;
	
	@ApiModelProperty(value = "操作人名称")
	private String operateName;

    @ApiModelProperty(value = "错误码")
    private Integer errorcode = 0;
    
   /* //服务号信息
	@ApiModelProperty(value = "端口号zip批量申请任务表")
	@ManyToOne(cascade=CascadeType.DETACH ,fetch = FetchType.LAZY)
	@JoinColumn(name="task_id",foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	@NotFound(action = NotFoundAction.IGNORE)
    private ExcelEntityTask excelEntityTask;*/

}
