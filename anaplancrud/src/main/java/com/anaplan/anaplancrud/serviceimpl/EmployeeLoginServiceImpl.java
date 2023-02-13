package com.anaplan.anaplancrud.serviceimpl;

import com.anaplan.anaplancrud.dto.EmployeeDto;
import com.anaplan.anaplancrud.dto.Response;
import com.anaplan.anaplancrud.exception.EmployeeException;
import com.anaplan.anaplancrud.exception.InvalidCredentialsException;
import com.anaplan.anaplancrud.modal.Employee;
import com.anaplan.anaplancrud.repository.EmployeeRepository;
import com.anaplan.anaplancrud.service.EmployeeLoginService;
import com.anaplan.anaplancrud.service.EmployeeService;
import com.anaplan.anaplancrud.service.JwtTokenService;
import com.anaplan.anaplancrud.utility.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Map;

@Service
public class EmployeeLoginServiceImpl implements EmployeeLoginService {
    Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    @Autowired
    Environment env;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    JwtTokenService jwtTokenService;

    @Override
    public Object userLogin(EmployeeDto employeeDto) {
        try {
            logger.info("Inside userLogin of EmployeeServiceImpl");
            if (loginEmployeeValidation(employeeDto)) {
                logger.info("Getting Invalid data in request");
                throw new InvalidCredentialsException(env.getProperty(Constants.INVALID_DATA));
            }
            Employee registredEmployee = employeeExistByEmailAndPassword(employeeDto.getEmail(), employeeDto.getPassword());
            if (registredEmployee == null) {
                logger.info("Invalid Credentials");
                throw new InvalidCredentialsException(env.getProperty(Constants.INVALID_CREDENTIALS));
            }
            Map<String, String> map = jwtTokenService.generateToken(registredEmployee);
            return new Response<>(env.getProperty(Constants.SUCCESS_CODE), env.getProperty(Constants.EMPLOYEE_LOGGINED_SUCESSFULLY), map);
        } catch (Exception e) {
            String errorMessage = null;
            if (e instanceof InvalidCredentialsException) {
                errorMessage = ((InvalidCredentialsException) e).getMessage();
            } else {
                errorMessage = MessageFormat.format("Exception caught in userLogin of EmployeeServiceImpl:{0}", e.getMessage());
            }
            logger.error(errorMessage);
            throw new EmployeeException(errorMessage);
        }
    }

    public boolean loginEmployeeValidation(EmployeeDto employeeDto) {
        logger.info("Inside loginEmployeeValidation");
        boolean flag = false;
        if (employeeDto.getEmail() == null || employeeDto.getEmail().isEmpty() || employeeDto.getPassword() == null || employeeDto.getPassword().isEmpty()) {
            flag = true;
        }
        return flag;
    }

    public Employee employeeExistByEmailAndPassword(String employeeEmail, String employeePassword) throws Exception {
        logger.info("Inside employeeExistByEmailAndPassword Of EmployeeServiceImpl");
        Employee employee = employeeRepository.findByEmailAndPassword(employeeEmail, employeePassword);
        return employee;
    }

}

