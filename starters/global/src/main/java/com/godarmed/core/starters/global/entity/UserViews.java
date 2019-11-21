package com.godarmed.core.starters.global.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class UserViews implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private String password;
    private int adminFlag;
    private List<String> roles = new ArrayList();
    private boolean needPassword = true;
    private Map<String, Object> extend = new HashMap();
}

