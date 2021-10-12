package database.dao;

import database.connection.DataSourceHelper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FibonacciDaoImpl implements FibonacciDao {

    @Override
    public List<Integer> getFibonacciNumbers(int number) {

        List<Integer> list = new ArrayList<>();

        try (PreparedStatement statement = DataSourceHelper.connection()
                .prepareStatement("select * from fibonacci f where f.number<=?")) {
            statement.setInt(1, number);
            statement.execute();

            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                list.add(resultSet.getInt("fibonacci"));
            }

            if (list.size() == (number+1)) return list;
            return new ArrayList<>();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean createFibonacciNumbers(List<Integer> numbers) {

        // получаем количество записей в базе
        int firstIndex = rowCount("fibonacci");

        if (firstIndex > numbers.size()) return true;

        // добавляем отсутствующие в базе записи
        List<Integer> insertedNumbers = numbers.subList(firstIndex, numbers.size());
        try(PreparedStatement statement = DataSourceHelper.connection()
                .prepareStatement("insert into fibonacci (number, fibonacci) values (?, ?)")) {

            for (Integer number:insertedNumbers) {
                statement.setInt(1, firstIndex);
                statement.setInt(2, number);
                statement.addBatch();
                firstIndex++;
            }
            int[] executeBatch = statement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public int rowCount(String tableName){

        try (PreparedStatement statement = DataSourceHelper.connection()
                .prepareStatement("select count(*) from "+ tableName)) {
            statement.execute();

            ResultSet result = statement.getResultSet();
            result.next();
            return result.getInt(1);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

