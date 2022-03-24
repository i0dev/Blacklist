package com.i0dev.Blacklist;

import com.i0dev.Blacklist.commands.CmdBlacklist;
import com.i0dev.Blacklist.commands.CmdCheckBlacklist;
import com.i0dev.Blacklist.commands.CmdUnBlacklist;
import com.i0dev.Blacklist.config.GeneralConfig;
import com.i0dev.Blacklist.config.MessageConfig;
import com.i0dev.Blacklist.config.StorageConfig;
import com.i0dev.Blacklist.handlers.BlacklistHandler;
import com.i0dev.Blacklist.managers.BlacklistManager;
import com.i0dev.Blacklist.managers.GeneralManager;
import com.i0dev.Blacklist.templates.AbstractCommand;
import com.i0dev.Blacklist.templates.AbstractConfiguration;
import com.i0dev.Blacklist.templates.AbstractListener;
import com.i0dev.Blacklist.templates.AbstractManager;
import com.i0dev.Blacklist.utility.ConfigUtil;
import com.i0dev.Blacklist.utility.Utility;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Getter
@Plugin(id = "blacklist", name = "Blacklist", version = "1.0.0",
        description = "A lightweight blacklist plugin.", authors = {"i01"})
public class Heart {

    private final Path dataDirectory;

    public final ProxyServer server;
    public final Logger logger;

    List<AbstractManager> managers = new ArrayList<>();
    List<AbstractConfiguration> configs = new ArrayList<>();

    public File getDataFolder() {
        return dataDirectory.toFile();
    }


    @Inject
    public Heart(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        managers.addAll(Arrays.asList(
                new CmdBlacklist(this, "Blacklist"),
                new CmdUnBlacklist(this, "UnBlacklist"),
                new CmdCheckBlacklist(this, "CheckBlacklist"),
                new BlacklistManager(this),
                new GeneralManager(this),
                new BlacklistHandler(this)
        ));

        configs.addAll(Arrays.asList(
                new GeneralConfig(this, getDataFolder() + "/General.json"),
                new MessageConfig(this, getDataFolder() + "/Messages.json"),
                new StorageConfig(this, getDataFolder() + "/Storage.json")
        ));

        loadConfigs();
        registerManagers();
        // SQLUtil.establishConnection(this, false);

        System.out.println("\u001B[32m" + getClass().getAnnotation(Plugin.class).name() + " by: " + getClass().getAnnotation(Plugin.class).authors()[0] + " has been enabled.");
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        configs.clear();
        managers.forEach(AbstractManager::deinitialize);
        managers.clear();
        // Connection connection = SQLUtil.getConnection();
        // if (connection != null) connection.close();
        System.out.println("\u001B[31m" + getClass().getAnnotation(Plugin.class).name() + " by: " + getClass().getAnnotation(Plugin.class).authors()[0] + " has been disabled.");

    }

    public void loadConfigs() {
        // old ~ new
        ArrayList<Utility.Pair<AbstractConfiguration, AbstractConfiguration>> toReplace = new ArrayList<>();
        configs.forEach(abstractConfiguration -> toReplace.add(new Utility.Pair<>(abstractConfiguration, ConfigUtil.load(abstractConfiguration, this))));
        toReplace.forEach(pairs -> {
            configs.remove(pairs.getKey());
            configs.add(pairs.getValue());
        });
    }

    public void reloadPlugin() {
        onProxyShutdown(null);
        onProxyInitialization(null);
    }


    public <T> T getManager(Class<T> clazz) {
        return (T) managers.stream().filter(manager -> manager.getClass().equals(clazz)).findFirst().orElse(null);
    }

    public <T> T getConfig(Class<T> clazz) {
        return (T) configs.stream().filter(config -> config.getClass().equals(clazz)).findFirst().orElse(null);
    }

    public void registerManagers() {
        managers.forEach(abstractManager -> {
            if (abstractManager.isLoaded()) abstractManager.deinitialize();
            if (abstractManager instanceof AbstractListener)
                getServer().getEventManager().register(this, abstractManager);
            else if (abstractManager instanceof AbstractCommand) {
                getServer().getCommandManager().register(((AbstractCommand) abstractManager).getCommand(), (Command) abstractManager);
            }
            abstractManager.initialize();
            abstractManager.setLoaded(true);
        });
    }

    public GeneralConfig cnf() {
        return getConfig(GeneralConfig.class);
    }

    public MessageConfig msg() {
        return getConfig(MessageConfig.class);
    }

    public StorageConfig storage() {
        return getConfig(StorageConfig.class);
    }

    public GeneralManager genMgr() {
        return getManager(GeneralManager.class);
    }

    public BlacklistManager blMgr() {
        return getManager(BlacklistManager.class);
    }

}
