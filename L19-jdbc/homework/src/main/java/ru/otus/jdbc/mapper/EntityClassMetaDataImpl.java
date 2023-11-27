package ru.otus.jdbc.mapper;

import ru.otus.crm.model.ID;
import ru.otus.crm.model.SQLTemplateOrder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T>{
    Class<T> clz;

    public EntityClassMetaDataImpl(Class<T> clz){
        this.clz = clz;
    }

    @Override
    public String getName() {
        return clz.getSimpleName().toLowerCase();
    }

    @Override
    public Constructor<T> getConstructor() {
        try {
            return clz.getConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();

        return null;}
    }

    @Override
    public Field getIdField() {
        for (Field field : clz.getDeclaredFields()) {
            if (field.isAnnotationPresent(ID.class)) {
                return field;
            }
        }
        return null;
    }

    @Override
    public List<Field> getAllFields() {
        return Stream.of(clz.getDeclaredFields())
                .sorted(Comparator.comparingInt(x -> x.getAnnotation(SQLTemplateOrder.class).position()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        List<Field> result = new ArrayList<>();
        for (Field field : clz.getDeclaredFields()){
            if (!field.isAnnotationPresent(ID.class)){
                result.add(field);
            }
        }
        return result.stream()
                .sorted(Comparator.comparingInt(x -> x.getAnnotation(SQLTemplateOrder.class).position()))
                .collect(Collectors.toList());
    }
}
