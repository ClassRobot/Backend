# OneBot消息处理机制

## 概述

本模块实现了基于注解的消息处理机制，通过`@RequestMessageMapping`注解将请求消息路由到对应的处理方法，实现了消息处理的解耦和扩展。

## 使用方法

### 1. 创建消息处理器

创建一个类，实现`RequestMessageHandler`接口，并使用`@Service`注解将其注册为Spring Bean：

```java
@Service
public class MyRequestMessageHandler implements RequestMessageHandler {
    
    // 实现接口方法（通常不会直接调用）
    @Override
    public ResponseMessage handleRequest(RequestMessage message) {
        throw new UnsupportedOperationException("请使用@RequestMessageMapping注解的方法处理请求");
    }
}
```

### 2. 添加处理方法

在处理器类中添加处理特定请求的方法，并使用`@RequestMessageMapping`注解标记：

```java
@RequestMessageMapping(value = "get_user_info", description = "获取用户信息", priority = 0)
public ResponseMessage getUserInfo(RequestMessage message) {
    // 处理请求并返回响应
    ResponseMessage.UserData userData = new ResponseMessage.UserData();
    userData.setUser_id("user123");
    userData.setUser_name("User Name");
    
    return ResponseMessage.builder()
            .status("ok")
            .data(userData)
            .echo(message.getEcho())
            .build();
}
```

### 3. 注解参数说明

`@RequestMessageMapping`注解有以下参数：

- `value`：请求动作名称，必填
- `description`：请求描述，可选
- `priority`：优先级，数值越小优先级越高，默认为0

### 4. 处理器优先级

当多个处理器方法映射到同一个action时，会按照priority从小到大的顺序尝试处理，直到有一个处理器成功返回响应。

### 5. 类级别注解

也可以在类上使用`@RequestMessageMapping`注解，作为方法注解的前缀：

```java
@Service
@RequestMessageMapping("user")
public class UserMessageHandler implements RequestMessageHandler {
    
    // 实际action为"user.get_info"
    @RequestMessageMapping("get_info")
    public ResponseMessage getInfo(RequestMessage message) {
        // 处理请求
    }
}
```

## 工作原理

1. 系统启动时，`RequestMessageHandlerManager`会扫描所有带有`@RequestMessageMapping`注解的方法
2. 当收到请求消息时，根据消息的action字段找到对应的处理器方法
3. 调用处理器方法处理请求，并返回响应

## 注意事项

- 处理方法必须接受一个`RequestMessage`参数，并返回`ResponseMessage`
- 处理方法应该处理可能的异常，确保能够返回有效的响应
- 如果没有找到对应的处理器，系统会返回一个404错误响应