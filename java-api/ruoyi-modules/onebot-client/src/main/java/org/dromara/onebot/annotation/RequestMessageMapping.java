package org.dromara.onebot.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 请求消息映射注解，用于标记处理特定action的方法
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMessageMapping {

    /**
     * 请求动作名称
     */
    public String value();
    
    /**
     * 请求描述
     */
    String description() default "";
    
    /**
     * 优先级，数值越小优先级越高
     */
    int priority() default 0;
}
