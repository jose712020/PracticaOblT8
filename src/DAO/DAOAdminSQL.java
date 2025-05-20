package DAO;

import models.Admin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DAOAdminSQL implements DAOAdmin {
    @Override
    public Admin readAdmin(DAOManager dao) {
        Admin admin = null;
        String sentencia = "Select * from Admin";

        try {
            dao.open();
            PreparedStatement ps = dao.getConn().prepareStatement(sentencia);
            try (ResultSet rs = ps.executeQuery()){
               while (rs.next()) {
                   admin = new Admin(
                           rs.getInt("id"),
                           rs.getString("nombre"),
                           rs.getString("clave"),
                           rs.getString("email")
                   );
                }
                dao.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return admin;
    }
}
