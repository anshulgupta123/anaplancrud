package com.anaplan.anaplancrud.controller;

import com.anaplan.anaplancrud.dto.EmployeeDto;
import com.anaplan.anaplancrud.exception.InvalidCredentialsException;
import com.anaplan.anaplancrud.service.EmployeeLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeLoginController {

    Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    @Autowired
    EmployeeLoginService employeeLoginService;


    @PostMapping("/v1/employeeLogin")
    public ResponseEntity<Object> loginUser(@RequestBody EmployeeDto employeeDto) throws InvalidCredentialsException {
        logger.info("Request for loginUser of EmployeeLoginController:{}", employeeDto);
        return new ResponseEntity<>(employeeLoginService.userLogin(employeeDto), HttpStatus.OK);
    }

}
