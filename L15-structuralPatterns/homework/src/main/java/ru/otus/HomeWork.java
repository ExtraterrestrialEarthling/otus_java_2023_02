package ru.otus;

import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.homework.EvenSecondDeterminer;
import ru.otus.processor.homework.EvenSecondExceptionProcessor;
import ru.otus.processor.homework.SwitchFields11And12Processor;

import java.util.ArrayList;
import java.util.List;

public class HomeWork {

    public static void main(String[] args) {

        var processors = List.of(new SwitchFields11And12Processor(),
                new EvenSecondExceptionProcessor(new EvenSecondDeterminer()));
        var complexProcessor = new ComplexProcessor(processors, exception -> {});
        var historyListener = new HistoryListener();
        complexProcessor.addListener(historyListener);

        var messageData = new ObjectForMessage();
        messageData.setData(new ArrayList<>());
        var message = new Message.Builder(1L).field1("field1")
                .field2("field2")
                .field3("field3")
                .field4("field4")
                .field5("field5")
                .field11("field11")
                .field12("field12")
                .field13(messageData)
                .build();

        var result = complexProcessor.handle(message);
        System.out.println("Result: " + result);
        complexProcessor.removeListener(historyListener);
    }

}
