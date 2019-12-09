package com.godarmed.microservice.consumerdemo1.validater_demo.protocol.dto;

import com.godarmed.microservice.consumerdemo1.jpa_demo.protocol.dto.BaseDTO;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class PersonDTO extends BaseDTO implements Serializable {

    //验证组
    public interface AddPersonGroup {//添加
    }

    public interface UpdatePersonGroup {//修改
    }

    public interface QueryPersonGroup {//查询
    }

    public interface DeletePersonGroup {//删除
    }

    private static final long serialVersionUID = 1L;

    @NotNull(message = "id 不能为空", groups = {UpdatePersonGroup.class, DeletePersonGroup.class})
    private Long id;

    @NotNull(message = "classId 不能为空", groups = {UpdatePersonGroup.class, AddPersonGroup.class})
    private String classId;

    @Size(max = 10,message = "姓名长度应该在1-10之间", groups = {UpdatePersonGroup.class, AddPersonGroup.class})
    @NotNull(message = "name 不能为空", groups = {UpdatePersonGroup.class, AddPersonGroup.class})
    private String name;

    @Pattern(regexp = "((^Man$|^Woman$|^UGM$))", message = "sex 值不在可选范围", groups = {UpdatePersonGroup.class, AddPersonGroup.class})
    @NotNull(message = "sex 不能为空", groups = {UpdatePersonGroup.class, AddPersonGroup.class})
    private String sex;

    @Email(message = "email 格式不正确", groups = {UpdatePersonGroup.class, AddPersonGroup.class})
    @NotNull(message = "email 不能为空", groups = {UpdatePersonGroup.class, AddPersonGroup.class})
    private String email;

}
