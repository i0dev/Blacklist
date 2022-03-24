package com.i0dev.Blacklist.templates;

import com.i0dev.Blacklist.Heart;
import lombok.Getter;

@Getter
public abstract class AbstractListener extends AbstractManager  {
    public AbstractListener(Heart heart) {
        super(heart);
    }
}