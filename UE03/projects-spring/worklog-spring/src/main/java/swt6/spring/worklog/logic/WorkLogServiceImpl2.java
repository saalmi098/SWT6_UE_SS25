package swt6.spring.worklog.logic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swt6.spring.worklog.dao.EmployeeRepository;
import swt6.spring.worklog.domain.Employee;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service("workLogService")
public class WorkLogServiceImpl2 implements WorkLogService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Employee syncEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Employee> findEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }
}
