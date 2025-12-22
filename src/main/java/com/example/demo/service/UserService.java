public interface UserService {

    User register(String email, String password, String role);

    User login(String email, String password);

    User getByEmail(String email);
}
