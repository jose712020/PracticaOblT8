package DAO;

import models.Cliente;

import java.util.ArrayList;

public interface DAOCliente {
    public ArrayList<Cliente> readAll(DAOManager dao);
    public boolean insert(DAOManager dao, Cliente cliente);
    public boolean update(DAOManager dao, Cliente cliente);
    public boolean delete(DAOManager dao, Cliente cliente);
}
