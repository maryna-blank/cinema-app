package cinema.controller;

import cinema.model.Role;
import cinema.model.User;
import cinema.service.RoleService;
import cinema.service.UserService;
import java.util.Set;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inject")
public class InjectController {
    private final RoleService roleService;
    private final UserService userService;

    public InjectController(RoleService roleService,
                            UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @GetMapping
    public String injectData() {
        // add roles
        Role adminRole = new Role();
        adminRole.setRoleName(Role.RoleName.ADMIN);
        roleService.add(adminRole);
        Role userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);
        roleService.add(userRole);

        // add users
        User admin = new User();
        admin.setEmail("admin@gmail.com");
        admin.setPassword("1234");
        admin.setRoles(Set.of(adminRole));
        userService.add(admin);

        User user = new User();
        user.setEmail("user@gmail.com");
        user.setPassword("1234");
        user.setRoles(Set.of(userRole));
        userService.add(user);

        return "Done!";
    }
}
