package example;

import example.repository.RepositoryX;
import example.repository.dbutil.DbDriver;
import example.service.MyService;
import example.service.impl.MyServiceImpl;

public class Main {

    public static void main(String[] args) {
        MyService myService = new MyServiceImpl(new RepositoryX(new DbDriver()));
        myService.login(null);
    }
}
