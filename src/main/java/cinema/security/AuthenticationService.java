package cinema.security;

import cinema.exception.AuthenticationException;
import cinema.model.User;

public interface AuthenticationService {
    User register(String email, String password);

    User login(String login, String password) throws AuthenticationException;
}
