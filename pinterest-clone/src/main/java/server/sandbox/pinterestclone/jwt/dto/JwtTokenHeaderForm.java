package server.sandbox.pinterestclone.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtTokenHeaderForm {
    String headerName;
    String jwtToken;
}
