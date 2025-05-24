package DAO;

import models.Cliente;
import models.Pedido;
import models.Trabajador;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DAOPedidoSQL implements DAOPedido {
    private DAOPedidoProductosSQL daoPedidoProductos = new DAOPedidoProductosSQL();

    @Override
    public ArrayList<Pedido> readAll(DAOManager dao) {
        ArrayList<Pedido> pedidos = new ArrayList<>();
        String sentencia = "SELECT * FROM Pedido";

        try {
            dao.open();
            PreparedStatement ps = dao.getConn().prepareStatement(sentencia);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(new Pedido(
                            rs.getInt("id"),
                            rs.getDate("fechaPedido").toLocalDate(),
                            rs.getDate("fechaEntregaEstimada").toLocalDate(),
                            rs.getInt("estado"),
                            rs.getString("comentario"),
                            daoPedidoProductos.readAll(dao, rs.getInt("id"))
                    ));
                }
            }
            dao.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return pedidos;
    }

    @Override
    public boolean insert(DAOManager dao, Pedido pedido, Cliente cliente) {
        try {
            dao.open();
            String sentencia = "INSERT INTO `Pedido` (`id`, `fechaPedido`, `fechaEntregaEstimada`, `estado`, `comentario`, " +
                    "`idCliente`) VALUES ('" + pedido.getId() + "', '" + pedido.getFechaPedido() + "', '" +
                    pedido.getFechaEntregaEstimada() + "', '" + pedido.getEstado() + "', '" + pedido.getComentario() +
                    "', '" + cliente.getId() + "')";
            Statement stmt = dao.getConn().createStatement();
            stmt.executeUpdate(sentencia);
            dao.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean update(DAOManager dao, Pedido pedido) {
        try {
            dao.open();
            String sentencia = "UPDATE `Pedido` SET `fechaPedido` = '" + pedido.getFechaPedido() + "', `fechaEntregaEstimada` = '"
                    + pedido.getFechaEntregaEstimada() + "', `estado` = '" + pedido.getEstado() + "', `comentario` = '" +
                    pedido.getComentario() + "' WHERE `Pedido`.`id` = " + pedido.getId();
            Statement stmt = dao.getConn().createStatement();
            stmt.executeUpdate(sentencia);
            dao.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateTrabajador(DAOManager dao, Pedido pedido, Trabajador trabajador) {
        try {
            dao.open();
            String sentencia = "UPDATE `Pedido` SET `idTrabajador` = '" + trabajador.getId() +
                    "' WHERE `Pedido`.`id` = " + pedido.getId();
            Statement stmt = dao.getConn().createStatement();
            stmt.executeUpdate(sentencia);
            dao.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public ArrayList<Pedido> readPedidosByIdCliente(DAOManager dao, Cliente cliente) {
        ArrayList<Pedido> pedidos = new ArrayList<>();
        String sentencia = "SELECT * FROM Pedido WHERE `idCliente` = '" + cliente.getId() + "'";

        try {
            dao.open();
            PreparedStatement ps = dao.getConn().prepareStatement(sentencia);
            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                    pedidos.add(new Pedido(
                            rs.getInt("id"),
                            rs.getDate("fechaPedido").toLocalDate(),
                            rs.getDate("fechaEntregaEstimada").toLocalDate(),
                            rs.getInt("estado"),
                            rs.getString("comentario"),
                            daoPedidoProductos.readAll(dao, rs.getInt("id"))
                    ));
                }
            }
            dao.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return pedidos;
    }

    @Override
    public ArrayList<Pedido> readPedidosByIdTrabajador(DAOManager dao, Trabajador trabajador) {
        ArrayList<Pedido> pedidos = new ArrayList<>();
        String sentencia = "SELECT * FROM Pedido WHERE `idTrabajador` = '" + trabajador.getId() + "'";

        try {
            dao.open();
            PreparedStatement ps = dao.getConn().prepareStatement(sentencia);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(new Pedido(
                            rs.getInt("id"),
                            rs.getDate("fechaPedido").toLocalDate(),
                            rs.getDate("fechaEntregaEstimada").toLocalDate(),
                            rs.getInt("estado"),
                            rs.getString("comentario"),
                            daoPedidoProductos.readAll(dao, rs.getInt("id"))
                    ));
                }
            }
            dao.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return pedidos;
    }
}
