package kz.dataBase.study.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface SetHandler<T> {
    T handle(ResultSet resultSet) throws SQLException;
}
