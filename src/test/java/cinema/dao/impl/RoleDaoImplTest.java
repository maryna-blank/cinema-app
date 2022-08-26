package cinema.dao.impl;

import cinema.dao.RoleDao;
import cinema.exception.DataProcessingException;
import cinema.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RoleDaoImplTest extends AbstractTest {
    private RoleDao roleDao;
    private Role userRole;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        userRole = new Role(Role.RoleName.USER);
        roleDao.add(userRole);
    }

    @Test
    void add_Ok() {
        Role expected = new Role();
        expected.setRoleName(Role.RoleName.ADMIN);
        roleDao.add(expected);
        Role actual = roleDao.getByName(Role.RoleName.ADMIN.name());
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void add_Null_NotOk() {
        assertThrows(DataProcessingException.class, () -> roleDao.add(null));
    }

    @Test
    void getByName_Ok() {
        Role actual = roleDao.getByName(Role.RoleName.USER.name());
        assertNotNull(actual);
        assertEquals(userRole, actual);
    }

    @Test
    void getByName_NullRole_NotOk() {
        assertThrows(DataProcessingException.class, () -> roleDao.getByName(null));
    }

    @Test
    void getByName_NoSuchRole_NotOk() {
        assertThrows(DataProcessingException.class, () -> roleDao.getByName("ADMIN"));
    }
}
