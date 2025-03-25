package swt6.spring.worklog.api;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swt6.spring.worklog.dto.EmployeeDto;
import swt6.spring.worklog.logic.WorkLogService;

import java.lang.reflect.Type;
import java.util.List;

@RestController // registriert die Klasse als Controller + verpackt Rueckgabewerte in HTTP-Response-Objekte
@RequestMapping(value = "/worklog", produces = {MediaType.APPLICATION_JSON_VALUE})
@Slf4j // erstellt und initialisiert ein Logger-Objekt in der Klasse
public class EmployeeRestController {

    private final WorkLogService workLogService;
    private final ModelMapper mapper;

    @Autowired // optional
    public EmployeeRestController(WorkLogService workLogService, ModelMapper mapper) {
        this.workLogService = workLogService;
        this.mapper = mapper;
        log.info("EmployeeRestController constructed");
    }

    @GetMapping(value = "/hello", produces = {MediaType.TEXT_PLAIN_VALUE})
    public String hello() {
        log.info("EmployeeRestController.hello()");
        return "Hello from EmployeeRestController";
    }

    @GetMapping(value="/employees")
    public List<EmployeeDto> getEmployees() {
        log.info("EmployeeRestController.getEmployees()");
        var employees = this.workLogService.findAllEmployees();

        // Typ EmployeeDto bei Liste geht zur Laufzeit verloren --> TypeToken verwenden (anonyme Subklasse)
        Type listDtoType = new TypeToken<List<EmployeeDto>>() {}.getType();
        return mapper.map(employees, listDtoType);
    }
}
