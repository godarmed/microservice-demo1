package com.godarmed.core.starters.global.utils.security;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SensitiveHandler {
    private Map<String, Object> hashmap = new LinkedHashMap();
    private String json = null;
    private List<Map<String, Object>> pathList = new ArrayList();

    public SensitiveHandler(String json) {
        this.json = json;
        this.parseString2Object(json);
        if (this.pathList.size() == 1) {
            this.changeToMap("root", this.pathList.get(0));
        } else {
            this.changeToMap("root", this.pathList);
        }

    }

    public Map<String, Object> get(String str) {
        Map<String, Object> resultMap = new LinkedHashMap();
        Iterator var3 = this.hashmap.keySet().iterator();

        while(var3.hasNext()) {
            Object obj = var3.next();
            if (this.anotherEquals(str, (String)obj)) {
                resultMap.put((String)obj, this.hashmap.get(obj));
            }
        }

        return resultMap;
    }

    public List<Object> get(String[] str) {
        List<Object> list = new ArrayList();
        String[] var3 = str;
        int var4 = str.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String s = var3[var5];
            list.add(this.get(s));
        }

        return list;
    }

    public List<Object> get(List<String> str) {
        List<Object> list = new ArrayList();
        Iterator var3 = str.iterator();

        while(var3.hasNext()) {
            String s = (String)var3.next();
            list.add(this.get(s));
        }

        return list;
    }

    private void changeToMap(String totalPathName, Object obj) {
        Object oldKey = null;
        int oldNum = -1;
        if (obj instanceof List) {
            List list = (List)obj;

            for(int i = 0; i < list.size(); oldNum = i++) {
                if (oldNum != -1) {
                    totalPathName = totalPathName.replace(String.valueOf(oldNum), String.valueOf(i));
                } else {
                    totalPathName = totalPathName + "[" + i + "]";
                }

                this.changeToMap(totalPathName, list.get(i));
            }
        } else if (obj instanceof Map) {
            Map map = (Map)obj;

            Object k;
            for(Iterator var9 = map.keySet().iterator(); var9.hasNext(); oldKey = k) {
                k = var9.next();
                if (oldKey != null) {
                    totalPathName = totalPathName.replace((String)oldKey, (String)k);
                } else {
                    totalPathName = totalPathName + "." + (String)k;
                }

                this.changeToMap(totalPathName, map.get(k));
            }
        } else {
            this.hashmap.put(totalPathName, obj);
        }

    }

    private boolean anotherEquals(String str1, String str2) {
        String[] split1 = str1.split("\\.");
        String[] split2 = str2.split("\\.");
        boolean flag = false;
        if (split1.length == split2.length) {
            for(int i = 0; i < split1.length; ++i) {
                if (split1[i].contains("*")) {
                    flag = true;
                } else if (split1[i].equals(split2[i])) {
                    flag = true;
                } else {
                    flag = false;
                }

                if (!flag) {
                    return false;
                }
            }
        }

        return flag;
    }

    private void parseString2Object(String content) {
        String currentString = content.trim();
        if (currentString.startsWith("[") && currentString.endsWith("]")) {
            this.pathList.addAll(this.formatJsonArray(JSON.parseArray(currentString)));
        } else if (currentString.startsWith("{") && currentString.endsWith("}")) {
            this.pathList.add(this.formatJsonObject(currentString));
        } else if (currentString.indexOf("=") > 0) {
            this.pathList.add(this.formatJsonObject(this.parseQUeryString(content).toJSONString()));
        }

    }

    private List<Map<String, Object>> formatJsonArray(JSONArray jsonArray) {
        ArrayList<Map<String, Object>> list = new ArrayList();

        for(int i = 0; i < jsonArray.size(); ++i) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            list.add(this.formatJsonObject(jsonObject.toJSONString()));
        }

        return list;
    }

    public Map<String, Object> formatJsonObject(String jsonStr) {
        Map<String, Object> map = new HashMap();
        JSONObject json = JSON.parseObject(jsonStr);
        Iterator var4 = json.keySet().iterator();

        while(true) {
            while(var4.hasNext()) {
                Object k = var4.next();
                Object v = json.get(k);
                if (v instanceof JSONArray) {
                    List<Map<String, Object>> list = new ArrayList();
                    Iterator it = ((JSONArray)v).iterator();

                    while(it.hasNext()) {
                        Object json2 = it.next();
                        list.add(this.formatJsonObject(json2.toString()));
                    }

                    map.put(k.toString(), list);
                } else {
                    map.put(k.toString(), v);
                }
            }

            return map;
        }
    }

    private JSONObject parseQUeryString(String content) {
        if (content.indexOf("?") >= 0) {
            content = content.substring(content.indexOf("?"));
        }

        String[] sections = content.split("&");
        JSONObject parsedContent = new JSONObject();
        String[] kv = null;
        String[] var5 = sections;
        int var6 = sections.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            String section = var5[var7];
            kv = section.split("=");
            if (kv.length != 2) {
                return null;
            }

            parsedContent.put(kv[0], kv[1]);
        }

        return parsedContent;
    }
}
