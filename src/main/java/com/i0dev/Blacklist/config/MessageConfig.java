package com.i0dev.Blacklist.config;

import com.i0dev.Blacklist.Heart;
import com.i0dev.Blacklist.templates.AbstractConfiguration;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.units.qual.A;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MessageConfig extends AbstractConfiguration {


    String helpPageHeader = "<gray><u>_______<reset><dark_grey>[<reset> <red><b>Blacklist <dark_grey>]<u>_______";
    String reloadUsage = " <red>* <gray>/BlacklistReload";
    String blacklistUsage = " <red>* <gray>/Blacklist <player> [reason] [-s]";
    String unBlacklistUsage = " <red>* <gray>/UnBlacklist <player> [-s]";
    String checkBlacklistUsage = " <red>* <gray>/CheckBlacklist <player>";

    String reloadedConfig = "<gray>You have <green>reloaded <gray>the configuration.";
    String noPermission = "<red>You don not have permission to run that command.";
    String cantFindPlayer = "<red>Can't find player.";
    String invalidNumber = "<red>Invalid number.";
    String noConsole = "<red>You cannot run this command from console.";

    String playerAlreadyBlacklisted = "<red>{player} <gray>is already blacklisted";
    String playerNotBlacklisted = "<red>{player} <gray>is not currently blacklisted";

    List<String> disconnectMessage = Arrays.asList(
            "<dark_gray><st>+------<reset><dark_gray>[<dark_red><b>Blacklisted<reset><dark_gray>]<st>------+<reset>",
            "",
            "<dark_gray><b>» <gray>Blacklisted on: <white>{date}<reset>",
            "<dark_gray><b>» <gray>Blacklisted by: <red><i>{staff}<reset>",
            "<dark_gray><b>» <gray>Reason: <white>{reason}<reset>",
            "",
            "<dark_gray><b>» <gray>Appeal at: <white><u><i>discord.mcrivals.com<reset>",
            "",
            "<dark_gray><st>+----------------------------+"
    );

    List<String> announceMessage = Arrays.asList(
            "<dark_gray><st>+------<reset><dark_gray>[<dark_red><b>Blacklist<dark_gray>]<st>------+<reset>",
            "<dark_gray><b>» <gray>Staff: <white><i>{staff}<reset>",
            "<dark_gray><b>» <gray>Player: <red>{player}<reset>",
            "<dark_gray><b>» <gray>Server: <white>Global<reset>",
            "<dark_gray><b>» <gray>Reason: <white>{reason}<reset>",
            "<dark_gray><st>+---------------------+"
    );

    List<String> unBlacklistAnnounceMessage = Arrays.asList(
            "<dark_gray><st>+------<reset><dark_gray>[<green><b>Un-Blacklist<dark_gray>]<st>------+<reset>",
            "<dark_gray><b>» <gray>Staff: <white><i>{staff}<reset>",
            "<dark_gray><b>» <gray>Player: <red>{player}<reset>",
            "<dark_gray><st>+------------------------+"
    );

    List<String> notBlacklistedCheck = Arrays.asList(
            "<red>{player} <gray>is not currently blacklisted."
    );
    List<String> blacklistedCheck = Arrays.asList(
            "<green>{player} <gray>is currently blacklisted!",
            "<dark_gray><b>» <gray>Staff: <red>{staff}",
            "<dark_gray><b>» <gray>Reason: <red>{reason}",
            "<dark_gray><b>» <gray>Date: <red>{date}"
    );


    public MessageConfig(Heart heart, String path) {
        this.path = path;
        this.heart = heart;
    }
}
