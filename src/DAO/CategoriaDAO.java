
package DAO;

import Modelo.Categoria;
import Util.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {
    public void crearCategoria(Categoria categoria) throws SQLException {
    String sql = """
        INSERT INTO Categorias (
            nombre_categoria, 
            descripcion_categoria
        ) VALUES (?, ?) """;

    try (Connection c = ConexionDB.getConnection();
         PreparedStatement stmt = c.prepareStatement(sql)) {
        stmt.setString(1, categoria.getNombreCategoria());
        stmt.setString(2, categoria.getDescripcionCategoria());
        stmt.executeUpdate();
    }
}

public List<Categoria> leerTodasCategorias() throws SQLException {
        String sql = "SELECT * FROM Categorias";
        List<Categoria> categorias = new ArrayList<>();

        try (Connection c = ConexionDB.getConnection();
            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setIdCategoria(rs.getInt("id_categoria"));
                categoria.setNombreCategoria(rs.getString("nombre_categoria"));
                categoria.setDescripcionCategoria(rs.getString("descripcion_categoria"));
                categorias.add(categoria);
            }
        }
        return categorias;
    }
public void actualizarCategoria(Categoria categoria) throws SQLException {
    String sql = "UPDATE Categorias SET nombre_categoria = ?, descripcion_categoria = ? WHERE id_categoria = ?";
    
    try (Connection c = ConexionDB.getConnection();
         PreparedStatement stmt = c.prepareStatement(sql)) {
        stmt.setString(1, categoria.getNombreCategoria());
        stmt.setString(2, categoria.getDescripcionCategoria());
        stmt.setInt(3, categoria.getIdCategoria());
        stmt.executeUpdate();
    }
}

public void eliminarCategoria(int idCategoria) throws SQLException {
    String sql = "DELETE FROM Categorias WHERE id_categoria = ?";
    
    try (Connection c = ConexionDB.getConnection();
         PreparedStatement stmt = c.prepareStatement(sql)) {
        stmt.setInt(1, idCategoria);
        stmt.executeUpdate();
    }
}

// Método Main
public static void main(String[] args) {
    try {
        CategoriaDAO dao = new CategoriaDAO();
        
        // Actualizar una categoría
        Categoria categoria = new Categoria();
        categoria.setIdCategoria(1); // ID existente
        categoria.setNombreCategoria("Electrónica Actualizada");
        categoria.setDescripcionCategoria("Productos electrónicos modernos");
        dao.actualizarCategoria(categoria);
        System.out.println("Categoría actualizada.");
        
    
        dao.eliminarCategoria(1); 
        System.out.println("Categoría eliminada.");
        
        // Leer y mostrar todas las categorías para verificar
        List<Categoria> categorias = dao.leerTodasCategorias();
        System.out.println("\nLista de categorías:");
        for (Categoria cat : categorias) {
            System.out.println("ID: " + cat.getIdCategoria() + 
                               ", Nombre: " + cat.getNombreCategoria() + 
                               ", Descripción: " + cat.getDescripcionCategoria());
        }
    } catch (SQLException e) {
        System.err.println("Error: " + e.getMessage());
    }
}


}