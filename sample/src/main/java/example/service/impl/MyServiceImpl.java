package example.service.impl;

import example.model.User;
import example.repository.RepositoryX;
import example.service.MyService;

public class MyServiceImpl implements MyService {

    RepositoryX x;

    public MyServiceImpl(RepositoryX x) {
        this.x = x;
    }

    @Override
    public void login(User user) {

    }
}
