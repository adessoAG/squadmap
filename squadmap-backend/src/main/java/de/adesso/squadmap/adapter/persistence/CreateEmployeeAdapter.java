package de.adesso.squadmap.adapter.persistence;

import de.adesso.squadmap.adapter.persistence.exceptions.EmployeeAlreadyExistsException;
import de.adesso.squadmap.application.domain.Employee;
import de.adesso.squadmap.application.port.driven.employee.CreateEmployeePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class CreateEmployeeAdapter implements CreateEmployeePort {

    private final EmployeeRepository employeeRepository;
    private final EmployeePersistenceMapper mapper;

    @Override
    public long createEmployee(Employee employee) {
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new EmployeeAlreadyExistsException();
        }
        return employeeRepository.save(mapper.mapToNeo4JEntity(employee)).getEmployeeId();
    }
}