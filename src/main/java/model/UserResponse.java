package model;

import lombok.Getter;
import lombok.Setter;

public class UserResponse {
    @Getter
    @Setter
    private User user;
    @Getter
    @Setter
    private boolean success;
    @Getter
    @Setter
    private String accessToken;
    @Getter
    @Setter
    private String refreshToken;
}
