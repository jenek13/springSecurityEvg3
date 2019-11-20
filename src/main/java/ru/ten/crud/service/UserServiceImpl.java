package ru.ten.crud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ten.crud.dao.UserDAO;
import ru.ten.crud.model.User;
import java.sql.SQLException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    public UserServiceImpl() {
    }

    @Autowired
    private UserDAO userDAO;

    @Override
    public User selectUser(long id)  {
        try {
            return  userDAO.getUserById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  null;
    }

    @Override
    public List<User> listUser() {
        return userDAO.selectAllUsers();
    }

    @Override
    public void deleteUser(long id)  {
        try {
            userDAO.removeUser(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertUser(User user) {
        userDAO.addUser(user);
    }

    @Override
    public void updateUser(User user)  {
        try {
            userDAO.editUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User getUserByLogin(String login) {
        return userDAO.getUserByLogin(login);
    }

}
