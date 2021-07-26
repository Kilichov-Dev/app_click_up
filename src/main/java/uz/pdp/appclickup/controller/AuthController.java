package uz.pdp.appclickup.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appclickup.dto.LoginDto;
import uz.pdp.appclickup.dto.RegisterDto;
import uz.pdp.appclickup.dto.Response;
import uz.pdp.appclickup.entity.User;
import uz.pdp.appclickup.security.JwtProvider;
import uz.pdp.appclickup.service.AuthService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    final AuthService authService;
    final AuthenticationManager authenticationManager;
    final JwtProvider jwtProvider;

    public AuthController(AuthService authService,
                          AuthenticationManager authenticationManager,
                          JwtProvider jwtProvider) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/register")
    public HttpEntity<?> registerUser(@Valid @RequestBody RegisterDto registerDto) {
        Response response = authService.registerUser(registerDto);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @PostMapping("/login")
    public HttpEntity<?> loginUser(@RequestBody LoginDto loginDto) {
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getEmail(),
                    loginDto.getPassword()));

            User principal = (User) authenticate.getPrincipal();
            String token = jwtProvider.generateToken(principal.getEmail());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.ok(new Response("Login or password error!", false));
        }

    }

    @PutMapping("/verify")
    private HttpEntity<?> verifyEmail(@RequestParam(name = "emailCode") String code, @RequestParam String email) {
        Response response = authService.verifyEmail(email, code);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }
}
