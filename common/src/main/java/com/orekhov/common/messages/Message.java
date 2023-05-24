package com.orekhov.common.messages;

import com.orekhov.common.bean.Source;
import com.orekhov.common.bean.Type;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Message {
    protected Type type;
    protected Source source;

    public String getCode() {
        return source.name() + "_" + type.name();
    }
}
