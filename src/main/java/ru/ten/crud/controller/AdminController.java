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
import ru.ten.crud.utils.CodeMessenger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String redirectToLoginPage() {
        return "redirect:/login";
    }

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public ModelAndView showLoginPage()  {
        ModelAndView model = new ModelAndView("login");
        spring.app.utils.ErrorCode code = CodeMessenger.getCode();
        if (code != null && code.equals(spring.app.utils.ErrorCode.LOGIN)) {
            model.addObject("isNotValid", true);
        }
        return model;
    }

    @RequestMapping(value = {"/admin"}, method = RequestMethod.GET)
    public ModelAndView adminPage() {
        List<User> listUsers = userService.listUser();
        ModelAndView model = new ModelAndView("admin");
        model.addObject("admin", listUsers);
        return model;
    }

    @RequestMapping(value = {"/admin/addUser"}, method = RequestMethod.GET)
    public ModelAndView addUser() {
        ModelAndView model = new ModelAndView("addUser");
        spring.app.utils.ErrorCode code = CodeMessenger.getCode();
        if (code != null && code.equals(spring.app.utils.ErrorCode.ADD)) {
            model.addObject("isNotValid", true);
        }
        return model;
    }

    @RequestMapping(value = {"/admin/addUser"}, method = RequestMethod.POST)
    public String addUser(@RequestParam("login") String login, @RequestParam("password") String password,
                          @RequestParam("role") String role) {
        if (login.isEmpty() || password.isEmpty()) {
            CodeMessenger.setCode(spring.app.utils.ErrorCode.ADD);
            return "redirect:/admin/addUser";
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        User user = new User(login, encodedPassword, true);
        user.setRoles(getRoles(role));
        userService.insertUser(user);
        return "redirect:/admin";
    }

    @RequestMapping(value = {"/admin/edit/{id}"}, method = RequestMethod.GET)
    public ModelAndView editUser(@PathVariable("id") Long id)  {
        ModelAndView model = new ModelAndView("edit");
        spring.app.utils.ErrorCode code = CodeMessenger.getCode();
        if (code != null && code.equals(spring.app.utils.ErrorCode.EDIT)) {
            model.addObject("isNotValid", true);
        }
        model.addObject("user", userService.selectUser(id));
        return model;
    }

    @RequestMapping(value = {"/admin/edit"}, method = RequestMethod.POST)
    public String editUser(@RequestParam("id") Long id, @RequestParam("login") String login,
                           @RequestParam("password") String password, @RequestParam("role") String role)  {
        if (login.isEmpty() || password.isEmpty()) {
            CodeMessenger.setCode(spring.app.utils.ErrorCode.EDIT);
            return "redirect:/admin/edit/" + id;
        }

        User user = new User(id, login, password, true);
        user.setRoles(getRoles(role));

        userService.updateUser(user);

        return "redirect:/admin";

    }

    @RequestMapping(value = "/admin/delete/{id}", method = RequestMethod.GET)
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);

        return "redirect:/admin";
    }

    @RequestMapping(value = {"/user"}, method = RequestMethod.GET)
    public ModelAndView userPage() {
        //User loggedInUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        String username = user.getName();
        ModelAndView model = new ModelAndView("user");
        model.addObject("user", user);
        return model;
    }

    @RequestMapping(value = "/error", method = RequestMethod.GET)
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
