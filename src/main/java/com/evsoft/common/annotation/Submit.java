package com.evsoft.common.annotation;

import java.lang.annotation.*;

/**
 * Created by 苏文辉 on 2019/3/18.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Submit {

    String key() default "";
}
