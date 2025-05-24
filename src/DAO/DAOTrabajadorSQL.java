package DAO;

import models.Trabajador;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DAOTrabajadorSQL implements DAOTrabajador {
    private final DAOPedidoSQL daoPedidoSQL = new DAOPedidoSQL();

    @Override
    public ArrayList<Trabajador> readAll(DAOManager dao) {
        ArrayList<Trabajador> lista = new ArrayList<>();
        String sentencia = "SELECT * FROM Trabajador";

        try {
            dao.open();
            PreparedStatement ps = dao.getConn().prepareStatement(sentencia);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Trabajador(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("pass"),
                            rs.getString("email"),
                            rs.getInt("movil")
                    ));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                dao.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        for (Trabajador t : lista) {
            t.setPedidosAsignados(daoPedidoSQL.readPedidosByIdTrabajador(dao, t));
            //t.getPedidosAsignados().addAll(daoPedidoSQL.readPedidosByIdTrabajador(dao, t));
        }

        return lista;
    }

    @Override
    public boolean insert(DAOManager dao, Trabajador trabajador) {
        try {
            dao.open();
            String sentencia = "INSERT INTO `Trabajador` (`id`, `nombre`, `pass`, `email`, `movil`) VALUES ('" +
                    trabajador.getId() + "', '" + trabajador.getNombre() + "', '" + trabajador.getPass() + "', '" +
                    trabajador.getEmail() + "', '" + trabajador.getMovil() + "')";
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
    public boolean update(DAOManager dao, Trabajador trabajador) {
        try {
            dao.open();
            String sentencia = "UPDATE Trabajador SET `nombre` = '" + trabajador.getNombre() + "', `pass` = '" +
                    trabajador.getPass() + "', `email` = '" + trabajador.getEmail() + "', `movil` = '" + trabajador.getMovil()
                    + "' WHERE `Trabajador`.`id` = " + trabajador.getId();
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
    public boolean delete(DAOManager dao, Trabajador trabajador) {
        try {
            dao.open();
            String sentencia = "DELETE FROM Trabajador WHERE `Trabajador`.`id` = '" + trabajador.getId() + "'";
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
    public Trabajador buscaTrabajadorPrueba(DAOManager dao) {
        try {
            dao.open();
            Trabajador trabajador = null;
            String sentencia = "SELECT * FROM `Trabajador` WHERE `Trabajador`.`id` = '" + 100000 + "'";
            PreparedStatement ps = dao.getConn().prepareStatement(sentencia);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    trabajador = new Trabajador(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("pass"),
                            rs.getString("email"),
                            rs.getInt("movil")
                    );
                }
            }
            return trabajador;
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
}
