package com.godarmed.microservice.consumerdemo1.validater_demo.config;


import java.util.function.Function;

public class ValidMethod<IN,OUT> {
    Function<IN,OUT> processor;
}
