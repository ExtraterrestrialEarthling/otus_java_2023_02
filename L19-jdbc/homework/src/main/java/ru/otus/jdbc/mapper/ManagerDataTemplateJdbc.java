package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.crm.model.Manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ManagerDataTemplateJdbc implements DataTemplate<Manager> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;

    public ManagerDataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
    }

    @Override
    public Optional<Manager> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return new Manager(rs.getLong("no"), rs.getString("label"), rs.getString("param1"));
                }
                return null;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<Manager> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            List<Manager> managerList = new ArrayList<>();
            try {
                while (rs.next()) {
                    managerList.add(new Manager(rs.getLong("no"),
                            rs.getString("label"), rs.getString("param1")));

                }
                return managerList;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }


    @Override
    public long insert(Connection connection, Manager manager) {
        try {
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(),
                    List.of(manager.getLabel(), manager.getParam1()));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }

    }

    @Override
    public void update(Connection connection, Manager manager) {
        try {
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(),
                    List.of(manager.getLabel(), manager.getParam1(), manager.getNo()));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }
}
