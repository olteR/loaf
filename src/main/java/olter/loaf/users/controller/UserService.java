package olter.loaf.users.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import olter.loaf.common.exception.InvalidFieldsException;
import olter.loaf.common.exception.ResourceNotFoundException;
import olter.loaf.common.security.JwtHandler;
import olter.loaf.common.security.dto.LoginRequest;
import olter.loaf.common.security.dto.LoginResponse;
import olter.loaf.users.UserMapper;
import olter.loaf.users.dto.RegisterRequest;
import olter.loaf.users.exception.EmailTakenException;
import olter.loaf.users.exception.InvalidRegisterRequestException;
import olter.loaf.users.exception.LoginException;
import olter.loaf.users.exception.UsernameTakenException;
import olter.loaf.users.model.UserEntity;
import olter.loaf.users.model.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtHandler jwtHandler;

    @Value("${loaf.security.min-username-length}")
    private Integer MIN_USERNAME_LENGTH;
    @Value("${loaf.security.min-password-length}")
    private Integer MIN_PASSWORD_LENGTH;

    public LoginResponse loginUser(LoginRequest loginRequest) {
        if (loginRequest.getName() == null || loginRequest.getPassword() == null) {
            throw new InvalidFieldsException(LoginRequest.class);
        }
        UserEntity user = userRepository.findByName(loginRequest.getName())
            .orElseThrow(() -> new LoginException(loginRequest.getName()));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new LoginException(loginRequest.getName());
        }
        log.info("{} logging in", user.getName());
        return new LoginResponse(user.getId(), user.getName(),
            jwtHandler.generateJwt(user.getName(), Map.of("uid", user.getId())));
    }

    public void registerUser(RegisterRequest registerRequest) {
        log.info("Registering user with name {}", registerRequest.getUsername());
        validateRegisterRequest(registerRequest);
        UserEntity user = userMapper.registerRequestToEntity(registerRequest);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userRepository.save(user);
    }

    public UserEntity getLoggedInUser(String name) {
        return userRepository.findByName(name)
            .orElseThrow(() -> new ResourceNotFoundException(UserEntity.class, name));
    }

    @Override
    public UserDetails loadUserByUsername(String name) {
        return userEntityToUserDetails(userRepository.findByName(name).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found with this username")));
    }

    private UserDetails userEntityToUserDetails(UserEntity userEntity) {
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public String getPassword() {
                return userEntity.getPassword();
            }

            @Override
            public String getUsername() {
                return userEntity.getName();
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };
    }

    private void validateRegisterRequest(RegisterRequest request) {
        if (request.getUsername().length() < MIN_USERNAME_LENGTH ||
            request.getPassword().length() < MIN_PASSWORD_LENGTH) {
            throw new InvalidRegisterRequestException(request.getUsername());
        }

        if (!Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"" +
            "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e" +
            "-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:" +
            "(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])" +
            "|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:" +
            "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e" +
            "-\\x7f])+)\\])").matcher(request.getEmail()).find()) {
            throw new InvalidRegisterRequestException(request.getUsername());
        }

        Optional<UserEntity> userWithUsername = userRepository.findByName(request.getUsername());
        if (userWithUsername.isPresent()) {
            throw new UsernameTakenException(request.getUsername());
        }

        Optional<UserEntity> userWithEmail = userRepository.findByEmail(request.getEmail());
        if (userWithEmail.isPresent()) {
            throw new EmailTakenException(request.getEmail());
        }
    }
}
