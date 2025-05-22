package DAO;

import models.Trabajador;

import java.util.ArrayList;

public interface DAOTrabajador {
    public ArrayList<Trabajador> readAll(DAOManager dao);
    public boolean insert(DAOManager dao, Trabajador trabajador);
    public boolean update(DAOManager dao, Trabajador trabajador);
    public boolean delete(DAOManager dao, Trabajador trabajador);
    public boolean buscaTrabajadorPrueba(DAOManager dao);
}
