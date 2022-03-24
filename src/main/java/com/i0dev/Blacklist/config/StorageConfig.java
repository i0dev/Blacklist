package com.i0dev.Blacklist.config;

import com.i0dev.Blacklist.Heart;
import com.i0dev.Blacklist.objects.BlacklistRecord;
import com.i0dev.Blacklist.templates.AbstractConfiguration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class StorageConfig extends AbstractConfiguration {


    public StorageConfig(Heart heart, String path) {
        this.path = path;
        this.heart = heart;
    }

    List<BlacklistRecord> blacklisted = new ArrayList<>();


    public BlacklistRecord getRecord(String uuid) {
        return blacklisted.stream().filter(blacklistRecord -> blacklistRecord.getUuid().equalsIgnoreCase(uuid)).findAny().orElse(null);
    }

}

