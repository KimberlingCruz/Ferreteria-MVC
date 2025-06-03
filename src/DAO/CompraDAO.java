
package DAO;

import Modelo.Compra;
import Util.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;
/**
 *
 * @author Edith
 */
public class CompraDAO {
 public int crearCompra(Compra compra) throws SQLException {
        String sql = """
        INSERT INTO Compras (
            id_empleado, 
            fecha_compra, 
            total_compra
        ) VALUES (?, ?, ?)""";
        int generatedId = -1;

        try (Connection c = ConexionDB.getConnection(); PreparedStatement stmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, compra.getIdEmpleado());
            stmt.setDate(2, new java.sql.Date(compra.getFechaCompra().getTime()));
            stmt.setFloat(3, compra.getTotalCompra());
            stmt.executeUpdate();

            // Obtener el ID generado
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
            }
        }
        return generatedId;
    }

  public List<Compra> leerTodasCompras() throws SQLException {
        String sql = "SELECT * FROM Compras";
        List<Compra> compras = new ArrayList<>();

        try (Connection c = ConexionDB.getConnection();
             PreparedStatement stmt = c.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Compra compra = new Compra();
                compra.setIdCompra(rs.getInt("id_compra"));
                compra.setIdEmpleado(rs.getInt("id_empleado"));
                compra.setFechaCompra(rs.getDate("fecha_compra"));
                compra.setTotalCompra(rs.getFloat("total_compra"));
                compras.add(compra);
            }
        }
        return compras;
    }

    
    public void actualizarCompra(Compra compra) throws SQLException {
    String sql = "UPDATE Compras SET id_empleado = ?, fecha_compra = ?, total_compra = ? WHERE id_compra = ?";
    
    try (Connection c = ConexionDB.getConnection();
         PreparedStatement stmt = c.prepareStatement(sql)) {
        stmt.setInt(1, compra.getIdEmpleado());
        stmt.setDate(2, new java.sql.Date(compra.getFechaCompra().getTime()));
        stmt.setFloat(3, compra.getTotalCompra());
        stmt.setInt(4, compra.getIdCompra());
        stmt.executeUpdate();
    }
}

// Método para eliminar una compra
public void eliminarCompra(int idCompra) throws SQLException {
    String sql = "DELETE FROM Compras WHERE id_compra = ?";
    
    try (Connection c = ConexionDB.getConnection();
         PreparedStatement stmt = c.prepareStatement(sql)) {
        stmt.setInt(1, idCompra);
        stmt.executeUpdate();
    }
}

// Método Main
  public static void main(String[] args) {
    try {
        CompraDAO dao = new CompraDAO();
        
        // Actualizar una compra
        Compra compra = new Compra();
        compra.setIdCompra(1); // ID existente
        compra.setIdEmpleado(2);
        compra.setFechaCompra(new java.util.Date());
        compra.setTotalCompra(1500.50f);
        dao.actualizarCompra(compra);
        System.out.println("Compra actualizada.");
        
        // Leer y mostrar todas las compras para verificar
        List<Compra> compras = dao.leerTodasCompras();
        System.out.println("Lista de compras:");
        for (Compra comp : compras) {
            System.out.println("ID: " + comp.getIdCompra() + 
                               ", Empleado ID: " + comp.getIdEmpleado()+ 
                               ", Fecha: " + comp.getFechaCompra() + 
                               ", Total: " + comp.getTotalCompra());
        }
    } catch (SQLException e) {
        System.err.println("Error: " + e.getMessage());
    }
}

}