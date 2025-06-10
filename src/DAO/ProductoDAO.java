/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Modelo.Producto;
import Util.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Edith Cruz
 */
public class ProductoDAO {
public void crearProducto(Producto producto) throws SQLException {
    String sql = """
        INSERT INTO Productos (
            nombre_producto, 
            descripcion_producto, 
            id_categoria, 
            precio_unitario, 
            stock, 
            imagen
        ) VALUES (?, ?, ?, ?, ?, ?)""";
    
    try (Connection c = ConexionDB.getConnection();
         PreparedStatement stmt = c.prepareStatement(sql)) {
        stmt.setString(1, producto.getNombreProducto());
        stmt.setString(2, producto.getDescripcionProducto());
        stmt.setInt(3, producto.getIdCategoria());
        stmt.setFloat(4, producto.getPrecioUnitario());
        stmt.setInt(5, producto.getStock());
        stmt.setString(6, producto.getImagen());
        stmt.executeUpdate();
    }
}
    public List<Producto> leerTodosProductos() throws SQLException {
        String sql = "SELECT * FROM Productos";
        List<Producto> productos = new ArrayList<>();

        try (Connection c = ConexionDB.getConnection();
             PreparedStatement stmt = c.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Producto producto = new Producto();
                producto.setIdProducto(rs.getInt("id_producto"));
                producto.setNombreProducto(rs.getString("nombre_producto"));
                producto.setDescripcionProducto(rs.getString("descripcion_producto"));
                producto.setIdCategoria(rs.getInt("id_categoria"));
                producto.setPrecioUnitario(rs.getFloat("precio_unitario"));
                producto.setStock(rs.getInt("stock"));
                producto.setImagen(rs.getString("imagen"));
                productos.add(producto);
            }
        }
        return productos;
    }
    
    public Producto obtenerProductoPorId(int idProducto) throws SQLException {
    String sql = "SELECT * FROM Productos WHERE id_producto = ?";
    Producto producto = null;

    try (Connection c = ConexionDB.getConnection(); PreparedStatement stmt = c.prepareStatement(sql)) {
        stmt.setInt(1, idProducto);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                producto = new Producto();
                producto.setIdProducto(rs.getInt("id_producto"));
                producto.setNombreProducto(rs.getString("nombre_producto"));
                producto.setDescripcionProducto(rs.getString("descripcion_producto"));
                producto.setIdCategoria(rs.getInt("id_categoria"));
                producto.setPrecioUnitario(rs.getFloat("precio_unitario"));
                producto.setStock(rs.getInt("stock"));
                producto.setImagen(rs.getString("imagen"));
            }
        }
    }
    return producto;
}
    
public void actualizarProducto(Producto producto) throws SQLException {
    String sql = "UPDATE Productos SET nombre_producto = ?, descripcion_producto = ?, id_categoria = ?, precio_unitario = ?, stock = ?, imagen = ? WHERE id_producto = ?";
    
    try (Connection c = ConexionDB.getConnection();
         PreparedStatement stmt = c.prepareStatement(sql)) {
        stmt.setString(1, producto.getNombreProducto());
        stmt.setString(2, producto.getDescripcionProducto());
        stmt.setInt(3, producto.getIdCategoria());
        stmt.setFloat(4, producto.getPrecioUnitario());
        stmt.setInt(5, producto.getStock());
        stmt.setString(6, producto.getImagen());
        stmt.setInt(7, producto.getIdProducto());
        stmt.executeUpdate();
    }
}

// Método para eliminar un producto
public void eliminarProducto(int idProducto) throws SQLException {
    String sql = "DELETE FROM Productos WHERE id_producto = ?";
    
    try (Connection c = ConexionDB.getConnection();
         PreparedStatement stmt = c.prepareStatement(sql)) {
        stmt.setInt(1, idProducto);
        stmt.executeUpdate();
    }
}

// Método Main
public static void main(String[] args) {
    try {
        ProductoDAO dao = new ProductoDAO();
        
        // Actualizar un producto
        Producto producto = new Producto();
        producto.setIdProducto(1); // ID existente
        producto.setNombreProducto("Laptop Actualizada");
        producto.setDescripcionProducto("Laptop de alta gama");
        producto.setIdCategoria(1);
        producto.setPrecioUnitario(1200.0f);
        producto.setStock(50);
        producto.setImagen("laptop.jpg");
        dao.actualizarProducto(producto);
        System.out.println("Producto actualizado.");
        
        // Eliminar un producto
        dao.eliminarProducto(1); // ID a eliminar
        System.out.println("Producto eliminado.");
        
        // Leer y mostrar todos los productos para verificar
        List<Producto> productos = dao.leerTodosProductos();
        System.out.println("Lista de productos:");
        for (Producto prod : productos) {
            System.out.println("ID: " + prod.getIdProducto() + 
                               ", Nombre: " + prod.getNombreProducto() + 
                               ", Descripción: " + prod.getDescripcionProducto() + 
                               ", Categoría ID: " + prod.getIdCategoria() + 
                               ", Precio: " + prod.getPrecioUnitario() + 
                               ", Stock: " + prod.getStock() + 
                               ", Imagen: " + prod.getImagen());
        }
    } catch (SQLException e) {
        System.err.println("Error: " + e.getMessage());
    }
}
}