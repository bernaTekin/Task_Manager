package com.jantsa.task.manager.TaskManager.service;

import com.jantsa.task.manager.TaskManager.entity.RequestLogin;
import com.jantsa.task.manager.TaskManager.entity.User;
import com.jantsa.task.manager.TaskManager.repository.UserRepository; // UserRepository'yi kullanıyoruz
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserRepository userRepository;

    public User login(RequestLogin requestLogin) {

        Optional<User> userOptional = userRepository.findByCompanyId(requestLogin.getCompany_id());

        if (userOptional.isEmpty()) {
            return null;
        }

        User userFromDb = userOptional.get();
        if (userFromDb.getPassword().equals(requestLogin.getPassword())) {
            return userFromDb;
        } else {
            return null;
        }
    }
}