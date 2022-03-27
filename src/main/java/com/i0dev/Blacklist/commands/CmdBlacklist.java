package com.i0dev.Blacklist.commands;

import com.i0dev.Blacklist.Heart;
import com.i0dev.Blacklist.config.GeneralConfig;
import com.i0dev.Blacklist.config.MessageConfig;
import com.i0dev.Blacklist.templates.AbstractCommand;
import com.i0dev.Blacklist.utility.MsgUtil;
import com.i0dev.Blacklist.utility.Utility;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;

import java.util.List;

public class CmdBlacklist extends AbstractCommand {

    MessageConfig msg;
    GeneralConfig config;

    public CmdBlacklist(Heart heart, String command) {
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
            sender.sendMessage(MsgUtil.parse(msg.getHelpPageHeader()));
            sender.sendMessage(MsgUtil.parse(msg.getReloadUsage()));
            sender.sendMessage(MsgUtil.parse(msg.getBlacklistUsage()));
            sender.sendMessage(MsgUtil.parse(msg.getCheckBlacklistUsage()));
            sender.sendMessage(MsgUtil.parse(msg.getUnBlacklistUsage()));
            return;
        }
        String uuid = Utility.getUUIDFromIGNAPI(args[0]);

        if (uuid == null) {
            sender.sendMessage(MsgUtil.parse(msg.getCantFindPlayer()));
            return;
        }

        uuid = java.util.UUID.fromString(uuid.replaceFirst("(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5")).toString();

        if (heart.blMgr().isBlacklisted(uuid)) {
            sender.sendMessage(MsgUtil.parse(msg.getPlayerAlreadyBlacklisted().replace("{player}", args[0])));
            return;
        }

        boolean silent;

        StringBuilder reasonBuilder = new StringBuilder();
        if (args.length > 2) {
            for (int i = 1; i < args.length; i++) {
                reasonBuilder.append(args[i]).append(" ");
            }
            reasonBuilder = new StringBuilder(reasonBuilder.substring(0, reasonBuilder.length() - 1));
        }
        String reason = reasonBuilder.toString();
        silent = reason.contains(" -s");
        reason = reason.replace(" -s", "");
        String staffName = sender instanceof ConsoleCommandSource ? "Console" : ((Player) sender).getUsername();

        StringBuilder disconnectMessage = new StringBuilder();
        for (String s : msg.getDisconnectMessage()) {
            disconnectMessage.append(heart.blMgr().replace(s, staffName, args[0], reason, System.currentTimeMillis())).append("<newline>");
        }
        Player p = heart.getServer().getPlayer(args[0]).orElse(null);
        String ip = "";
        if (p != null) {
            ip = p.getRemoteAddress().getAddress().getHostAddress();
            p.disconnect(MsgUtil.parse(disconnectMessage.toString()));
        }
        heart.blMgr().blacklistPlayer(uuid, sender, reason, ip);
        if ("".equalsIgnoreCase(reason)) {
            reason = "No reason provided";
        }
        StringBuilder announcement = new StringBuilder();
        if (silent) announcement.append("<dark_red>[Silent]<newline>");
        for (int i = 0; i < msg.getAnnounceMessage().size(); i++) {
            announcement.append(heart.blMgr().replace(msg.getAnnounceMessage().get(i), staffName, args[0], reason, System.currentTimeMillis()));
            if (i < msg.getAnnounceMessage().size() - 1) announcement.append("<newline>");
        }

        if (silent) {
            for (Player allPlayer : heart.getServer().getAllPlayers()) {
                if (allPlayer.hasPermission("blacklist.silent.bypass"))
                    allPlayer.sendMessage(MsgUtil.parse(announcement.toString()));
            }
        } else
            heart.getServer().sendMessage(MsgUtil.parse(announcement.toString()));
    }


    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("blacklist.command.blacklist");
    }

    @Override
    public List<String> suggest(CommandSource sender, String[] args) {
        if (args.length == 1)
            return tabCompleteHelper(args[0], players());
        return blank;
    }
}
