package com.i0dev.Blacklist.objects;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class BlacklistRecord {

    String uuid;
    String executorUuid;
    String reason;
    String ip;
    long timeStamp;

}
