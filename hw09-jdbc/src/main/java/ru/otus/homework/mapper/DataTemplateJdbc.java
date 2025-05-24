package ru.otus.homework.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import lombok.SneakyThrows;
import ru.otus.homework.core.DataTemplate;
import ru.otus.homework.core.DbExecutor;

/** Сохратяет объект в базу, читает объект из базы */
@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {
    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<?> entityClassMetaData;
    private final Constructor<?> constructor;

    public DataTemplateJdbc(
            DbExecutor dbExecutor, EntitySQLMetaData sqlMetaData, EntityClassMetaData<?> classMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = sqlMetaData;
        this.entityClassMetaData = classMetaData;
        this.constructor = classMetaData.getConstructor();
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(
                connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), this::convertToOptional);
    }

    @SneakyThrows
    @SuppressWarnings("All")
    private T convertToOptional(ResultSet rs) {
        List<Field> fields = entityClassMetaData.getAllFields();
        if (!rs.next()) return null;
        int columnCount = rs.getMetaData().getColumnCount();
        List<Object> values = getValuesForEntity(rs, columnCount);
        T client = getClient(fields, values);
        return client;
    }

    private List<Object> getValuesForEntity(ResultSet rs, int columnCount) throws SQLException {
        List<Object> values = new ArrayList<>();
        for (int k = 1; k <= columnCount; k++) {
            values.add(rs.getObject(k));
        }
        return values;
    }

    @SuppressWarnings("All")
    private T getClient(List<Field> fields, List<Object> values)
            throws InstantiationException, IllegalAccessException, InvocationTargetException {
        T client = (T) constructor.newInstance();
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            field.setAccessible(true);
            Object value = values.get(i);
            field.set(client, value);
        }
        return client;
    }
    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor
                .executeSelect(connection, entitySQLMetaData.getSelectAllSql(), List.of(), this::convertToList)
                .orElseThrow(() -> new RuntimeException("No records found"));
    }

    @SneakyThrows
    private List<T> convertToList(ResultSet rs) {
        List<T> result = new ArrayList<>();
        List<Field> fields = entityClassMetaData.getAllFields();
        int columnCount = rs.getMetaData().getColumnCount();
        while (rs.next()) {
            List<Object> values = getValuesForEntity(rs, columnCount);
            T client = getClient(fields, values);
            result.add(client);
        }
        return result;
    }

    @Override
    public long insert(Connection connection, T client) {
        List<Field> fields = entityClassMetaData.getFieldsWithoutId();
        List<Object> values = getFieldValues(fields, client);
        return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), values);
    }

    @SneakyThrows
    private List<Object> getFieldValues(List<Field> fields, T client) {
        List<Object> values = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            values.add(field.get(client));
        }
        return values;
    }

    @Override
    public void update(Connection connection, T client) {
        List<Field> fields = entityClassMetaData.getAllFields();
        List<Object> values = getFieldValues(fields, client);
        Object idValue = values.removeFirst();
        values.add(idValue);
        dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), values);
    }
}
