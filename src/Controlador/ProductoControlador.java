package Controlador;

import DAO.ProductoDAO;
import Modelo.Producto;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;


public class ProductoControlador {

    private final ProductoDAO productoDAO;

    public ProductoControlador() {
        this.productoDAO = new ProductoDAO();
    }

    // Método para crear un nuevo producto
    public void crearProducto(String nombreProducto, String descripcionProducto, int idCategoria,
            float precioUnitario, int stock, String imagen) {
        try {
            Producto producto = new Producto();
            producto.setNombreProducto(nombreProducto);
            producto.setDescripcionProducto(descripcionProducto);
            producto.setIdCategoria(idCategoria);
            producto.setPrecioUnitario(precioUnitario);
            producto.setStock(stock);
            producto.setImagen(imagen);
            productoDAO.crearProducto(producto);
            JOptionPane.showMessageDialog(null, "Producto creado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al crear el producto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para obtener todos los productos
    public List<Producto> obtenerTodosProductos() {
        try {
            return productoDAO.leerTodosProductos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al leer los productos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
     public Producto obtenerProductoPorId(int idProducto) {
        try {
            return productoDAO.obtenerProductoPorId(idProducto);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener el producto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // Método para actualizar un producto existente
    public void actualizarProducto(int idProducto, String nombreProducto, String descripcionProducto, int idCategoria,
            float precioUnitario, int stock, String imagen) {
        try {
            Producto producto = new Producto();
            producto.setIdProducto(idProducto);
            producto.setNombreProducto(nombreProducto);
            producto.setDescripcionProducto(descripcionProducto);
            producto.setIdCategoria(idCategoria);
            producto.setPrecioUnitario(precioUnitario);
            producto.setStock(stock);
            producto.setImagen(imagen);
            productoDAO.actualizarProducto(producto);
            JOptionPane.showMessageDialog(null, "Producto actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el producto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para eliminar un producto
    public void eliminarProducto(int idProducto) {
        try {
            productoDAO.eliminarProducto(idProducto);
            JOptionPane.showMessageDialog(null, "Producto eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el producto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método main para pruebas
    public static void main(String[] args) {
        ProductoControlador controlador = new ProductoControlador();

        // Crear un producto
        controlador.crearProducto("Laptop", "Laptop de alta gama", 1, 1500.99f, 10, "ruta/laptop.jpg");

        // Leer todos los productos
        List<Producto> productos = controlador.obtenerTodosProductos();
        if (productos != null) {
            System.out.println("Lista de productos:");
            for (Producto p : productos) {
                System.out.println("ID: " + p.getIdProducto() +
                               ", Nombre: " + p.getNombreProducto() + 
                               ", Descripción: " + p.getDescripcionProducto() + 
                               ", Categoría ID: " + p.getIdCategoria() + 
                               ", Precio: " + p.getPrecioUnitario() + 
                               ", Stock: " + p.getStock() + 
                               ", Imagen: " + p.getImagen());
            }
        }

        // Actualizar un producto (suponiendo que ID 1 existe)
        controlador.actualizarProducto(1, "Laptop Pro", "Laptop mejorada", 1, 1800.50f, 8, "ruta/laptop_pro.jpg");

        // Eliminar un producto
        controlador.eliminarProducto(1);
    }
}