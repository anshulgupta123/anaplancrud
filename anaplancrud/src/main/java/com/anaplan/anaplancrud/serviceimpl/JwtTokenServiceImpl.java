package com.anaplan.anaplancrud.serviceimpl;

import com.anaplan.anaplancrud.exception.EmployeeException;
import com.anaplan.anaplancrud.exception.InvalidCredentialsException;
import com.anaplan.anaplancrud.modal.Employee;
import com.anaplan.anaplancrud.service.JwtTokenService;
import com.anaplan.anaplancrud.utility.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtTokenServiceImpl implements JwtTokenService {

    @Autowired
    Environment environment;

    Logger logger = LoggerFactory.getLogger(JwtTokenServiceImpl.class);

    @Override
    public Map<String, String> generateToken(Employee employee) {
        try {
            logger.info("Inside generateToken of JwtTokenServiceImpl");
            Map<String, Object> claims = new HashMap<>();
            claims.put(Constants.PASSWORD, employee.getPassword());
            String jwtToken = Jwts.builder().setIssuer(Constants.EMPLOYEE_APPLICATION)
                    .setSubject(employee.getEmail())
                    .setIssuedAt(new Date()).addClaims(claims)
                    .setExpiration(new Date(System.currentTimeMillis() + Constants.JWT_TOKEN_VALIDITY * 1000))
                    .signWith(SignatureAlgorithm.HS256, Constants.MYSECRET)
                    .compact();
            Map<String, String> map = new HashMap<>();
            map.put(Constants.TOKEN, jwtToken);
            map.put(Constants.EMAIL, employee.getEmail());
            map.put(Constants.Message, "Authentication Successful");
            return map;
        } catch (Exception e) {
            String errorMessage = null;
            if (e instanceof InvalidCredentialsException) {
                errorMessage = ((InvalidCredentialsException) e).getMessage();
            } else {
                errorMessage = MessageFormat.format("Exception caught in generateToken of JwtTokenServiceImpl:{0}", e.getMessage());
            }
            logger.error(errorMessage);
            throw new EmployeeException(errorMessage);
        }
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(environment.getProperty(Constants.MYSECRET)).parseClaimsJws(token).getBody();
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }


    public Boolean validateToken(String token, Employee employee) {
        final String username = getUsernameFromToken(token);
        return (username.equals(employee.getName()) && !isTokenExpired(token));
    }


}


