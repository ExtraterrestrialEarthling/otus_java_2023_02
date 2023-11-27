package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {
    EntityClassMetaData<T> classMetaData;


    public EntitySQLMetaDataImpl(EntityClassMetaData<T> classMetaData) {
        this.classMetaData = classMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return "SELECT * FROM " + classMetaData.getName();
    }

    @Override
    public String getSelectByIdSql() {
        return "SELECT * FROM " + classMetaData.getName() + " WHERE " + classMetaData.getIdField().getName() + " = ?";
    }

    @Override
    public String getInsertSql() {
        return "INSERT INTO " + classMetaData.getName() + " (" + getInsertColumns() + ") " +
                "VALUES (" + getValuePlaceHolders() + ")";
    }

    @Override
    public String getUpdateSql() {
        return "UPDATE " + classMetaData.getName() + "SET " + getUpdateColumns() + " WHERE " + classMetaData.getIdField() + " = ?";
    }

    private String getInsertColumns() {
        StringBuilder sbColumns = new StringBuilder();
        List<Field> fields = classMetaData.getFieldsWithoutId();
        for (int i = 0; i < fields.size(); i++) {
            sbColumns.append(fields.get(i).getName());

            if (i < fields.size() - 1) {
                sbColumns.append(", ");
            }
        }
        return sbColumns.toString();
    }

    private String getValuePlaceHolders(){
        StringBuilder sbPlaceHolders = new StringBuilder();
        List<Field> fields = classMetaData.getFieldsWithoutId();
        for (int i = 0; i < fields.size(); i++) {
            sbPlaceHolders.append("?");

            if (i < fields.size() - 1) {
                sbPlaceHolders.append(", ");
            }
        }
        return sbPlaceHolders.toString();
    }

    private String getUpdateColumns(){
        StringBuilder sbColumns = new StringBuilder();
        List<Field> fields = classMetaData.getFieldsWithoutId();
        for (int i = 0; i < fields.size(); i++) {
            sbColumns.append(fields.get(i).getName()).append(" = ?");

            if (i < fields.size() - 1) {
                sbColumns.append(", ");
            }
        }
        return sbColumns.toString();
    }
}
