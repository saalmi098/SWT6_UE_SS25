package swt6.spring.worklog.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // Wenn Exception geworfen wird, wird HTTP-Statuscode 404 zurueckgegeben
public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(Long id) {
        super("Could not find employee with id %d".formatted(id));
    }
}
