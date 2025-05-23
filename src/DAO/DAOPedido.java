package DAO;

import models.Cliente;
import models.Pedido;
import models.Trabajador;

import java.util.ArrayList;

public interface DAOPedido {
    public ArrayList<Pedido> readAll(DAOManager dao);
    public ArrayList<Pedido> readPedidosByIdTrabajador(DAOManager dao, Trabajador trabajador);
    public boolean insert(DAOManager dao, Pedido pedido, Cliente cliente);
    public boolean update(DAOManager dao, Pedido pedido);
    public boolean updateTrabajador(DAOManager dao, Pedido pedido, Trabajador trabajador);
    public ArrayList<Pedido> readPedidosByIdCliente(DAOManager dao, Cliente c);
}
