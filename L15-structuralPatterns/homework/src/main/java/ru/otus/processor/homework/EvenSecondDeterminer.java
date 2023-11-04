package ru.otus.processor.homework;

public class EvenSecondDeterminer implements EvenNumberDeterminer {
    @Override
    public boolean isEven() {
        return (System.currentTimeMillis()/1000)%2==0;
    }
}
