package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class SwitchFields11And12Processor implements Processor {

    @Override
    public Message process(Message message) {
        String newField11Value = message.getField12();
        String newField12Value = message.getField11();
        return message.toBuilder().field11(newField11Value).field12(newField12Value).build();
    }
}
