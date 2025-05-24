package DAO;

import models.Admin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DAOAdminSQL implements DAOAdmin {
    @Override
    public ArrayList<Admin> readAdmin(DAOManager dao) {
        ArrayList<Admin> lista = new ArrayList<>();
        String sentencia = "Select * from Admin";

        try {
            dao.open();
            PreparedStatement ps = dao.getConn().prepareStatement(sentencia);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Admin(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("clave"),
                            rs.getString("email")
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
        return lista;
    }

    @Override
    public boolean insert(DAOManager dao, Admin admin) {
        try {
            dao.open();
            String sentencia = "INSERT INTO `Admin` (`id`, `nombre`, `clave`, `email`) VALUES ('" + admin.getId() +
                    "', '" + admin.getNombre() + "', '" + admin.getClave() + "', '" + admin.getEmail() + "')";
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
