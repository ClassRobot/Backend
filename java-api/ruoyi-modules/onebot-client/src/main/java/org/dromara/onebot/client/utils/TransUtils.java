package org.dromara.onebot.client.utils;

import org.dromara.onebot.sdk.util.GsonUtils;
import com.google.gson.JsonObject;

import static org.dromara.onebot.client.instances.event.MsgHandlerImpl.META_EVENT;

/**
 * Name: onebot-client / TransUtils
 * Author: cnlimiter
 * CreateTime: 2023/11/22 11:24
 * Description:
 */

public class TransUtils {

    public static JsonObject toArray(JsonObject json){
        if (json.has(META_EVENT)) return json;
        if (json.has("message") && GsonUtils.isArrayNode(json, "message")){
            json.addProperty("message", GsonUtils.getAsJsonArray(json, "message").toString());
        }
        return json;
    }
}
