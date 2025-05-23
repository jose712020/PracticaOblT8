package DAO;

import models.Cliente;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DAOClienteSQL implements DAOCliente{
    private DAOPedidoSQL daoPedidoSQL = new DAOPedidoSQL();
    @Override
    public ArrayList<Cliente> readAll(DAOManager dao) {
        ArrayList<Cliente> clientes = new ArrayList<>();

        try {
            dao.open();
            String sentencia = "SELECT * FROM Cliente";
            PreparedStatement ps = dao.getConn().prepareStatement(sentencia);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    clientes.add(new Cliente(
                            rs.getInt("id"),
                            rs.getString("email"),
                            rs.getString("clave"),
                            rs.getString("nombre"),
                            rs.getString("localidad"),
                            rs.getString("provincia"),
                            rs.getString("direccion"),
                            rs.getInt("movil"),
                            rs.getString("token"),
                            rs.getBoolean("isValid")
                    ));
                }
                dao.close();
                for (Cliente c : clientes) {
                    c.setPedidos(daoPedidoSQL.readPedidosByIdCliente(dao, c));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return clientes;
    }

    @Override
    public Cliente buscaClientePrueba(DAOManager dao) {
        try {
            dao.open();
            Cliente cliente = null;
            String sentencia = "SELECT * FROM `Cliente` WHERE `Cliente`.`id` = '" + 99999 + "'";
            PreparedStatement ps = dao.getConn().prepareStatement(sentencia);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    cliente = new Cliente(rs.getInt("id"),
                            rs.getString("email"),
                            rs.getString("clave"),
                            rs.getString("nombre"),
                            rs.getString("localidad"),
                            rs.getString("provincia"),
                            rs.getString("direccion"),
                            rs.getInt("movil"),
                            rs.getString("token"),
                            rs.getBoolean("isValid")
                    );
                }
            }
            dao.close();
            return cliente;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean insert(DAOManager dao, Cliente cliente) {
        try {
            dao.open();
            String sentencia = "INSERT INTO `Cliente` (`id`, `email`, `clave`, `nombre`, `localidad`, `provincia`, `direccion`, " +
                    "`movil`, `token`, `isValid`) VALUES ('" + cliente.getId() + "', '" + cliente.getEmail() + "', '" +
                    cliente.getClave() + "', '" + cliente.getNombre() + "', '" + cliente.getLocalidad() + "', '" +
                    cliente.getProvincia() + "', '" + cliente.getDireccion() + "', '" + cliente.getMovil() + "', '" +
                    cliente.getToken() + "', '"
                    + (cliente.isValid() ? 1 : 0) + "')";
            Statement stmt = dao.getConn().createStatement();
            stmt.executeUpdate(sentencia);
            dao.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean update(DAOManager dao, Cliente cliente) {
        try {
            dao.open();
            String sentencia = "UPDATE Cliente SET 'email' = '" + cliente.getEmail() + "', `clave` = '" +
                    cliente.getClave() + "', `nombre` = '" + cliente.getNombre() + "', `localidad` = '" +
                    cliente.getLocalidad() + "', `provincia` = '" + cliente.getProvincia() + "', `direccion` = '" +
                    cliente.getDireccion() + "', `movil` = '" + cliente.getMovil() + "', `token` = '" + cliente.getToken() +
                    "', `isValid` = '" + cliente.isValid() + "' WHERE `Cliente`.`id` = " + cliente.getId();
            Statement stmt = dao.getConn().createStatement();
            stmt.executeUpdate(sentencia);
            dao.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(DAOManager dao, Cliente cliente) {
        try {
            dao.open();
            String sentencia = "DELETE FROM Cliente WHERE `Cliente`.`id` = '" + cliente.getId() + "'";
            Statement stmt = dao.getConn().createStatement();
            stmt.executeUpdate(sentencia);
            dao.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
