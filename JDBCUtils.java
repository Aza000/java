package kz.dataBase.study.classes;

import kz.dataBase.study.interfaces.SetHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCUtils {

    public static <T>T select (Connection c, String sql, SetHandler<T> setHandler, Object ... parameters) throws SQLException {
        try(PreparedStatement preparedStatement = c.prepareStatement(sql)){
            populatePreparedStatement(preparedStatement,parameters);
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                return setHandler.handle(resultSet);
            }
        }
    }

    public static <T>T insert(Connection connection, String sql, SetHandler<T> setHandler, Object ... parameters) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS)){
            populatePreparedStatement(statement,parameters);
            int result = statement.executeUpdate();
            if (result != 1)
                throw new SQLException("Can't insert row to database. Result=" + result);
            try (ResultSet resultSet = statement.getGeneratedKeys()){
                return setHandler.handle(resultSet);
            }
        }
    }

    public static int executeUpdate(Connection connection,String sql, Object ... parameters) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql) ){
            populatePreparedStatement(preparedStatement,parameters);
            return preparedStatement.executeUpdate();
        }
    }

    private static void populatePreparedStatement(PreparedStatement preparedStatement, Object ... parameters) throws SQLException {
        if (parameters != null){
            for (int i = 0; i < parameters.length; i++){
                preparedStatement.setObject( i +1,parameters[i]);
            }
        }
    }
}
