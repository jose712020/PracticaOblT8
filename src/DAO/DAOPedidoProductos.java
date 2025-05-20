package DAO;

import models.Pedido;
import models.Producto;

import java.util.ArrayList;
import java.util.HashMap;

public interface DAOPedidoProductos {
    public HashMap<Integer, ArrayList<Integer>> readAll(DAOManager dao, Pedido pedido, ArrayList<Integer> productos);
    public boolean insert(DAOManager dao, Pedido pedido, Producto producto);
}
