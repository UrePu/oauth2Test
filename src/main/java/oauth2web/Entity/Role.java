package oauth2web.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    MEMBER("ROLE_MEMBER","회원"),
        ADMIN("ROLE_ADMIN","관리자");

    private final String key;
    private final String keyword;
}
