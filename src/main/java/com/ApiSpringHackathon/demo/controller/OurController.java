package com.ApiSpringHackathon.demo.controller;

import com.ApiSpringHackathon.demo.model.TokenReqRes;
import com.ApiSpringHackathon.demo.model.Users;
import com.ApiSpringHackathon.demo.repository.UserRepository;
import com.ApiSpringHackathon.demo.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Controller
@RequestMapping
public class OurController {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    //Register
    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody Users user) {
        // Verifica si ya existe un usuario con ese correo
        Users existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "User with this email already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        // Encripta la contraseña
        String hashedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        System.out.println(user);
        // Guarda el usuario
        if (userRepository.save(user).getId_Usuario() > 0) {

            Map<String, String> response = new HashMap<>();
            response.put("message", "User registered successfully");
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("User Not Saved, Internal Server Error. Please try Again ");
    }


    //Login
    @PostMapping("/generate-token")
    public ResponseEntity<Object> generateToken(@RequestBody TokenReqRes tokenReqRes){
        Users databaseEmail = userRepository.findByEmail(tokenReqRes.getUserName());
        if (databaseEmail == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sorry, Does Not Exists");
        }
        if(new BCryptPasswordEncoder().matches(tokenReqRes.getPassword(), databaseEmail.getPassword())){
            String token = jwtTokenUtil.generateToken(tokenReqRes.getUserName());
            long remainingTime = jwtTokenUtil.getRemainingTime(token);
            TokenReqRes response = new TokenReqRes(tokenReqRes.getUserName(), token);
            response.setRemainingTime(remainingTime);
            response.setExpirationTime("24 hours");

            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Password Doesn't Match. Verify");
        }
    }


    @PostMapping("/validate-token")
    public ResponseEntity<Object> validateToken(@RequestBody TokenReqRes tokenReqRes){
        return ResponseEntity.ok(jwtTokenUtil.validateToken(tokenReqRes.getToken()));
    }




}