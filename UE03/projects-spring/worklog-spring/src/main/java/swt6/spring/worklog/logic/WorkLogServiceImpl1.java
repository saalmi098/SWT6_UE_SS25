package swt6.spring.worklog.logic;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import swt6.spring.worklog.dao.EmployeeDao;
import swt6.spring.worklog.domain.Employee;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional // jede Methode wird in einer Transaktion ausgefuehrt
public class WorkLogServiceImpl1 implements WorkLogService {

    private final EmployeeDao employeeDao;

    @Override
    public Employee syncEmployee(Employee employee) {
        return employeeDao.merge(employee);
    }

    @Override
    @Transactional(readOnly = true)
    // // explizit angeben, dass es sich um eine Leseoperation handelt (Repeatable Read Problem)
    // Spring macht auch Optimierungen (mehrere lesende Operationen in der selben DB-Connection)
    public Optional<Employee> findEmployeeById(Long id) {
        return employeeDao.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> findAllEmployees() {
        return employeeDao.findAll();
    }
}
