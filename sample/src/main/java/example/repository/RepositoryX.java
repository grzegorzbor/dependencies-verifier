package example.repository;

import example.repository.dbutil.DbDriver;

public class RepositoryX {
    DbDriver driver;


    public RepositoryX(DbDriver driver) {
        this.driver = driver;
    }
}
