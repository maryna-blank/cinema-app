package cinema.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import cinema.dao.UserDao;
import cinema.model.Role;
import cinema.model.User;
import cinema.service.UserService;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private static final String EMAIL = "user@i.ua";
    private static final String PASSWORD = "2345";
    private static final String ENCODED_PASSWORD = "1234";
    private static final Set<Role> ROLES = Set.of(new Role(Role.RoleName.USER));
    private UserService userService;
    private UserDao userDao;
    private User expected;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(passwordEncoder, userDao);
        expected = new User();
        expected.setId(1L);
        expected.setEmail(EMAIL);
        expected.setPassword(PASSWORD);
        expected.setRoles(ROLES);
        Mockito.when(passwordEncoder.encode(anyString())).thenReturn(ENCODED_PASSWORD);
    }

    @Test
    void add_Ok() {
        Mockito.when(userDao.add(expected)).thenReturn(expected);
        User actual = userService.add(expected);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void add_Null_NotOk() {
        assertThrows(RuntimeException.class, () -> userService.add(null));
    }

    @Test
    void get_Ok() {
        Mockito.when(userDao.get(anyLong())).thenReturn(Optional.of(expected));
        User actual = userService.get(expected.getId());
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void get_NoSuchId_NotOk() {
        Mockito.when(userDao.get(anyLong())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.get(5L));
    }

    @Test
    void get_Null_NotOk() {
        Mockito.when(userDao.get(anyLong())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.get(null));
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(anyString())).thenReturn(Optional.of(expected));
        User actual = userService.findByEmail(expected.getEmail()).get();
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void findByEmail_NoSuchEmail_NotOk() {
        Mockito.when(userDao.findByEmail(anyString())).thenReturn(Optional.empty());
        Optional<User> userOptional = userService.findByEmail(EMAIL);
        assertTrue(userOptional.isEmpty());
    }

    @Test
    void findByEmail_Null_NotOk() {
        Mockito.when(userDao.findByEmail(anyString())).thenReturn(Optional.empty());
        Optional<User> userOptional = userService.findByEmail(null);
        assertTrue(userOptional.isEmpty());
    }
}
