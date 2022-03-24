package com.i0dev.Blacklist.managers;

import com.i0dev.Blacklist.Heart;
import com.i0dev.Blacklist.config.StorageConfig;
import com.i0dev.Blacklist.objects.BlacklistRecord;
import com.i0dev.Blacklist.templates.AbstractManager;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class BlacklistManager extends AbstractManager {
    public BlacklistManager(Heart heart) {
        super(heart);
    }


    StorageConfig storage;

    @Override
    public void initialize() {
        storage = heart.storage();
    }

    @Override
    public void deinitialize() {
        storage = null;
    }

    public void blacklistPlayer(String player, CommandSource sender, String reason, String ip) {

        BlacklistRecord record = new BlacklistRecord();
        record.setReason(reason);
        record.setIp(ip);
        record.setUuid(player);
        record.setTimeStamp(System.currentTimeMillis());
        record.setExecutorUuid(sender instanceof ConsoleCommandSource ? "Console" : ((Player) sender).getUniqueId().toString());

        storage.getBlacklisted().add(record);
        storage.save();
    }


    public void unBlacklistPlayer(String uuid) {
        storage.getBlacklisted().remove(storage.getRecord(uuid));
        storage.save();
    }


    public boolean isBlacklisted(String UUID) {
        return storage.getBlacklisted().stream().filter(blacklistRecord -> blacklistRecord.getUuid().equalsIgnoreCase(UUID)).findAny().orElse(null) != null;
    }

    public String replace(String s, String staff, String player, String reason, long time) {
        return s
                .replace("{staff}", staff)
                .replace("{player}", player)
                .replace("{date}", formatDate(time))
                .replace("{reason}", reason);
    }

    public String formatDate(long timeMillis) {
        ZonedDateTime time = ZonedDateTime.ofInstant(Instant.ofEpochMilli(timeMillis), ZoneId.of("America/New_York"));
        String Month = time.getMonth().getValue() + "";
        String Day = time.getDayOfMonth() + "";
        String Year = time.getYear() + "";
        String Hour = time.getHour() + "";
        String Minute = time.getMinute() + "";
        String Second = time.getSecond() + "";

        return Month + "/" + Day + "/" + Year + " " + (Hour.length() == 1 ? "0" + Hour : Hour) + ":" + (Minute.length() == 1 ? "0" + Minute : Minute) + " EST";
    }

}
