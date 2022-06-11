package tr.edu.yildiz.cfms.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tr.edu.yildiz.cfms.api.models.LoginRequest;
import tr.edu.yildiz.cfms.business.abstracts.AuthService;
import tr.edu.yildiz.cfms.core.response_types.Response;

@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @CrossOrigin
    public Response login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/logout")
    @CrossOrigin
    public Response login(@RequestHeader("Authorization") String token) {
        return authService.logout(token);
    }

    @PostMapping("/user")
    @CrossOrigin
    public Response getUser(@RequestHeader("Authorization") String token) {
        return authService.getUser(token);
    }
}
