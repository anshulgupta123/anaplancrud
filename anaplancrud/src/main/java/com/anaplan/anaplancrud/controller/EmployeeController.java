package com.anaplan.anaplancrud.controller;

import com.anaplan.anaplancrud.dto.EmployeeDto;
import com.anaplan.anaplancrud.exception.EmployeeException;
import com.anaplan.anaplancrud.repository.EmployeeRepository;
import com.anaplan.anaplancrud.service.EmployeeService;
import com.anaplan.anaplancrud.service.JwtTokenService;
import com.anaplan.anaplancrud.utility.Constants;
import com.anaplan.anaplancrud.utility.UrlConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    JwtTokenService jwtTokenService;

    @Autowired
    EmployeeRepository employeeRepository;


    @Autowired
    Environment environment;

    Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @PostMapping(value = UrlConstants.ADD_EMPLOYEE)
    public ResponseEntity<Object> addEmployee(@RequestHeader String xToken, @RequestBody EmployeeDto employeeDto) {
        logger.info("Request for addEmployee of EmployeeController :{}", employeeDto);
        if (checkSessionExpire(xToken)) {
            logger.info("Session Time Out");
            throw new EmployeeException(environment.getProperty(Constants.SESSION_EXPIRED));
        }
        return new ResponseEntity<>(employeeService.addEmployee(employeeDto), HttpStatus.OK);
    }

    @PutMapping(value = UrlConstants.UPDATE_EMPLOYEE)
    public ResponseEntity<Object> updateEmployee(@RequestHeader String xToken, @RequestBody EmployeeDto employeeDto) {
        logger.info("Request for updateEmployee of EmployeeController :{}", employeeDto);
        if (checkSessionExpire(xToken)) {
            logger.info("Session Time Out");
            throw new EmployeeException(environment.getProperty(Constants.SESSION_EXPIRED));
        }
        return new ResponseEntity<>(employeeService.updateEmployee(employeeDto), HttpStatus.OK);
    }

    @GetMapping(UrlConstants.GET_ALL_EMPLOYEES)
    public ResponseEntity<Object> getAllEmployee(@RequestHeader String xToken) {
        if (checkSessionExpire(xToken)) {
            logger.info("Session Time Out");
            throw new EmployeeException(environment.getProperty(Constants.SESSION_EXPIRED));
        }
        logger.info("Request for getAllEmployee of EmployeeController");
        return new ResponseEntity<>(employeeService.getAllEmployess(), HttpStatus.OK);
    }

    @GetMapping(UrlConstants.GET_EMPLOYEE_BY_ID)
    public ResponseEntity<Object> getEmployeeBYId(@RequestHeader String xToken, @RequestParam Long employeeId) {
        logger.info("Request for getEmployeeBYId of EmployeeController");
        if (checkSessionExpire(xToken)) {
            logger.info("Session Time Out");
            throw new EmployeeException(environment.getProperty(Constants.SESSION_EXPIRED));
        }
        return new ResponseEntity<>(employeeService.getEmployeeById(employeeId), HttpStatus.OK);

    }

    @DeleteMapping(UrlConstants.DELETE_EMPLOYEE)
    public ResponseEntity<Object> deleteEmployeeBYId(@RequestHeader String xToken, @RequestParam Long employeeId) {
        if (checkSessionExpire(xToken)) {
            logger.info("Session Time Out");
            throw new EmployeeException(environment.getProperty(Constants.SESSION_EXPIRED));
        }
        logger.info("Request for deleteEmployeeBYId of EmployeeController");
        return new ResponseEntity<>(employeeService.deleteEmployee(employeeId), HttpStatus.OK);
    }

    public boolean checkSessionExpire(String xToken) {
        logger.info("Checking session is expired or not");
        return jwtTokenService.isTokenExpired(xToken);
    }
}
