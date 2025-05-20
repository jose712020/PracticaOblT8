package DAO;

import models.Producto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DAOProductoSQL implements DAOProducto {
    @Override
    public ArrayList<Producto> readAll(DAOManager dao) {
        ArrayList<Producto> lista = new ArrayList<>();

        String sentencia = "Select * from Producto";

        try {
            dao.open();
            PreparedStatement ps = dao.getConn().prepareStatement(sentencia);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Producto(
                            rs.getInt("id"),
                            rs.getString("marca"),
                            rs.getString("modelo"),
                            rs.getString("descripcion"),
                            rs.getFloat("precio"),
                            rs.getInt("relevancia")
                    ));
                }
                dao.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

    @Override
    public boolean insert(DAOManager dao, Producto producto) {
        try {
            dao.open();
            String sentencia = "INSERT INTO `Producto` (`id`, `marca`, `modelo`, `descripcion`, `precio`, `relevancia`) VALUES ("
                    + "'" + producto.getId() + "', "
                    + "'" + producto.getMarca() + "', "
                    + "'" + producto.getModelo() + "', "
                    + "'" + producto.getDescripcion() + "', "
                    + "'" + producto.getPrecio() + "', "
                    + "'" + producto.getRelevancia() + "')";

            Statement stmt = dao.getConn().createStatement();
            stmt.executeUpdate(sentencia);
            dao.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean update(DAOManager dao, Producto producto) {
        try {
            dao.open();
            String sentencia = "UPDATE Producto SET `marca` = '" + producto.getMarca() + "'," +
                    " `modelo` = '" +  producto.getModelo() + "'," +
                    " `descripcion` = '" + producto.getDescripcion() + "'," +
                    " `precio` = '" + producto.getPrecio() + "' WHERE `Producto`.`id` = " + producto.getId();
            Statement stmt = dao.getConn().createStatement();
            stmt.executeUpdate(sentencia);
            dao.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
