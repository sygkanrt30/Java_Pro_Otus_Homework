package ru.otus.homework.mapper;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    private final String tableName;
    private final String idFieldName;
    private final List<String> withoutIdFieldNames;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.tableName = entityClassMetaData.getName().toLowerCase();
        this.idFieldName = entityClassMetaData.getIdField().getName();
        this.withoutIdFieldNames = entityClassMetaData.getFieldsWithoutId().stream()
                .map(Field::getName)
                .toList();
    }

    @Override
    public String getSelectAllSql() {
        return "SELECT * FROM %s".formatted(tableName);
    }

    @Override
    public String getSelectByIdSql() {
        return "SELECT * FROM %s WHERE %s = ?".formatted(tableName, idFieldName);
    }

    @Override
    public String getInsertSql() {
        String fieldNames = String.join(", ", withoutIdFieldNames);
        String questions = makeStringOfQuestions();
        return "INSERT INTO %s (%s) VALUES (%s)".formatted(tableName, fieldNames, questions);
    }

    private String makeStringOfQuestions() {
        String[] arrOfQuestions = new String[withoutIdFieldNames.size()];
        Arrays.fill(arrOfQuestions, "?");
        return String.join(", ", arrOfQuestions);
    }

    @Override
    public String getUpdateSql() {
        String paramsToChange = makeStringOfParamsToChange();
        return "UPDATE %s SET %s WHERE %s = ?".formatted(tableName, paramsToChange, idFieldName);
    }

    private String makeStringOfParamsToChange() {
        String[] arrOfQuestions = new String[withoutIdFieldNames.size()];
        for (int i = 0; i < arrOfQuestions.length; i++) {
            arrOfQuestions[i] = withoutIdFieldNames.get(i) + " = ?";
        }
        return String.join(", ", arrOfQuestions);
    }
}
