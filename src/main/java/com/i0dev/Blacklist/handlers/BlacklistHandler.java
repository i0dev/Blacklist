package com.i0dev.Blacklist.handlers;

import com.i0dev.Blacklist.Heart;
import com.i0dev.Blacklist.objects.BlacklistRecord;
import com.i0dev.Blacklist.templates.AbstractListener;
import com.i0dev.Blacklist.utility.MsgUtil;
import com.i0dev.Blacklist.utility.Utility;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;

public class BlacklistHandler extends AbstractListener {
    public BlacklistHandler(Heart heart) {
        super(heart);
    }

    @Subscribe
    public void onServerPreConnect(ServerPreConnectEvent e) {
        if (heart.blMgr().isBlacklisted(e.getPlayer().getUniqueId().toString())) {
            e.setResult(ServerPreConnectEvent.ServerResult.denied());
            BlacklistRecord record = heart.storage().getRecord(e.getPlayer().getUniqueId().toString());
            StringBuilder disconnectMessage = new StringBuilder();
            for (String s : heart.msg().getDisconnectMessage()) {
                disconnectMessage.append(heart.blMgr().replace(s, Utility.getIGNFromUUID(record.getExecutorUuid()), e.getPlayer().getUsername(), record.getReason(), record.getTimeStamp())).append("<newline>");
            }
            e.getPlayer().disconnect(MsgUtil.parse(disconnectMessage.toString()));
        }
    }
}
