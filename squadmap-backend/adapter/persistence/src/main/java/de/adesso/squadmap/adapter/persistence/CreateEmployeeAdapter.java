package de.adesso.squadmap.adapter.persistence;

import de.adesso.squadmap.application.port.driven.employee.CreateEmployeePort;
import de.adesso.squadmap.common.PersistenceAdapter;
import de.adesso.squadmap.domain.Employee;
import de.adesso.squadmap.domain.exceptions.EmployeeAlreadyExistsException;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
class CreateEmployeeAdapter implements CreateEmployeePort {

    private final EmployeeRepository employeeRepository;
    private final PersistenceMapper<Employee, EmployeeNeo4JEntity> employeePersistenceMapper;

    @Override
    public long createEmployee(Employee employee) {
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new EmployeeAlreadyExistsException(employee.getEmail());
        }
        return employeeRepository.save(employeePersistenceMapper.mapToNeo4JEntity(employee)).getEmployeeId();
    }
}
