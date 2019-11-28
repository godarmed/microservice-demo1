package com.godarmed.core.starters.global.utils;

import com.google.common.base.Strings;

public class StringTemplate {
    private static final String PLACEHOLD = "\\{\\}";

    public StringTemplate() {
    }

    public static final String template(String template, Object... args) {
        if (template != null && template.indexOf("{}") >= 0) {
            if ("{}".equals(template)) {
                return args == null ? null : String.valueOf(args[0]);
            } else {
                template = template + " ";
                String[] templates = template.split("\\{\\}");
                int length = templates.length;
                int argLength = args == null ? 0 : args.length;
                StringBuilder sb = new StringBuilder();

                for(int i = 0; i < length; ++i) {
                    sb.append(templates[i]);
                    if (argLength > i && i != length - 1) {
                        sb.append(args[i]);
                    }
                }

                return sb.toString();
            }
        } else {
            return template;
        }
    }

    public static final String transFirst2Upper(String arg) {
        return !Strings.isNullOrEmpty(arg) ? arg.substring(0, 1).toUpperCase() + arg.substring(1, arg.length()) : arg;
    }
}

