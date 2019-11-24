package ru.ten.crud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.ten.crud.model.Role;
import ru.ten.crud.model.User;
import ru.ten.crud.service.RoleService;
import ru.ten.crud.service.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller("admin")
public class AdminController {

    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public AdminController(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @GetMapping(value = {"/"})
    public String redirectToLoginPage() {
        return "redirect:/login";
    }

    @GetMapping(value = {"/login"})
    public String showLoginPage()  {
        return "login";
    }

    @GetMapping(value = {"/admin"})
    public ModelAndView adminPage() {
        List<User> listUsers = userService.listUser();
        ModelAndView model = new ModelAndView("admin");
        model.addObject("admin", listUsers);
        return model;
    }

    @GetMapping(value = {"/admin/addUser"})
    public String addUser() {
        return "addUser";
    }

    @PostMapping(value = {"/admin/addUser"})
    public String addUser(@RequestParam("login") String login, @RequestParam("password") String password,
                          @RequestParam("role") String role) {
        User user = new User(login, password, true);
        user.setRoles(getRoles(role));
        userService.insertUser(user);
        return "redirect:/admin";
    }

    @GetMapping(value = {"/admin/edit/{id}"})
    public ModelAndView editUser(@PathVariable("id") Long id)  {
        ModelAndView model = new ModelAndView("edit");
        model.addObject("user", userService.selectUser(id));
        return model;
    }

    /*@PostMapping(value = {"/admin/edit"})
    public String editUser(@RequestParam("id") Long id, @RequestParam("login") String login,
                           @RequestParam("password") String password, @RequestParam("role") String role)  {
        User user = new User(id, login, password, true);
        user.setRoles(getRoles(role));
        userService.updateUser(user);
        return "redirect:/admin";
    }*/

    @PostMapping(value = "/admin/edit")
    public String editUser(@ModelAttribute("user") User user, @ModelAttribute("role") Role role,
                           HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, SQLException {
        request.setCharacterEncoding("utf-8");
        userService.updateUser(new User(user.getId(), user.getLogin(),user.getPassword(),true));

        user.setRoles(getRoles(role.getName()));
        userService.updateUser(user);
        return "redirect:/admin";
    }


    /*@RequestMapping(value = {"/admin/edit"}, method = RequestMethod.POST)
    public String editUser(@RequestParam("id") Long id, @RequestParam("login") String login,
                           @RequestParam("password") String password, @RequestParam("role") String role) {
        User user = new User(id, login, password, true);
        user.setRoles(getRoles(role));

        userService.updateUser(user);

        return "redirect:/admin";

    }

    @RequestMapping(value = "/admin/users/edit", method = RequestMethod.POST)
    public String editUser(@ModelAttribute("user") User user,
                           HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, SQLException {

        request.setCharacterEncoding("utf-8");
        if (user.getName().isEmpty()) {
            return "redirect:/edit?id=" + user.getId();
        }
        userService.updateUser(new User(user.getId(), user.getName(), user.getAge()));
        return "redirect:/admin";
    }*/




    @GetMapping(value = "/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @GetMapping(value = {"/user"})
    public ModelAndView userPage() {
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        String username = user.getName();
        ModelAndView model = new ModelAndView("user");
        model.addObject("user", user);
        return model;
    }

    @GetMapping(value = "/error")
    public String accessDenied() {
        return "error";
    }

    private Set<Role> getRoles(String role) {
        Set<Role> roles = new HashSet<>();

        switch (role) {
            case "admin":
                roles.add(roleService.getRoleById(1L));
                break;
            case "user":
                roles.add(roleService.getRoleById(2L));
                break;
            case "admin, user":
                roles.add(roleService.getRoleById(1L));
                roles.add(roleService.getRoleById(2L));
                break;
            default:
                roles.add(roleService.getRoleById(2L));
                break;
        }

        return roles;
    }

}
