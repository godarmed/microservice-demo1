package com.godarmed.microservice.consumerdemo1.hutools_demo.mail_demo.config;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

import static cn.hutool.core.util.CharsetUtil.CHARSET_UTF_8;

@ConfigurationProperties(
        prefix = "mail"
)
@Component
@Data
public class MailProperties {

    @ApiModelProperty(name = "邮件服务器的SMTP地址")
    private String host = "smtp.qq.com";

    @ApiModelProperty(name = "邮件服务器的SMTP端口")
    private Integer port = 465;

    @ApiModelProperty(name = "发件人（必须正确，否则发送失败）")
    private String from = "2570613257@qq.com";

    @ApiModelProperty(name = "用户名，默认为发件人邮箱前缀")
    private String user = this.from.substring(0, from.indexOf("@"));

    @ApiModelProperty(name = "密码（注意，某些邮箱需要为SMTP服务单独设置密码，详情查看相关帮助）")
    private String pass = "lumumdzhydrldiji";

    @ApiModelProperty(name = "发件人（必须正确，否则发送失败）")
    private Boolean auth = Strings.isNotBlank(this.pass);

    @ApiModelProperty(name = "是否使用 STARTTLS 安全连接")
    private Boolean starttlsEnable = true;

    @ApiModelProperty(name = "指定实现javax.net.SocketFactory接口的类的名称,这个类将被用于创建SMTP的套接字")
    private String socketFactoryClass = "javax.net.ssl.SSLSocketFactory";

    @ApiModelProperty(name = "指定的端口连接到在使用指定的套接字工厂。如果没有设置,将使用默认端口456")
    private Integer socketFactoryPort = this.port;
}


