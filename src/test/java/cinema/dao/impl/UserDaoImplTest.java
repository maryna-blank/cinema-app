package cinema.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cinema.dao.RoleDao;
import cinema.dao.UserDao;
import cinema.exception.DataProcessingException;
import cinema.model.Role;
import cinema.model.User;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private UserDao userDao;
    private User user;
    private RoleDao roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        roleDao.add(new Role(Role.RoleName.USER));
        roleDao.add(new Role(Role.RoleName.ADMIN));
        user = new User();
        user.setEmail("user@i.ua");
        user.setPassword("2345");
        user.setRoles(Set.of(roleDao.getByName(Role.RoleName.USER.name())));
        userDao.add(user);
    }

    @Test
    void add_Ok() {
        User expected = new User();
        expected.setEmail("admin@i.ua");
        expected.setPassword("1234");
        expected.setRoles(Set.of(roleDao.getByName(Role.RoleName.ADMIN.name())));
        userDao.add(expected);
        User actual = userDao.get(expected.getId()).get();
        assertNotNull(actual);
        assertEquals(expected, actual);
        // TODO LazyInitializationException: failed to lazily initialize a collection
        //  of role: cinema.model.User.roles, could not initialize proxy - no Session
    }

    @Test
    void add_Null_NotOk() {
        assertThrows(DataProcessingException.class, () -> userDao.add(null));
    }

    @Test
    void get_Ok() {
        Optional<User> userOptional = userDao.get(user.getId());
        assertFalse(userOptional.isEmpty());
        assertEquals(user, userOptional.get());
        // TODO LazyInitializationException: failed to lazily initialize a collection
        //  of role: cinema.model.User.roles, could not initialize proxy - no Session
    }

    @Test
    void get_Null_NotOk() {
        assertThrows(DataProcessingException.class, () -> userDao.get(null));
    }

    @Test
    void get_NonExistent_NotOk() {
        assertTrue(userDao.get(7L).isEmpty());
    }

    @Test
    void findByEmail_Ok() {
        Optional<User> actual = userDao.findByEmail(user.getEmail());
        assertNotNull(actual.get());
        assertEquals(user, actual.get());
    }

    @Test
    void findByEmail_Null_NotOk() {
        Optional<User> actual = userDao.findByEmail(null);
        assertTrue(actual.isEmpty());
    }

    @Test
    void findByEmail_NoSuchEmail_NotOk() {
        Optional<User> actual = userDao.findByEmail("bob@i.ua");
        assertTrue(actual.isEmpty());
    }
}
