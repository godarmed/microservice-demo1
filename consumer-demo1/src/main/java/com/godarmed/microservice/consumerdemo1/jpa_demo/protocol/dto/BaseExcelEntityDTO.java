package com.godarmed.microservice.consumerdemo1.jpa_demo.protocol.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper=false)
@Data
public class BaseExcelEntityDTO extends BaseDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        @NotNull
        private Long id;

        private Long taskId;

        private String taskName;

        private Integer total;

        private Integer rows;

        private Long merchantId;

        private String merchantName;

        private String port;

        private String sign;

        private String usage;

        private String province;

        private String authorizationFiles;

        private Date validUntil;

        private Date validStart;

        private String status;

        private String message;

        private Integer errorcode;
}
