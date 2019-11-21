package com.godarmed.core.starters.datasource;

import java.io.Serializable;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "database"
)
@Data
public class DataBaseProperties implements Serializable {
    private static final long serialVersionUID = 1L;
    @Value("${database.prefix:PM_}")
    private String prefix = "PM_";
    @Value("${database.isopen:false}")
    private Boolean isopen = false;

    public DataBaseProperties() {
    }

    public String getPrefix() {
        return this.prefix;
    }

    public Boolean getIsopen() {
        return this.isopen;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setIsopen(Boolean isopen) {
        this.isopen = isopen;
    }
}

