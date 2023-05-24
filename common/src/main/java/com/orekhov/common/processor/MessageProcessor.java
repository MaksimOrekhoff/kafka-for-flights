package com.orekhov.common.processor;

import com.orekhov.common.messages.Message;

public interface MessageProcessor<T extends Message> {
    void process(String jsonMessage);
}
