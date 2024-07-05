package server.sandbox.pinterestclone.domain.converter;

import jakarta.persistence.AttributeConverter;
import server.sandbox.pinterestclone.service.enums.UserRole;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RolesAttributeConverter implements AttributeConverter<List<UserRole>, String> {
    @Override
    public String convertToDatabaseColumn(List<UserRole> roles) {
        return roles.stream().map(role -> role.getRole()).collect(Collectors.joining(", "));
    }

    @Override
    public List<UserRole> convertToEntityAttribute(String roles) {
        return Arrays.asList(roles.split(", ")).stream().map(role -> convertRoleStringToUserRoleEnum(role)).toList();
    }

    private UserRole convertRoleStringToUserRoleEnum(String role) {
        if (UserRole.ADMIN.getRole() == role) return UserRole.ADMIN;
        else  return UserRole.USER;
    }
}
