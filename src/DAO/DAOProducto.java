package DAO;

import models.Producto;

import java.util.ArrayList;

public interface DAOProducto {
    public ArrayList<Producto> readAll(DAOManager dao);
    public boolean insert(DAOManager dao, Producto producto);
    public boolean update(DAOManager dao, Producto producto);
    public boolean delete(DAOManager dao, Producto producto);
}
