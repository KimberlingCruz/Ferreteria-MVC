package DAO;


import Modelo.Compra;
import Modelo.DetalleCompra;
import Util.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class DetalleCompraDAO {
   public void crearDetalleCompra(DetalleCompra detalle)  throws SQLException {
    String sql = """
        INSERT INTO Detalles_Compras (
            id_compra, 
            id_producto, 
            cantidad, 
            precio_unitario
        ) VALUES (?, ?, ?, ?)""";
    
    try (Connection c = ConexionDB.getConnection();
         PreparedStatement stmt = c.prepareStatement(sql)) {
        stmt.setInt(1, detalle.getIdCompra());
        stmt.setInt(2, detalle.getIdProducto());
        stmt.setInt(3, detalle.getCantidad());
        stmt.setFloat(4, detalle.getPrecioUnitario());
        stmt.executeUpdate();
    }
}


    public List<DetalleCompra> leerTodosDetallesCompra() throws SQLException {
        String sql = "SELECT * FROM Detalles_Compras";
        List<DetalleCompra> detalles = new ArrayList<>();

        try (Connection c = ConexionDB.getConnection();
             PreparedStatement stmt = c.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                DetalleCompra detalle = new DetalleCompra();
                detalle.setIdDetalleCompra(rs.getInt("id_detalle_compra"));
                detalle.setIdCompra(rs.getInt("id_compra"));
                detalle.setIdProducto(rs.getInt("id_producto"));
                detalle.setCantidad(rs.getInt("cantidad"));
                detalle.setPrecioUnitario(rs.getFloat("precio_unitario"));
                detalles.add(detalle);
            }
        }
        return detalles;
    }
   public void actualizarDetalleCompra(DetalleCompra detalle) throws SQLException {
    String sql = "UPDATE Detalles_Compras SET id_compra = ?, id_producto = ?, cantidad = ?, precio_unitario = ? WHERE id_detalle_compra = ?";
    
    try (Connection c = ConexionDB.getConnection();
         PreparedStatement stmt = c.prepareStatement(sql)) {
        stmt.setInt(1, detalle.getIdCompra());
        stmt.setInt(2, detalle.getIdProducto());
        stmt.setInt(3, detalle.getCantidad());
        stmt.setFloat(4, detalle.getPrecioUnitario());
        stmt.setInt(5, detalle.getIdDetalleCompra());
        stmt.executeUpdate();
    }
}

// Método para eliminar un detalle de compra
public void eliminarDetalleCompra(int idDetalleCompra) throws SQLException {
    String sql = "DELETE FROM Detalles_Compras WHERE id_detalle_compra = ?";
    
    try (Connection c = ConexionDB.getConnection();
         PreparedStatement stmt = c.prepareStatement(sql)) {
        stmt.setInt(1, idDetalleCompra);
        stmt.executeUpdate();
    }
}



// Método Main
public static void main(String[] args) {
    try {
        DetalleCompraDAO dao = new DetalleCompraDAO();
        
        // Actualizar un detalle de compra
        DetalleCompra detalle = new DetalleCompra();
        detalle.setIdDetalleCompra(1); // ID existente
        detalle.setIdCompra(1);
        detalle.setIdProducto(2);
        detalle.setCantidad(5);
        detalle.setPrecioUnitario(100.0f);
        dao.actualizarDetalleCompra(detalle);
        System.out.println("Detalle de compra actualizado.");
        
        // Eliminar un detalle de compra
        dao.eliminarDetalleCompra(2); // ID a eliminar
        System.out.println("Detalle de compra eliminado.");
        
        // Leer y mostrar todos los detalles de compra para verificar
        List<DetalleCompra> detalles = dao.leerTodosDetallesCompra();
        System.out.println("Lista de detalles de compra:");
        for (DetalleCompra det : detalles) {
            System.out.println("ID: " + det.getIdDetalleCompra() + 
                               ", Compra ID: " + det.getIdCompra() + 
                               ", Producto ID: " + det.getIdProducto() + 
                               ", Cantidad: " + det.getCantidad() + 
                               ", Precio Unitario: " + det.getPrecioUnitario());
        }
    } catch (SQLException e) {
        System.err.println("Error: " + e.getMessage());
    }
}

 
}