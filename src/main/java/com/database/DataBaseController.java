package com.database;

import java.sql.*;

public class DataBaseController {
    public static final DataBaseConnection dataBaseConnection = new DataBaseConnection();

    public DataBaseConnection getDataBaseConnection() {
        return dataBaseConnection;
    }

    public static void executeSQL(PreparedStatement preparedStatement) {
        try {
            int[] result = preparedStatement.executeBatch();

            //System.out.println(Arrays.toString(result));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet selectSQL(String sql) throws SQLException {

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = dataBaseConnection.getConnection().prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();

        return resultSet;


    }


}
