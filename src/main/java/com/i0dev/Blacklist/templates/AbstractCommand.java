package com.i0dev.Blacklist.templates;

import com.i0dev.Blacklist.Heart;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public abstract class AbstractCommand extends AbstractManager implements SimpleCommand {

    // For tab completion
    protected List<String> blank = new ArrayList<>();

    protected List<String> players() {
        return heart.getServer().getAllPlayers().stream().map(Player::getUsername).collect(Collectors.toList());
    }

    String command;

    public AbstractCommand(Heart heart, String command) {
        super(heart);
        this.command = command;
    }


    @Override
    public void execute(Invocation invocation) {
        execute(invocation.source(), invocation.arguments());
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        if (invocation.arguments().length == 0)
            return suggest(invocation.source(), new String[]{""});
        return suggest(invocation.source(), invocation.arguments());
    }

    public abstract void execute(CommandSource sender, String[] args);

    public List<String> suggest(CommandSource sender, String[] args) {
        return null;
    }

    public List<String> tabCompleteHelper(String arg, Collection<String> options) {
        if (arg.equalsIgnoreCase("") || arg.equalsIgnoreCase(" "))
            return new ArrayList<>(options);
        else
            return options.stream().filter(s -> s.toLowerCase().startsWith(arg.toLowerCase())).collect(Collectors.toList());
    }
}