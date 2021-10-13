package notifier.afterrefactoring;

import notifier.afterrefactoring.database.ReportDao;
import notifier.afterrefactoring.database.SalaryPaymentsReportDaoImpl;
import notifier.afterrefactoring.mail.MailSender;

import java.sql.Connection;
import java.time.LocalDate;

public class RefactoredSalaryHtmlReportNotifier {

    private Connection connection;

    public RefactoredSalaryHtmlReportNotifier(Connection databaseConnection) {
        this.connection = databaseConnection;
    }

    public void generateAndSendHtmlSalaryReport(String departmentId, LocalDate dateFrom, LocalDate dateTo, String recipients) {

        ReportDao dao = new SalaryPaymentsReportDaoImpl(connection);
        String reportHtml = dao.createReport(departmentId, dateFrom, dateTo);

        MailSender.sendHtml(recipients,
                "Monthly department salary report",
                reportHtml);
    }
}