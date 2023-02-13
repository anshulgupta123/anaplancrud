package com.anaplan.anaplancrud.service;

import com.anaplan.anaplancrud.dto.EmployeeDto;
import org.springframework.stereotype.Service;

@Service
public interface EmployeeLoginService {
    public Object userLogin(EmployeeDto employeeDto);
}
