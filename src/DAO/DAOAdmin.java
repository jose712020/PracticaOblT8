package DAO;

import models.Admin;

import java.util.ArrayList;

public interface DAOAdmin {
    public ArrayList<Admin> readAdmin(DAOManager dao);
    public boolean insert(DAOManager dao, Admin admin);
}
