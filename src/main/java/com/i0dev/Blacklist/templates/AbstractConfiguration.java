package com.i0dev.Blacklist.templates;

import com.i0dev.Blacklist.Heart;
import com.i0dev.Blacklist.utility.ConfigUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbstractConfiguration {

    public transient Heart heart = null;
    public transient String path = "";


    public void save() {
        ConfigUtil.save(this);
    }
}
