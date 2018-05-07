package com.dindong.dingdong.network;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Created by neil on 2017/8/4.
 */
@Documented
@Target(PARAMETER)
@Retention(RUNTIME)
public @interface PartName {
  String value() default "";
}