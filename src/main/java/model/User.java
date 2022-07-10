package model;

import lombok.Getter;
import lombok.Setter;

public class User {
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private String email;

    public User(String email, String password, String name){
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public User(String email, String password){
        this.name = email;
        this.password = password;
    }
}
