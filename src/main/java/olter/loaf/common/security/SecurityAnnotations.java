package olter.loaf.common.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

public class SecurityAnnotations {
    @Target({ElementType.PARAMETER, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @AuthenticationPrincipal(expression = "@userService.getLoggedInUser(#this)")
    public @interface GetLoggedInUser {
    }
}
