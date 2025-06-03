
package DAO;

import Modelo.DetalleVenta;
import Util.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Edith
 */
public class DetalleVentaDAO {
    public int crearDetalleVenta(DetalleVenta detalle) throws SQLException {
    String sql = """
        INSERT INTO Detalles_Ventas (
            id_venta, 
            id_producto, 
            cantidad, 
            precio_unitario
        ) VALUES (?, ?, ?, ?)""";
         int generatedId = -1;
         
    try (Connection c = ConexionDB.getConnection();
         PreparedStatement stmt = c.prepareStatement(sql)) {
        stmt.setInt(1, detalle.getIdVenta());
        stmt.setInt(2, detalle.getIdProducto());
        stmt.setInt(3, detalle.getCantidad());
        stmt.setFloat(4, detalle.getPrecioUnitario());
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
    
    public List<DetalleVenta> leerTodosDetallesVenta() throws SQLException {
        String sql = "SELECT * FROM Detalles_Ventas";
        List<DetalleVenta> detalles = new ArrayList<>();

        try (Connection c = ConexionDB.getConnection();
             PreparedStatement stmt = c.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                DetalleVenta detalle = new DetalleVenta();
                detalle.setIdDetalleVenta(rs.getInt("id_detalle_venta"));
                detalle.setIdVenta(rs.getInt("id_venta"));
                detalle.setIdProducto(rs.getInt("id_producto"));
                detalle.setCantidad(rs.getInt("cantidad"));
                detalle.setPrecioUnitario(rs.getFloat("precio_unitario"));
                detalles.add(detalle);
            }
        }
        return detalles;
    }
public void actualizarDetalleVenta(DetalleVenta detalle) throws SQLException {
    String sql = "UPDATE Detalles_Ventas SET id_venta = ?, id_producto = ?, cantidad = ?, precio_unitario = ? WHERE id_detalle_venta = ?";
    
    try (Connection c = ConexionDB.getConnection();
         PreparedStatement stmt = c.prepareStatement(sql)) {
        stmt.setInt(1, detalle.getIdVenta());
        stmt.setInt(2, detalle.getIdProducto());
        stmt.setInt(3, detalle.getCantidad());
        stmt.setFloat(4, detalle.getPrecioUnitario());
        stmt.setInt(5, detalle.getIdDetalleVenta());
        stmt.executeUpdate();
    }
}

// Método para eliminar un detalle de venta
public void eliminarDetalleVenta(int idDetalleVenta) throws SQLException {
    String sql = "DELETE FROM Detalles_Ventas WHERE id_detalle_venta = ?";
    
    try (Connection c = ConexionDB.getConnection();
         PreparedStatement stmt = c.prepareStatement(sql)) {
        stmt.setInt(1, idDetalleVenta);
        stmt.executeUpdate();
    }
}

// Método Main
public static void main(String[] args) {
    try {
        DetalleVentaDAO dao = new DetalleVentaDAO();
        
        // Actualizar un detalle de venta
        DetalleVenta detalle = new DetalleVenta();
        detalle.setIdDetalleVenta(1); // ID existente
        detalle.setIdVenta(1);
        detalle.setIdProducto(3);
        detalle.setCantidad(2);
        detalle.setPrecioUnitario(200.0f);
        dao.actualizarDetalleVenta(detalle);
        System.out.println("Detalle de venta actualizado.");
        
        // Eliminar un detalle de venta
        dao.eliminarDetalleVenta(1); // ID a eliminar
        System.out.println("Detalle de venta eliminado.");
        
        // Leer y mostrar todos los detalles de venta para verificar
        List<DetalleVenta> detalles = dao.leerTodosDetallesVenta();
        System.out.println("Lista de detalles de venta:");
        for (DetalleVenta det : detalles) {
            System.out.println("ID: " + det.getIdDetalleVenta() + 
                               ", Venta ID: " + det.getIdVenta() + 
                               ", Producto ID: " + det.getIdProducto() + 
                               ", Cantidad: " + det.getCantidad() + 
                               ", Precio Unitario: " + det.getPrecioUnitario());
        }
    } catch (SQLException e) {
        System.err.println("Error: " + e.getMessage());
    }
}
}