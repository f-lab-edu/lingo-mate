package org.example.domain.login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.login.dto.LoginForm;
import org.example.domain.login.dto.LoginResponse;
import org.example.domain.login.dto.LogoutResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {
    private LoginService loginService;
    
    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginForm loginForm, HttpServletRequest request) {

        LoginResponse loginResponse = loginService.createSession(loginForm, request);

        if(loginResponse == null) {
            return ResponseEntity.badRequest().body(loginResponse);
        }
        
        return ResponseEntity.ok().body(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(HttpServletRequest request) {

        boolean isInvalidated = loginService.invalidateSession(request);

        if(isInvalidated == true) {
            return ResponseEntity.ok().body(new LogoutResponse("로그아웃 성공"));
        } else {
            return ResponseEntity.badRequest().body(new LogoutResponse("로그아웃 실패"));
        }
    }

}
