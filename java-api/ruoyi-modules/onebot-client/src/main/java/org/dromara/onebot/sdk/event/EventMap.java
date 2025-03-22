package org.dromara.onebot.sdk.event;

import org.dromara.onebot.sdk.event.message.GroupMessageEvent;
import org.dromara.onebot.sdk.event.message.GuildMessageEvent;
import org.dromara.onebot.sdk.event.message.PrivateMessageEvent;
import org.dromara.onebot.sdk.event.message.WholeMessageEvent;
import org.dromara.onebot.sdk.event.meta.HeartbeatMetaEvent;
import org.dromara.onebot.sdk.event.meta.LifecycleMetaEvent;
import org.dromara.onebot.sdk.event.notice.friend.FriendAddNoticeEvent;
import org.dromara.onebot.sdk.event.notice.friend.PrivateMsgDeleteNoticeEvent;
import org.dromara.onebot.sdk.event.notice.group.*;
import org.dromara.onebot.sdk.event.notice.guild.ChannelCreatedNoticeEvent;
import org.dromara.onebot.sdk.event.notice.guild.ChannelDestroyedNoticeEvent;
import org.dromara.onebot.sdk.event.notice.guild.ChannelUpdatedNoticeEvent;
import org.dromara.onebot.sdk.event.notice.guild.MessageReactionsUpdatedNoticeEvent;
import org.dromara.onebot.sdk.event.notice.misc.OtherClientStatusNoticeEvent;
import org.dromara.onebot.sdk.event.notice.misc.ReceiveOfflineFilesNoticeEvent;
import org.dromara.onebot.sdk.event.request.FriendAddRequestEvent;
import org.dromara.onebot.sdk.event.request.GroupAddRequestEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/9/14 17:04
 * Version: 1.0
 */
public class EventMap {
    public static Map<String, Class<? extends Event>> messageMap = new HashMap<>();

    static {
        messageMap.put("groupMessage", GroupMessageEvent.class);
        messageMap.put("privateMessage", PrivateMessageEvent.class);
        messageMap.put("wholeMessage", WholeMessageEvent.class);
        messageMap.put("guildMessage", GuildMessageEvent.class);

        messageMap.put("friend", FriendAddRequestEvent.class);
        messageMap.put("group", GroupAddRequestEvent.class);

        messageMap.put("group_upload", GroupUploadNoticeEvent.class);
        messageMap.put("group_admin", GroupAdminNoticeEvent.class);
        messageMap.put("group_decrease", GroupDecreaseNoticeEvent.class);
        messageMap.put("group_increase", GroupIncreaseNoticeEvent.class);
        messageMap.put("group_ban", GroupBanNoticeEvent.class);
        messageMap.put("group_recall", GroupMsgDeleteNoticeEvent.class);
        messageMap.put("poke", GroupPokeNoticeEvent.class);
        messageMap.put("lucky_king", GroupLuckyKingNoticeEvent.class);
        messageMap.put("honor", GroupHonorChangeNoticeEvent.class);
        messageMap.put("group_card", GroupCardChangeNoticeEvent.class);

        messageMap.put("friend_add", FriendAddNoticeEvent.class);
        messageMap.put("friend_recall", PrivateMsgDeleteNoticeEvent.class);
        messageMap.put("essence", GroupEssenceNoticeEvent.class);
        messageMap.put("offline_file", ReceiveOfflineFilesNoticeEvent.class);

        messageMap.put("lifecycle", LifecycleMetaEvent.class);
        messageMap.put("heartbeat", HeartbeatMetaEvent.class);

        messageMap.put("guild_channel_create", ChannelCreatedNoticeEvent.class);
        messageMap.put("guild_channel_destroy", ChannelDestroyedNoticeEvent.class);
        messageMap.put("guild_channel_update", ChannelUpdatedNoticeEvent.class);
        messageMap.put("guild_message_reactions_update", MessageReactionsUpdatedNoticeEvent.class);


        messageMap.put("other_client_status", OtherClientStatusNoticeEvent.class);
        messageMap.put("receive_offline_files", ReceiveOfflineFilesNoticeEvent.class);


    }
}
