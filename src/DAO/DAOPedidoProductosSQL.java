package DAO;

import models.Pedido;
import models.Producto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class DAOPedidoProductosSQL implements DAOPedidoProductos {
    private DAOProductoSQL daoProducto = new DAOProductoSQL();

    @Override
    public ArrayList<Producto> readAll(DAOManager dao, int idPedido) {
        ArrayList<Integer> lista = new ArrayList<>();
        ArrayList<Producto> productos = new ArrayList<>();
        String sentencia = "SELECT * FROM Pedido_Productos WHERE id_pedido='" + idPedido + "'";


        try {
            dao.open();
            PreparedStatement ps = dao.getConn().prepareStatement(sentencia);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(rs.getInt("id_producto"));
                }
            }
            dao.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (Producto p : daoProducto.readAll(dao)) {
            for (Integer idProducto : lista) {
                if (p.getId() == idProducto) productos.add(p);
            }
        }
        return productos;
    }

    @Override
    public boolean insert(DAOManager dao, Pedido pedido) {
        ArrayList<Producto> productos = pedido.getProductos();
        try {
            dao.open();
            for (Producto producto : productos) {
                String sentencia = "INSERT INTO `Pedido_Productos` (`id_pedido`, `id_producto`) VALUES ('" + pedido.getId() +
                        "', '" + producto.getId() +  "')";
                Statement stmt = dao.getConn().createStatement();
                stmt.executeUpdate(sentencia);
            }
            dao.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
