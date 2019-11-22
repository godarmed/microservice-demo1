package com.godarmed.core.starters.authclient.exception;

import com.alibaba.fastjson.JSON;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.godarmed.core.starters.global.entity.ResultModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class AccessDeniedException implements AccessDeniedHandler {
    public AccessDeniedException() {
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, org.springframework.security.access.AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpStatus.OK.value());
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        ResultModel<String> model = new ResultModel();
        model.setSubCode(403);

        try {
            model.setMessage(accessDeniedException.getMessage());
            response.getWriter().write(JSON.toJSONString(model));
        } catch (IOException var6) {
            var6.printStackTrace();
        }

    }
}

