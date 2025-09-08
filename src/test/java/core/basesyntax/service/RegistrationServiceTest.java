package core.basesyntax.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.dao.StorageDaoImpl;
import core.basesyntax.exception.RegistrationException;
import core.basesyntax.model.User;
import core.basesyntax.db.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegistrationServiceTest {
    private RegistrationService registrationService;
    private StorageDaoImpl storageDao;

    @BeforeEach
    void setUp() {
        Storage.people.clear();
        storageDao = new StorageDaoImpl();
        registrationService = new RegistrationServiceImpl(storageDao);
    }

    @Test
    void register_nullUser_notOk() {
        assertThrows(RegistrationException.class, () -> registrationService.register(null));
    }

    @Test
    void register_nullLogin_notOk() {
        User user = new User(null, "password", 20);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_emptyLogin_notOk() {
        User user = new User("", "password", 20);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_shortLogin_notOk() {
        User user = new User("short", "password", 20);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_duplicateLogin_notOk() {
        User first = new User("duplicateLogin", "password", 25);
        storageDao.add(first); // seed directly via DAO

        User second = new User("duplicateLogin", "password2", 30);
        assertThrows(RegistrationException.class, () -> registrationService.register(second));
    }

    @Test
    void register_nullPassword_notOk() {
        User user = new User("validLogin", null, 20);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_emptyPassword_notOk() {
        User user = new User("validLogin", "", 20);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_shortPassword_notOk() {
        User user = new User("validLogin", "12345", 20);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_nullAge_notOk() {
        User user = new User("validLogin", "password", null);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_underage_notOk() {
        User user = new User("validLogin", "password", 17);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_exactMinAge_ok() {
        User user = new User("validLogin", "password", 18);
        User registered = registrationService.register(user);

        assertNotNull(registered);
        assertEquals(18, registered.getAge());
    }

    @Test
    void register_validUser_ok() {
        User user = new User("validLogin", "strongPass", 20);
        User registered = registrationService.register(user);

        assertNotNull(registered);
        assertEquals("validLogin", registered.getLogin());
    }
}







