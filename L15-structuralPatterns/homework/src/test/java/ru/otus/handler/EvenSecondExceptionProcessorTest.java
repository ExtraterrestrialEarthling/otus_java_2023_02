package ru.otus.handler;

import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.processor.homework.EvenNumberDeterminer;
import ru.otus.processor.homework.EvenSecondDeterminer;
import ru.otus.processor.homework.EvenSecondExceptionProcessor;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EvenSecondExceptionProcessorTest {

    @Test
    void throwExceptionTest(){
        EvenNumberDeterminer determiner = mock(EvenSecondDeterminer.class);
        when(determiner.isEven()).thenReturn(true);
        EvenSecondExceptionProcessor processor = new EvenSecondExceptionProcessor(determiner);
        Message message = new Message.Builder(1L).field1("field1").build();
        assertThrows(Exception.class, ()-> processor.process(message));
    }
}
