package notifier.afterrefactoring.database;

import java.time.LocalDate;

public interface ReportDao {

    public String createReport(String departmentId, LocalDate dateFrom, LocalDate dateTo);

}
