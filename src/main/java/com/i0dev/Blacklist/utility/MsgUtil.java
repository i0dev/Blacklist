package com.i0dev.Blacklist.utility;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class MsgUtil {


    public static Component parse(String content) {
        return MiniMessage.miniMessage().deserialize(content);
    }

}
