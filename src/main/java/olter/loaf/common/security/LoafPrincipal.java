package olter.loaf.common.security;

import java.security.Principal;

public class LoafPrincipal implements Principal {
    private final String name;

    public LoafPrincipal(String name) {
        this.name = name;
   }

   @Override
    public String getName() {
        return name;
   }
}
