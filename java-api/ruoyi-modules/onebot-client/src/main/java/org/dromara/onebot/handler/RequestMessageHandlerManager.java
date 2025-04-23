package org.dromara.onebot.handler;

import lombok.extern.slf4j.Slf4j;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.onebot.annotation.RequestMessageMapping;
import org.dromara.onebot.entity.message.ChatMessage;
import org.dromara.onebot.entity.request.RequestMessage;
import org.dromara.onebot.entity.request.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.InitBinder;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 请求消息处理器管理器
 */
@Slf4j
@Component
public class RequestMessageHandlerManager {

//    @Autowired
//    private ApplicationContext applicationContext;

    /**
     * 请求处理器映射表，key为action名称，value为处理器方法信息
     */
    private final Map<String, List<HandlerMethodInfo>> handlerMethodMap = new ConcurrentHashMap<>();

    /**
     * 初始化，扫描所有带有RequestMessageMapping注解的处理器方法
     */
    public void init(List<MessageHandler> list) {
        // 获取所有MessageHandler实现类
//        Map<String, Object> handlers = applicationContext.getBeansOfType(Object.class);
        
        // 遍历所有Bean
        for (Object handler : list) {
            Class<?> handlerClass = handler.getClass();
            
            // 检查类上是否有RequestMessageMapping注解
            RequestMessageMapping classMapping = handlerClass.getAnnotation(RequestMessageMapping.class);
            String classAction = classMapping != null ? classMapping.value() : "";
            
            // 扫描所有方法
            for (Method method : handlerClass.getMethods()) {
                RequestMessageMapping methodMapping = method.getAnnotation(RequestMessageMapping.class);
                if (methodMapping != null) {
                    String action = methodMapping.value();
                    if (action.isEmpty() && !classAction.isEmpty()) {
                        action = classAction;
                    }
                    
                    // 检查方法参数和返回值是否符合要求
                    if (isValidHandlerMethod(method)) {
                        // 创建处理器方法信息
                        HandlerMethodInfo methodInfo = new HandlerMethodInfo(
                            handler,
                            method,
                            methodMapping.priority(),
                            methodMapping.description()
                        );
                        
                        // 添加到映射表
                        handlerMethodMap.computeIfAbsent(action, k -> new ArrayList<>()).add(methodInfo);
                        log.info("注册消息处理器: action={}, handler={}, method={}, priority={}", 
                                action, handler.getClass().getName(), method.getName(), methodMapping.priority());
                    } else {
                        log.warn("无效的处理器方法: {}.{}", handlerClass.getName(), method.getName());
                    }
                }
            }
        }
        
        // 对每个action的处理器列表按优先级排序
        for (List<HandlerMethodInfo> methods : handlerMethodMap.values()) {
            methods.sort(Comparator.comparingInt(HandlerMethodInfo::getPriority));
        }
        
        log.info("消息处理器注册完成，共注册{}个action处理器", handlerMethodMap.size());
    }
    
    /**
     * 处理请求消息
     *
     * @param message 请求消息
     * @return 响应消息
     */
    public ResponseMessage handleRequest(RequestMessage message) {
        String action = message.getAction();
        List<HandlerMethodInfo> handlers = handlerMethodMap.get(action);
        
        if (handlers != null && !handlers.isEmpty()) {
            // 按优先级顺序尝试处理
            for (HandlerMethodInfo handlerInfo : handlers) {
                try {

                    // 调用处理器方法
//                    Class<?>[] parameterTypes = handlerInfo.getMethod().getParameterTypes();
//
//                    Object[] params = new Object[parameterTypes.length];
//
//                    for (int i = 0; i < parameterTypes.length; i++) {
//                        params[0] = JsonUtils.parseObject(message.getJson(), parameterTypes[i]);
//                    }

                    Object result = handlerInfo.getMethod().invoke(handlerInfo.getHandler(), message);
                    if (result instanceof ResponseMessage) {
                        ResponseMessage response = (ResponseMessage) result;
                        response.setEcho(message.getEcho());
                        return response;
                    }
                } catch (Exception e) {
                    log.error("处理请求消息失败: action={}, handler={}, error={}", 
                            action, handlerInfo.getHandler().getClass().getName(), e.getMessage(), e);
                }
            }
        }
        
        // 没有找到处理器或处理失败，返回默认响应
        return ResponseMessage.builder()
                .status("failed")
                .retcode(404)
                .message("No handler found for action: " + action)
                .echo(message.getEcho())
                .build();
    }
    
    /**
     * 检查方法是否是有效的处理器方法
     * 有效的处理器方法应该接受一个RequestMessage参数，并返回ResponseMessage
     */
    private boolean isValidHandlerMethod(Method method) {
        // 检查返回类型
        if (!ResponseMessage.class.isAssignableFrom(method.getReturnType())) {
            return false;
        }
        
        // 检查参数
        Class<?>[] paramTypes = method.getParameterTypes();
        return paramTypes.length == 1 && RequestMessage.class.isAssignableFrom(paramTypes[0]);
    }
    
    /**
     * 处理器方法信息
     */
    private static class HandlerMethodInfo {
        private final Object handler;
        private final Method method;
        private final int priority;
        private final String description;
        
        public HandlerMethodInfo(Object handler, Method method, int priority, String description) {
            this.handler = handler;
            this.method = method;
            this.priority = priority;
            this.description = description;
        }
        
        public Object getHandler() {
            return handler;
        }
        
        public Method getMethod() {
            return method;
        }
        
        public int getPriority() {
            return priority;
        }
        
        public String getDescription() {
            return description;
        }
    }
}