package ru.polyan.onlinecart;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.polyan.onlinecart.model.User;
import ru.polyan.onlinecart.services.UserService;

import java.util.NoSuchElementException;

@SpringBootTest //(classes = {UserService.class, UserRepository.class, User.class})
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void findByName() {
        try {
            User user = userService.findByUsername("user").orElseThrow(()->new NoSuchElementException("User not find"));
        } catch (NoSuchElementException e) {
            Assertions.fail("User not find");
        }
    }

    @Test
    public void findByNameNoSuchUser() {
        Throwable check = Assertions.assertThrows(NoSuchElementException.class, ()->{userService.findByUsername("user777").orElseThrow(()->new NoSuchElementException("User not find"));});
        Assertions.assertEquals(check.getClass(), NoSuchElementException.class);
    }

}
