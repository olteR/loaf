package olter.loaf.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(STATELESS).and()
            .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class).authorizeHttpRequests()
            .requestMatchers("/", "/api/auth/**", "ws/**").permitAll().anyRequest().authenticated().and().csrf()
            .disable().formLogin().disable().httpBasic().disable().logout().disable();
        return http.build();
    }
}
