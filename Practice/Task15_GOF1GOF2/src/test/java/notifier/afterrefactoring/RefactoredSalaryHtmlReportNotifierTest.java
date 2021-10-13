package notifier.afterrefactoring;

import notifier.afterrefactoring.database.DataSourceHelper;
import notifier.afterrefactoring.mail.MailSender;
import org.h2.tools.Server;
import org.junit.Before;
import org.junit.Test;


import java.sql.*;
import java.time.LocalDate;
import java.time.Month;

/**
 * Задание 15. Паттерны проектирования, SOLID
 * Рефакторить код
 * 15.2. https://bitbucket.org/agoshkoviv/solid-homework/src/099989b0c76217689c4642242c87c1ac080dfc01/src/main/java/ru/sbt/bit/ood/solid/homework/SalaryHtmlReportNotifier.java?at=master&fileviewer=file-view-default
 */
public class RefactoredSalaryHtmlReportNotifierTest {

    @Before
    public void createBD() throws SQLException {
        DataSourceHelper.createDb();
        fillEmployees();
        fillSalaryPayments();
        Server.createTcpServer().start();
    }

    @Test
    public void test(){
        try (Connection connection = DataSourceHelper.connection()) {

            LocalDate dateFrom = LocalDate.of(2021, Month.SEPTEMBER, 3);
            LocalDate dateTo = LocalDate.of(2021, Month.OCTOBER, 3);

            RefactoredSalaryHtmlReportNotifier salaryHtmlReportSender = new RefactoredSalaryHtmlReportNotifier(connection);
            salaryHtmlReportSender.generateAndSendHtmlSalaryReport("dep1", dateFrom,
                    dateTo, "anna.zagrebneva@gmail.com");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSendHtmlMail(){
        MailSender.sendHtml("anna.zagrebneva@gmail.com",
                "Monthly department salary report",
                "<html><body><p>Hello world!!!</p></body></html>");
    }


    private boolean fillEmployees() {
        try (PreparedStatement statement = DataSourceHelper.connection()
                .prepareStatement("insert into employee" +
                        " (name, salary, department_id) values (?, ?, ?)")) {
            createEmployeeStatement(statement, "John Doe",100.0, "dep1");
            statement.addBatch();
            createEmployeeStatement(statement, "Jane Dow",50.0, "dep1");
            statement.addBatch();

            statement.executeBatch();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createEmployeeStatement(PreparedStatement statement, String name,
                                         Double salary, String department_id) throws SQLException {
        statement.setString(1, name);
        statement.setDouble(2, salary);
        statement.setString(3, department_id);
    }

    private boolean fillSalaryPayments() {
        try (PreparedStatement statement = DataSourceHelper.connection()
                .prepareStatement("insert into salary_payments" +
                        " (employee_id, date) values (?, ?)")) {

            createSalaryPaymentsStatement(statement,1,
                    LocalDate.of(2021, Month.SEPTEMBER, 15) );
            statement.addBatch();
            createSalaryPaymentsStatement(statement,2,
                    LocalDate.of(2021, Month.SEPTEMBER, 15) );
            statement.addBatch();

            statement.executeBatch();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createSalaryPaymentsStatement(PreparedStatement statement, int employee_id,
                                               LocalDate date) throws SQLException {
        statement.setInt(1, employee_id);
        statement.setDate(2, java.sql.Date.valueOf(date));
    }
}