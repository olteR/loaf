package olter.loaf.users.controller;

import java.util.Collection;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import olter.loaf.common.exception.ResourceNotFoundException;
import olter.loaf.security.JwtHandler;
import olter.loaf.security.dto.LoginRequest;
import olter.loaf.security.dto.LoginResponse;
import olter.loaf.users.model.UserEntity;
import olter.loaf.users.model.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtHandler jwtHandler;

  public LoginResponse loginUser(LoginRequest loginRequest) {
    UserEntity user =
        userRepository
            .findByName(loginRequest.getName())
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "User not found with this name"));

    if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
    }
    log.info(user.getName() + " logging in");
    return new LoginResponse(
        user.getId(),
        user.getName(),
        jwtHandler.generateJwt(user.getName(), Map.of("uid", user.getId())));
  }

  public UserEntity getLoggedInUser(String name) {
    return userRepository
        .findByName(name)
        .orElseThrow(() -> new ResourceNotFoundException(UserEntity.class.getName(), name));
  }

  @Override
  public UserDetails loadUserByUsername(String name) {
    return userEntityToUserDetails(
        userRepository
            .findByName(name)
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "User not found with this username")));
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
}
