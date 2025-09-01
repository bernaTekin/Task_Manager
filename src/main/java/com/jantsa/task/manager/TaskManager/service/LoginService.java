package com.jantsa.task.manager.TaskManager.service;

import com.jantsa.task.manager.TaskManager.entity.RequestLogin;
import com.jantsa.task.manager.TaskManager.entity.User;

public interface LoginService {

    User login(RequestLogin requestLogin);

}
