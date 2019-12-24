package com.godarmed.microservice.consumerdemo1.hutools_demo.mail_demo.controller;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.godarmed.microservice.consumerdemo1.hutools_demo.mail_demo.config.MailProperties;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;

@RestController
@RequestMapping("/mailTest")
@Log4j2
public class MailTestController {
    @Autowired
    MailProperties mailProperties;

    @RequestMapping("/sendMessageText")
    public String sendMessageText() {
        MailAccount mailAccount = new MailAccount();
        BeanUtils.copyProperties(mailProperties, mailAccount);
        mailAccount.defaultIfEmpty();
        log.info(mailAccount);
        return MailUtil.send(mailAccount, Arrays.asList("2570613257@qq.com"), "邮件测试", "邮件来自Hutool测试" + new Date(), false);
    }

}
