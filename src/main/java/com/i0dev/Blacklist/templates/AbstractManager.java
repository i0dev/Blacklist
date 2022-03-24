package com.i0dev.Blacklist.templates;


import com.i0dev.Blacklist.Heart;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbstractManager {

    public Heart heart;
    public boolean loaded = false;

    public void initialize() {

    }

    public void deinitialize() {

    }

    public AbstractManager(Heart heart) {
        this.heart = heart;
    }
}
