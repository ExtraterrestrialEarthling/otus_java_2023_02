package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.util.function.Consumer;

public class EvenSecondExceptionProcessor implements Processor {
    private EvenNumberDeterminer determiner;

    public EvenSecondExceptionProcessor(EvenNumberDeterminer determiner) {
        this.determiner = determiner;
    }

    public Message process(Message message) {
        if (determiner.isEven()) {
            throw new RuntimeException("Четная секунда!");
        }
        return message;
    }

}
