package com.anaplan.anaplancrud.service;

import com.anaplan.anaplancrud.dto.EmployeeDto;
import com.anaplan.anaplancrud.modal.Employee;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public interface JwtTokenService {
    Map<String, String> generateToken(Employee employee);

    public String getUsernameFromToken(String token);

    public Date getExpirationDateFromToken(String token);

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver);

    public Claims getAllClaimsFromToken(String token);

    public Boolean isTokenExpired(String token);

    public Boolean validateToken(String token, Employee employee);


}
