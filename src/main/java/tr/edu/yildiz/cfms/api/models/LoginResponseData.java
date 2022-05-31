package tr.edu.yildiz.cfms.api.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class LoginResponseData {
    @Getter
    private final String jwt;
}
