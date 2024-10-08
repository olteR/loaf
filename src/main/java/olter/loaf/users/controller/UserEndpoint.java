package olter.loaf.users.controller;

import lombok.RequiredArgsConstructor;
import olter.loaf.common.security.dto.LoginRequest;
import olter.loaf.common.security.dto.LoginResponse;
import olter.loaf.users.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserEndpoint {
    private final UserService userService;

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok().body(userService.loginUser(loginRequest));
    }

    @PostMapping("/auth/register")
    public ResponseEntity<Void> registerUser(@RequestBody RegisterRequest registerRequest) {
        userService.registerUser(registerRequest);
        return ResponseEntity.ok().build();
    }
}
