package com.i0dev.Blacklist.managers;

import com.i0dev.Blacklist.Heart;
import com.i0dev.Blacklist.templates.AbstractManager;
import com.velocitypowered.api.proxy.Player;

import java.util.UUID;

public class GeneralManager extends AbstractManager {
    public GeneralManager(Heart heart) {
        super(heart);
    }

    public Player getPlayer(String s) {
        if (s.length() > 20)
            return heart.getServer().getPlayer(UUID.fromString(s)).orElse(null);
        return heart.getServer().getPlayer(s).orElse(null);
    }
}
