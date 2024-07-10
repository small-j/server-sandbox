package server.sandbox.pinterestclone.service.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ADMIN"), USER("USER");

    private String rolePrefix = "ROLE_";
    private String role;

    UserRole(String role) {
        this.role = rolePrefix + role;
    }

    public String getNonPrefixRole() {
        return role.replace(rolePrefix, "");
    }
}
