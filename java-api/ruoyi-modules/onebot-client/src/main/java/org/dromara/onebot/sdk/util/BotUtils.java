package org.dromara.onebot.sdk.util;

import org.dromara.onebot.sdk.action.BaseBot;
import org.dromara.onebot.sdk.action.LagrangeExtend;
import org.dromara.onebot.sdk.action.OneBot;
import org.dromara.onebot.sdk.entity.ArrayMsg;
import org.dromara.onebot.sdk.enums.MsgType;
import org.dromara.onebot.sdk.event.message.MessageEvent;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import lombok.NonNull;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/9/14 13:48
 * Version: 1.0
 */
public class BotUtils {

    private static final Logger log = LoggerFactory.getLogger("BotUtils");
    private final static String CQ_CODE_SPLIT = "(?<=\\[CQ:[^]]{1,99999}])|(?=\\[CQ:[^]]{1,99999}])";

    private final static String CQ_CODE_REGEX = "\\[CQ:([^,\\[\\]]+)((?:,[^,=\\[\\]]+=[^,\\[\\]]*)*)]";

    /**
     * 判断是否为全体at
     *
     * @param msg 消息
     * @return 是否为全体at
     */
    public static boolean isAtAll(String msg) {
        return msg.contains("[CQ:at,qq=all]");
    }

    /**
     * 判断是否为全体at
     *
     * @param arrayMsg 消息链
     * @return 是否为全体at
     */
    public static boolean isAtAll(List<ArrayMsg> arrayMsg) {
        return arrayMsg.stream().anyMatch(it -> "all".equals(it.getData().get("qq")));
    }

    /**
     * 获取消息内所有at对象账号（不包含全体 at）
     *
     * @param arrayMsg 消息链
     * @return at对象列表
     */
    public static List<Long> getAtList(List<ArrayMsg> arrayMsg) {
        return arrayMsg
                .stream()
                .filter(it -> MsgType.at == it.getType() && !"all".equals(it.getData().get("qq")))
                .map(it -> Long.parseLong(it.getData().get("qq")))
                .collect(Collectors.toList());
    }

    /**
     * 获取消息内所有图片链接
     *
     * @param arrayMsg 消息链
     * @return 图片链接列表
     */
    public static List<String> getMsgImgUrlList(List<ArrayMsg> arrayMsg) {
        return arrayMsg
                .stream()
                .filter(it -> MsgType.image == it.getType()).map(it -> it.getData().get("url"))
                .collect(Collectors.toList());
    }

    /**
     * 获取消息内所有视频链接
     *
     * @param arrayMsg 消息链
     * @return 视频链接列表
     */
    public static List<String> getMsgVideoUrlList(List<ArrayMsg> arrayMsg) {
        return arrayMsg
                .stream()
                .filter(it -> MsgType.video == it.getType()).map(it -> it.getData().get("url"))
                .collect(Collectors.toList());
    }

    /**
     * 获取群头像
     *
     * @param groupId 群号
     * @param size    头像尺寸
     * @return 头像链接 （size为0返回真实大小, 40(40*40), 100(100*100), 640(640*640)）
     */
    public static String getGroupAvatar(long groupId, int size) {
        return String.format("https://p.qlogo.cn/gh/%s/%s/%s", groupId, groupId, size);
    }

    /**
     * 获取用户昵称
     *
     * @param userId QQ号
     * @return 用户昵称
     */
    @Deprecated
    public static String getNickname(long userId) {
        val url = String.format("https://r.qzone.qq.com/fcg-bin/cgi_get_portrait.fcg?uins=%s", userId);
        val result = NetUtils.get(url, "GBK");
        if (result != null && !result.isEmpty()) {
            String nickname = result.split(",")[6];
            return nickname.substring(1, nickname.length() - 1);
        }
        return "";
    }

    /**
     * 获取用户头像
     *
     * @param userId QQ号
     * @param size   头像尺寸
     * @return 头像链接 （size为0返回真实大小, 40(40*40), 100(100*100), 640(640*640)）
     */
    public static String getUserAvatar(long userId, int size) {
        return String.format("https://q1.qlogo.cn/g?b=qq&nk=%s&s=%s", userId, size);
    }

    /**
     * 消息解码
     *
     * @param string 需要解码的内容
     * @return 解码处理后的字符串
     */
    public static String unescape(String string) {
        return string.replace("&#44;", ",").replace("&#91;", "[").replace("&#93;", "]").replace("&amp;", "&");
    }

    /**
     * 消息编码
     *
     * @param string 需要编码的内容
     * @return 编码处理后的字符串
     */
    public static String escape(String string) {
        return string.replace("&", "&amp;").replace(",", "&#44;").replace("[", "&#91;").replace("]", "&#93;");
    }

    /**
     * 消息编码（可用于转义CQ码，防止文本注入）
     *
     * @param string 需要编码的内容
     * @return 编码处理后的字符串
     */
    public static String escape2(String string) {
        return string.replace("[", "&#91;").replace("]", "&#93;");
    }

    /**
     * string 消息上报转消息链
     * 建议传入 event.getMessage 而非 event.getRawMessage
     * 例如 go-cq-http rawMessage 不包含图片 url
     *
     * @param msg 需要修改客户端消息上报类型为 string
     * @return 消息链
     */
    public static List<ArrayMsg> rawToArrayMsg(@NonNull String msg) {
        List<ArrayMsg> chain = new ArrayList<>();
        try {
            Arrays.stream(msg.split(CQ_CODE_SPLIT)).filter(s -> !s.isEmpty()).forEach(s -> {
                Optional<Matcher> matcher = Optional.ofNullable(RegexUtils.regexMatcher(CQ_CODE_REGEX, s));
                ArrayMsg item = new ArrayMsg();
                Map<String, String> data = new HashMap<>();
                if (!matcher.isPresent()) {
                    item.setType(MsgType.text);
                    data.put("text", unescape(s));
                    item.setData(data);
                }
                if (matcher.isPresent()) {
                    MsgType type = MsgType.typeOf(matcher.get().group(1));
                    String[] params = matcher.get().group(2).split(",");
                    item.setType(type);
                    Arrays.stream(params).filter(args -> !args.isEmpty()).forEach(args -> {
                        String k = args.substring(0, args.indexOf("="));
                        String v = unescape(args.substring(args.indexOf("=") + 1));
                        data.put(k, v);
                    });
                    item.setData(data);
                }
                chain.add(item);
            });
        } catch (Exception e) {
            log.error("Conversion failed: {}", e.getMessage());
        }
        return chain;
    }


    /**
     * string 消息上报转消息链
     * 建议传入 event.getMessage 而非 event.getRawMessage
     * 例如 go-cq-http rawMessage 不包含图片 url
     *
     * @param msg 需要修改客户端消息上报类型为 string
     * @return 消息链
     */
    public static JsonArray rawToJson(String msg) {
        val array = new JsonArray();
        try {
            Arrays.stream(msg.split(CQ_CODE_SPLIT)).filter(s -> !s.isEmpty()).forEach(s -> {
                Optional<Matcher> matcher = Optional.ofNullable(RegexUtils.regexMatcher(CQ_CODE_REGEX, s));
                val object = new JsonObject();
                val params = new JsonObject();
                if (!matcher.isPresent()) {
                    object.addProperty("type", "text");
                    params.addProperty("text", s);
                    object.add("data", params);
                }
                if (matcher.isPresent()) {
                    object.addProperty("type", matcher.get().group(1));
                    String[] rawParams = matcher.get().group(2).split(",");
                    Arrays.stream(rawParams).filter(args -> !args.isEmpty()).forEach(args -> {
                        val k = args.substring(0, args.indexOf("="));
                        val v = unescape(args.substring(args.indexOf("=") + 1));
                        params.addProperty(k, v);
                    });
                    object.add("data", params);
                }
                array.add(object);
            });
        } catch (Exception e) {
            log.error("Conversion failed: {}", e.getMessage());
        }
        return array;
    }


    /**
     * 从 ArrayMsg 生成 CQ Code
     *
     * @param arrayMsg {@link ArrayMsg}
     * @return CQ Code
     */
    public static String arrayMsgToCode(ArrayMsg arrayMsg) {
        StringBuilder builder = new StringBuilder();
        if (Objects.isNull(arrayMsg.getType()) || MsgType.unknown.equals(arrayMsg.getType())) {
            builder.append("[CQ:").append(MsgType.unknown);
        } else {
            builder.append("[CQ:").append(arrayMsg.getType());
        }
        arrayMsg.getData().forEach((k, v) -> builder.append(",").append(k).append("=").append(escape(v)));
        builder.append("]");
        return builder.toString();
    }


    /**
     * 从 List<ArrayMsg> 生成 CQ Code
     *
     * @param arrayMsgs {@link ArrayMsg}
     * @return CQ Code
     */
    @SuppressWarnings("SpellCheckingInspection")
    public static String arrayMsgToCode(List<ArrayMsg> arrayMsgs) {
        StringBuilder builder = new StringBuilder();
        for (ArrayMsg item : arrayMsgs) {
            if (!MsgType.text.equals(item.getType())) {
                builder.append("[CQ:").append(item.getType());
                item.getData().forEach((k, v) -> builder.append(",").append(k).append("=").append(escape(v)));
                builder.append("]");
            } else {
                builder.append(escape(item.getData().get(MsgType.text.toString())));
            }
        }
        return builder.toString();
    }


    /**
     * 从 msg 转换
     *
     * @param rawJson {@link JsonObject}
     * @param event {@link MessageEvent}
     */
    public static void rawConvert(JsonObject rawJson, MessageEvent event) {
        // 支持 array 格式消息上报，如果 msg 是一个有效的 json 字符串则作为 array 上报
        if (rawJson.has("message")) {
            val message = GsonUtils.getAsString(rawJson, "message");
            if (GsonUtils.isArrayNode(rawJson, "message")) {
                List<ArrayMsg> arrayMsg = GsonUtils.convertToList(message, ArrayMsg.class);
                // 存入 array message
                event.setArrayMsg(arrayMsg);
                // 将 array message 转换回 string message
                event.setMessage(GsonUtils.getAsJsonArray(rawJson, "message").toString());
                // 解析cq消息
                if (rawJson.has("raw_message") && (event.getRawMessage() == null || event.getRawMessage().isEmpty())){
                    event.setRawMessage(arrayMsgToCode(arrayMsg));
                }
            } else {
                event.setArrayMsg(rawToArrayMsg(message));
                if (rawJson.has("raw_message") && (event.getRawMessage() == null || event.getRawMessage().isEmpty())){
                    event.setRawMessage(message);
                }
            }

        }
    }

    /**
     * 创建自定义消息合并转发
     *
     * @param uin      发送者QQ号
     * @param name     发送者显示名字
     * @param contents 消息列表，每个元素视为一个消息节点
     *                 <a href="https://docs.go-cqhttp.org/cqcode/#%E5%90%88%E5%B9%B6%E8%BD%AC%E5%8F%91">参考文档</a>
     * @return 消息结构
     */
    @SuppressWarnings("Duplicates")
    public static List<Map<String, Object>> generateForwardMsg(long uin, String name, List<String> contents) {
        List<Map<String, Object>> nodes = new ArrayList<>();
        contents.forEach(msg -> {
            Map<String, Object> node = new HashMap<>();
            node.put("type", "node");
            Map<String, Object> data = new HashMap<>();
            data.put("name", name);
            data.put("uin", uin);
            data.put("content", msg);
            node.put("data", data);
            nodes.add(node);
        });
        return nodes;
    }

    /**
     * 兼容 Shamrock
     * 生成自定义合并转发消息
     *
     * @param contents 消息列表，每个元素视为一个消息节点
     * @return 消息结构
     */
    @SuppressWarnings("Duplicates")
    public static List<Map<String, Object>> generateForwardMsg(List<String> contents) {
        List<Map<String, Object>> nodes = new ArrayList<>();
        contents.forEach(msg -> {
            Map<String, Object> node = new HashMap<>();
            node.put("type", "node");
            Map<String, Object> data = new HashMap<>();
            data.put("content", msg);
            node.put("data", data);
            nodes.add(node);
        });
        return nodes;
    }

    /**
     * 兼容 Lagrange
     * 生成自定义合并转发消息
     *
     * @param contents 消息列表，每个元素视为一个消息节点 Object 可为 List<ArrayMsg> 或 CQCode
     * @return 消息结构
     */
    @SuppressWarnings("Duplicates")
    public static List<Map<String, Object>> generateForwardMsg(String uin, String name, List<?> contents) {
        List<Map<String, Object>> nodes = new ArrayList<>();
        contents.forEach(msg -> {
            Map<String, Object> node = new HashMap<>();
            node.put("type", "node");
            Map<String, Object> data = new HashMap<>();
            data.put("name", name);
            data.put("uin", uin);
            data.put("content", msg);
            node.put("data", data);
            nodes.add(node);
        });
        return nodes;
    }

    /**
     * 兼容 Lagrange
     * 生成自定义合并转发消息
     *
     * @param contents 消息列表，每个元素视为一个消息节点 Object 可为 List<ArrayMsg> 或 CQCode
     * @return 消息结构
     */
    @SuppressWarnings("Duplicates")
    public static List<Map<String, Object>> generateForwardMsg(BaseBot bot, List<?> contents) {
        List<Map<String, Object>> nodes = new ArrayList<>();
        contents.forEach(msg -> {
            Map<String, Object> node = new HashMap<>();
            node.put("type", "node");
            Map<String, Object> data = new HashMap<>();
            data.put("name", bot.getLoginInfo().getData().getNickname());
            String appName = bot.getVersionInfo().getData().getAppName();
            // 兼容Lagrange
            if (appName.equals("Lagrange.OneBot")) {
                data.put("uin", String.valueOf(bot.getSelfId()));
            } else {
                data.put("uin", bot.getSelfId());
            }
            data.put("content", msg);
            node.put("data", data);
            nodes.add(node);
        });
        return nodes;
    }

    /**
     * 兼容 Shamrock
     * 生成引用消息和自定义消息混合合并转发
     *
     * @param contents   消息列表，每个元素视为一个消息节点
     * @param quoteMsgId 引用的消息ID
     * @return 消息结构
     */
    public static List<Map<String, Object>> generateForwardMsg(List<String> contents, List<String> quoteMsgId) {
        List<Map<String, Object>> nodes = generateForwardMsg(contents);
        quoteMsgId.forEach(id -> {
            Map<String, Object> node = new HashMap<>();
            node.put("type", "node");
            Map<String, Object> data = new HashMap<>();
            data.put("id", id);
            node.put("data", data);
            nodes.add(node);
        });
        return nodes;
    }
}
