package DAO;

import Modelo.Venta;
import Util.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

public class VentaDAO {
    public int crearVenta(Venta venta) throws SQLException {
    String sql = """
        INSERT INTO Ventas (
            id_cliente, 
            id_empleado, 
            fecha_venta, 
            total_venta
        ) VALUES (?, ?, ?, ?)""";
        int generatedId = -1;

        try (Connection c = ConexionDB.getConnection(); PreparedStatement stmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, venta.getIdCliente());
            stmt.setInt(2, venta.getIdEmpleado());
            stmt.setTimestamp(3, new java.sql.Timestamp(venta.getFechaVenta().getTime()));
            stmt.setFloat(4, venta.getTotalVenta());
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
 public List<Venta> leerTodasVentas() throws SQLException {
        String sql = "SELECT * FROM Ventas";
        List<Venta> ventas = new ArrayList<>();

        try (Connection c = ConexionDB.getConnection();
             PreparedStatement stmt = c.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Venta venta = new Venta();
                venta.setIdCliente(rs.getInt("id_cliente"));
                venta.setIdEmpleado(rs.getInt("id_empleado"));
                venta.setFechaVenta(rs.getTimestamp("fecha_venta"));
                venta.setTotalVenta(rs.getFloat("total_venta"));
                ventas.add(venta);
            }
        }
        return ventas;
    }

public void actualizarVenta(Venta venta) throws SQLException {
    String sql = "UPDATE Ventas SET id_cliente = ?, id_empleado  = ?, fecha_venta = ?, total_venta = ? WHERE id_venta = ?";
    
    try (Connection c = ConexionDB.getConnection();
         PreparedStatement stmt = c.prepareStatement(sql)) {
        stmt.setInt(1, venta.getIdCliente());
        stmt.setInt(2, venta.getIdEmpleado());
        stmt.setTimestamp(3, new java.sql.Timestamp(venta.getFechaVenta().getTime()));
        stmt.setFloat(4, venta.getTotalVenta());
        stmt.setInt(5, venta.getIdVenta());
        stmt.executeUpdate();
    }
}

// Método para eliminar una venta
public void eliminarVenta(int idVenta) throws SQLException {
    String sql = "DELETE FROM Ventas WHERE id_Venta = ?";
    
    try (Connection c = ConexionDB.getConnection();
         PreparedStatement stmt = c.prepareStatement(sql)) {
        stmt.setInt(1, idVenta);
        stmt.executeUpdate();
    }
}

// Método Main
public static void main(String[] args) {
    try {
        VentaDAO dao = new VentaDAO();
        
        // Actualizar una venta
        Venta venta = new Venta();
        venta.setIdVenta(1); // ID existente
        venta.setIdCliente(1);
        venta.setIdEmpleado(2);
        venta.setFechaVenta(new java.util.Date());
        venta.setTotalVenta(500.0f);
        dao.actualizarVenta(venta);
        System.out.println("Venta actualizada.");
        
        // Eliminar una venta
        dao.eliminarVenta(1); // ID a eliminar
        System.out.println("Venta eliminada.");
       
        // Leer y mostrar todas las ventas para verificar
        List<Venta> ventas = dao.leerTodasVentas();
        System.out.println("Lista de ventas:");
        for (Venta ven : ventas) {
            System.out.println("ID: " + ven.getIdVenta() + 
                               ", Cliente ID: " + ven.getIdCliente() + 
                               ", Empleado ID: " + ven.getIdEmpleado() + 
                               ", Fecha: " + ven.getFechaVenta() + 
                               ", Total: " + ven.getTotalVenta());
        }
    } catch (SQLException e) {
        System.err.println("Error: " + e.getMessage());
    }
}
}