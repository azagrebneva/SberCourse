package notifier.afterrefactoring.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class SalaryPaymentsReportDaoImpl implements ReportDao {

    private final Connection connection;

    public SalaryPaymentsReportDaoImpl(Connection connection) {
        this.connection = connection;
    }

    public String createReport(String departmentId, LocalDate dateFrom, LocalDate dateTo) {
        // prepare statement with sql
                try (PreparedStatement ps = connection.prepareStatement(
                "select emp.id as emp_id, emp.name as emp_name, sum(salary) as salary " +
                        "from employee emp " +
                        "left join salary_payments sp on emp.id = sp.employee_id " +
                        "where emp.department_id = ? and  sp.date >= ? and sp.date <= ? " +
                        "group by emp.id, emp.name" +
                        "")) {

            // inject parameters to sql
            ps.setString(1, departmentId);
            ps.setDate(2, java.sql.Date.valueOf(dateFrom));
            ps.setDate(3, java.sql.Date.valueOf(dateTo));
            // execute query and get the results
            ResultSet results = ps.executeQuery();

            String textHtml = createHtmlReport(results);
            return textHtml;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Mistakes in salary report query execution.", e);
        }
    }


    public String createHtmlReport(ResultSet results) throws SQLException {
        // create a StringBuilder holding a resulting html
        StringBuilder resultingHtml = new StringBuilder();

        resultingHtml.append("<html><body><table><tr><td>Employee</td><td>Salary</td></tr>");

        double totals = 0;
        while (results.next()) {
            // process each row of query results
            resultingHtml.append("<tr>"); // add row start tag
            resultingHtml.append("<td>").append(results.getString("emp_name")).append("</td>"); // appending employee name
            resultingHtml.append("<td>").append(results.getDouble("salary")).append("</td>"); // appending employee salary for period
            resultingHtml.append("</tr>"); // add row end tag
            totals += results.getDouble("salary"); // add salary to totals
        }
        resultingHtml.append("<tr><td>Total</td><td>").append(totals).append("</td></tr>");
        resultingHtml.append("</table></body></html>");

        return resultingHtml.toString();
    }
}
