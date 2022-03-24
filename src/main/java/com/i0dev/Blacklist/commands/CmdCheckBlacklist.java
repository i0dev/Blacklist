package com.i0dev.Blacklist.commands;

import com.i0dev.Blacklist.Heart;
import com.i0dev.Blacklist.config.GeneralConfig;
import com.i0dev.Blacklist.config.MessageConfig;
import com.i0dev.Blacklist.objects.BlacklistRecord;
import com.i0dev.Blacklist.templates.AbstractCommand;
import com.i0dev.Blacklist.utility.MsgUtil;
import com.i0dev.Blacklist.utility.Utility;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;

import java.util.List;

public class CmdCheckBlacklist extends AbstractCommand {

    MessageConfig msg;
    GeneralConfig config;

    public CmdCheckBlacklist(Heart heart, String command) {
        super(heart, command);
    }

    @Override
    public void initialize() {
        msg = getHeart().getConfig(MessageConfig.class);
        config = getHeart().getConfig(GeneralConfig.class);
    }

    @Override
    public void deinitialize() {
        msg = null;
        config = null;

    }

    @Override
    public void execute(CommandSource sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(MsgUtil.parse(msg.getCheckBlacklistUsage()));
            return;
        }
        String uuid = Utility.getUUIDFromIGNAPI(args[0]);

        if (uuid == null) {
            sender.sendMessage(MsgUtil.parse(msg.getCantFindPlayer()));
            return;
        }

        if (heart.blMgr().isBlacklisted(uuid)) {
            BlacklistRecord record = heart.storage().getRecord(uuid);

            StringBuilder msg = new StringBuilder();
            for (int i = 0; i < heart.msg().getBlacklistedCheck().size(); i++) {
                msg.append(heart.blMgr().replace(heart.msg().getBlacklistedCheck().get(i), Utility.getIGNFromUUID(record.getExecutorUuid()), args[0], record.getReason(), record.getTimeStamp()));
                if (i < heart.msg().getBlacklistedCheck().size() - 1) msg.append("<newline>");
            }
            sender.sendMessage(MsgUtil.parse(msg.toString()));

        } else {
            StringBuilder msg = new StringBuilder();
            for (int i = 0; i < heart.msg().getNotBlacklistedCheck().size(); i++) {
                msg.append(heart.blMgr().replace(heart.msg().getNotBlacklistedCheck().get(i), "", args[0], "", System.currentTimeMillis()));
                if (i < heart.msg().getNotBlacklistedCheck().size() - 1) msg.append("<newline>");
            }
            sender.sendMessage(MsgUtil.parse(msg.toString()));
        }
    }

    @Override
    public List<String> suggest(CommandSource sender, String[] args) {
        if (args.length == 1)
            return tabCompleteHelper(args[0], players());
        return blank;
    }
}
