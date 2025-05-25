package DAO;

import models.Cliente;
import models.Producto;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DAOCarroSQL implements DAOCarro, Serializable {
    private final DAOProductoSQL daoProductoSQL = new DAOProductoSQL();

    @Override
    public ArrayList<Producto> readAll(DAOManager dao, Cliente cliente) {
        ArrayList<Producto> productos = new ArrayList<>();
        ArrayList<Integer> listaId = new ArrayList<>();
        String sentencia = "SELECT idProducto FROM `Carro` WHERE `idCliente`='" + cliente.getId() + "'";

        try {
            dao.open();
            PreparedStatement ps = dao.getConn().prepareStatement(sentencia);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    listaId.add(rs.getInt("idProducto"));
                }
            }
            dao.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (!listaId.isEmpty()) {
            for (Producto p : daoProductoSQL.readAll(dao)) {
                for (Integer id : listaId) {
                    if (p.getId() == id) productos.add(p);
                }
            }
        }
        return productos;
    }

    @Override
    public boolean insert(DAOManager dao, Cliente cliente, Producto producto) {
        try {
            dao.open();
            String sentencia = "INSERT INTO `Carro` (`idCliente`, `idProducto`) VALUES ('" + cliente.getId() +
                    "', '" + producto.getId() + "')";
            Statement stmt = dao.getConn().createStatement();
            stmt.executeUpdate(sentencia);
            dao.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(DAOManager dao, Cliente cliente, Producto producto) {
        try {
            dao.open();
            String sentencia = "DELETE FROM Carro WHERE `idCliente` = '" + cliente.getId() + "' AND `idProducto` = '" +
                    producto.getId() + "' LIMIT 1";
            Statement stmt = dao.getConn().createStatement();
            stmt.executeUpdate(sentencia);
            dao.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteAll(DAOManager dao, Cliente cliente) {
        try {
            dao.open();
            String sentencia = "DELETE FROM Carro WHERE `idCliente` = '" + cliente.getId() + "'";
            Statement stmt = dao.getConn().createStatement();
            stmt.executeUpdate(sentencia);
            dao.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
