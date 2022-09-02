package cinema.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cinema.dao.RoleDao;
import cinema.model.Role;
import cinema.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest {
    private RoleDao roleDao;
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void add_Ok() {
        Role expected = new Role();
        expected.setRoleName(Role.RoleName.USER);
        Mockito.when(roleDao.add(expected)).thenReturn(expected);
        Role actual = roleService.add(expected);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void add_Null_NotOk() {
        assertThrows(RuntimeException.class, () -> roleService.add(null));
    }

    @Test
    void getByName_Ok() {
        Role expected = new Role();
        expected.setRoleName(Role.RoleName.USER);
        String roleName = Role.RoleName.USER.name();
        Mockito.when(roleDao.getByName(roleName)).thenReturn(expected);
        Role actual = roleService.getByName(roleName);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void getByName_Null_NotOk() {
        assertThrows(RuntimeException.class, () -> roleService.getByName(null));
    }

    @Test
    void getRoleByName_NoSuchName_NotOk() {
        String roleName = Role.RoleName.USER.name();
        assertThrows(RuntimeException.class, () -> roleService.getByName(roleName));
    }
}
