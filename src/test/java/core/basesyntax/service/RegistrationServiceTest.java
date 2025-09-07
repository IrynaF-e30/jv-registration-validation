package core.basesyntax.service;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.dao.StorageDaoImpl;
import core.basesyntax.exception.RegistrationException;
import core.basesyntax.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RegistrationServiceTest {
    private RegistrationService registrationService;

    @BeforeEach
    void setUp() {
        StorageDao storageDao = new StorageDaoImpl(); // нове порожнє сховище для кожного тесту
        registrationService = new RegistrationServiceImpl(storageDao);
    }

    // ----------------- Успішна реєстрація -----------------
    @Test
    void register_validUser_ok() {
        User user = new User();
        user.setLogin("validLogin1");
        user.setPassword("validPass");
        user.setAge(25);

        User result = registrationService.register(user);

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void register_edgeLoginLength6_ok() {
        User user = new User();
        user.setLogin("abcdef"); // 6 символів
        user.setPassword("validPass");
        user.setAge(20);

        User result = registrationService.register(user);

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void register_edgePasswordLength6_ok() {
        User user = new User();
        user.setLogin("uniqueLogin2");
        user.setPassword("123456"); // 6 символів
        user.setAge(20);

        User result = registrationService.register(user);

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void register_edgeAge18_ok() {
        User user = new User();
        user.setLogin("uniqueLogin3");
        user.setPassword("validPass");
        user.setAge(18);

        User result = registrationService.register(user);

        assertNotNull(result);
        assertEquals(user, result);
    }

    // ----------------- Невалідні дані -----------------
    @Test
    void register_nullUser_notOk() {
        assertThrows(RegistrationException.class,
                () -> registrationService.register(null));
    }

    @Test
    void register_shortLogin_notOk() {
        User user = new User();
        user.setLogin("abc");
        user.setPassword("validPass");
        user.setAge(25);

        assertThrows(RegistrationException.class,
                () -> registrationService.register(user));
    }

    @Test
    void register_existingLogin_notOk() {
        User user1 = new User();
        user1.setLogin("duplicateLogin");
        user1.setPassword("validPass");
        user1.setAge(25);
        registrationService.register(user1);

        User user2 = new User();
        user2.setLogin("duplicateLogin");
        user2.setPassword("anotherPass");
        user2.setAge(30);

        assertThrows(RegistrationException.class,
                () -> registrationService.register(user2));
    }

    @Test
    void register_shortPassword_notOk() {
        User user = new User();
        user.setLogin("validLogin2");
        user.setPassword("123");
        user.setAge(25);

        assertThrows(RegistrationException.class,
                () -> registrationService.register(user));
    }

    @Test
    void register_underage_notOk() {
        User user = new User();
        user.setLogin("validLogin3");
        user.setPassword("validPass");
        user.setAge(15);

        assertThrows(RegistrationException.class,
                () -> registrationService.register(user));
    }

    @Test
    void register_nullAge_notOk() {
        User user = new User();
        user.setLogin("validLogin4");
        user.setPassword("validPass");
        user.setAge(null);

        assertThrows(RegistrationException.class,
                () -> registrationService.register(user));
    }
}



