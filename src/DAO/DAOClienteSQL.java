package DAO;

import models.Cliente;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DAOClienteSQL implements DAOCliente{
    private final DAOPedidoSQL daoPedidoSQL = new DAOPedidoSQL();
    private final DAOCarroSQL daoCarroSQL = new DAOCarroSQL();

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
            }
            dao.close();
            for (Cliente c : clientes) {
                c.setPedidos(daoPedidoSQL.readPedidosByIdCliente(dao, c));
                c.setCarro(daoCarroSQL.readAll(dao, c));
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
            return cliente;
        } catch (Exception e) {
            return null;
        } finally {
            try {
                dao.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
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
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            try {
                dao.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public boolean update(DAOManager dao, Cliente cliente) {
        try {
            dao.open();
            String sentencia = "UPDATE Cliente SET `email` = '" + cliente.getEmail() + "', `clave` = '" +
                    cliente.getClave() + "', `nombre` = '" + cliente.getNombre() + "', `localidad` = '" +
                    cliente.getLocalidad() + "', `provincia` = '" + cliente.getProvincia() + "', `direccion` = '" +
                    cliente.getDireccion() + "', `movil` = '" + cliente.getMovil() + "', `token` = '" + cliente.getToken() +
                    "', `isValid` = '" + (cliente.isValid() ? 1 : 0) + "' WHERE `Cliente`.`id` = " + cliente.getId();
            Statement stmt = dao.getConn().createStatement();
            stmt.executeUpdate(sentencia);
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            try {
                dao.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public boolean delete(DAOManager dao, Cliente cliente) {
        try {
            dao.open();
            String sentencia = "DELETE FROM Cliente WHERE `Cliente`.`id` = '" + cliente.getId() + "'";
            Statement stmt = dao.getConn().createStatement();
            stmt.executeUpdate(sentencia);
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            try {
                dao.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
