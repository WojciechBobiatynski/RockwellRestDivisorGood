import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestPassword {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestPassword.class);

    public static void main (String [] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode("password");
        LOGGER.info("Password by BCryp {} ", password);

    }
}
