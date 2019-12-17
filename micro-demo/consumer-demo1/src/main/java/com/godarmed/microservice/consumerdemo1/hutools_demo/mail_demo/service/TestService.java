package com.godarmed.microservice.consumerdemo1.hutools_demo.mail_demo.service;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;

public class TestService {
    public static void main(String[] args) {
        MailAccount mailAccount = new MailAccount();
        //MailUtil.send()
        MailUtil.send("2570613257@qq.com", "邮件测试", "邮件来自Hutool测试", false);
    }
}
