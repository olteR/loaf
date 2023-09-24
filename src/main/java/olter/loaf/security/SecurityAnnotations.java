package olter.loaf.security;

import java.lang.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

public class SecurityAnnotations {
  @Target({ElementType.PARAMETER, ElementType.TYPE})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  @AuthenticationPrincipal(expression = "@userService.getLoggedInUser(#this)")
  public @interface GetLoggedInUser {}
}
