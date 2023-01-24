package nextstep.auth.controller;


import nextstep.auth.AuthService;
import nextstep.auth.TokenRequest;
import nextstep.auth.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> login(@RequestBody TokenRequest request) {
        TokenResponse tokenResponse = authService.createToken(request);
        return ResponseEntity.ok().body(tokenResponse);
    }
}