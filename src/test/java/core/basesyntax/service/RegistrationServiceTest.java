package core.basesyntax.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.dao.StorageDaoImpl;
import core.basesyntax.exception.RegistrationException;
import core.basesyntax.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegistrationServiceTest {
    private RegistrationService registrationService;

    @BeforeEach
    void setUp() {
        registrationService = new RegistrationService(new StorageDaoImpl());
    }

    @Test
    void register_validUser_ok() {
        User user = new User();
        user.setLogin("validLogin");
        user.setPassword("validPass");
        user.setAge(25);

        User registeredUser = registrationService.register(user);

        assertNotNull(registeredUser);
        assertEquals("validLogin", registeredUser.getLogin());
    }

    @Test
    void register_nullUser_notOk() {
        assertThrows(RegistrationException.class, () -> registrationService.register(null));
    }

    @Test
    void register_existingLogin_notOk() {
        User user = new User();
        user.setLogin("duplicateLogin");
        user.setPassword("password");
        user.setAge(30);
        registrationService.register(user);

        User duplicateUser = new User();
        duplicateUser.setLogin("duplicateLogin");
        duplicateUser.setPassword("anotherPass");
        duplicateUser.setAge(28);

        assertThrows(RegistrationException.class,
                () -> registrationService.register(duplicateUser));
    }
}





