package DAO;

import models.Pedido;
import models.Producto;

import java.util.ArrayList;
import java.util.HashMap;

public interface DAOPedidoProductos {
    public ArrayList<Producto> readAll(DAOManager dao, int idPedido);
    public boolean insert(DAOManager dao, Pedido pedido);
}
