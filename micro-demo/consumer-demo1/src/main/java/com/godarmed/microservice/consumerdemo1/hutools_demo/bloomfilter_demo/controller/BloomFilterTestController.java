package com.godarmed.microservice.consumerdemo1.hutools_demo.bloomfilter_demo.controller;

import cn.hutool.bloomfilter.BitMapBloomFilter;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.http.HtmlUtil;

public class BloomFilterTestController {
    public static void main(String[] args) {
        BitMapBloomFilter filter = new BitMapBloomFilter(10);
        filter.add("123");
        filter.add("abc");
        filter.add("ddd");

        // 查找
        System.out.println(filter.contains("abc"));

        String html = "<div class=\"test_div\" width=\"120\"></div>";
        // 结果为：<div></div>
        String result = HtmlUtil.removeAllHtmlAttr(html, "div");
        System.out.println(result);

        StrBuilder builder = StrBuilder.create();
    }
}
