package com.godarmed.core.starters.authclient.exception;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.godarmed.core.starters.global.entity.ResultModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class TokenInvalidException implements AuthenticationEntryPoint {
    public TokenInvalidException() {
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ResultModel<String> model = new ResultModel();
        model.setSubCode(401);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        model.setMessage("身份认证信息无效");

        try {
            response.getWriter().write(JSON.toJSONString(model));
        } catch (IOException var6) {
            var6.printStackTrace();
        }

    }
}
