
package Controlador;

import DAO.DetalleVentaDAO;
import Modelo.DetalleVenta;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Edith
 */
public class DetalleVentaControlador {

    private final DetalleVentaDAO detalleVentaDAO;

    public DetalleVentaControlador() {
        this.detalleVentaDAO = new DetalleVentaDAO();
    }

    // Método para crear un nuevo detalle de venta
    public void crearDetalleVenta(int idVenta, int idProducto, int cantidad, float precioUnitario) {
        try {
            DetalleVenta detalle = new DetalleVenta();
            detalle.setIdVenta(idVenta);
            detalle.setIdProducto(idProducto);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(precioUnitario);
            detalleVentaDAO.crearDetalleVenta(detalle);
            JOptionPane.showMessageDialog(null, "Detalle de venta creado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al crear el detalle de venta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para obtener todos los detalles de venta
    public List<DetalleVenta> obtenerTodosDetallesVenta() {
        try {
            return detalleVentaDAO.leerTodosDetallesVenta();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al leer los detalles de venta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // Método para actualizar un detalle de venta existente
    public void actualizarDetalleVenta(int idDetalleVenta, int idVenta, int idProducto, int cantidad, float precioUnitario) {
        try {
            DetalleVenta detalle = new DetalleVenta();
            detalle.setIdDetalleVenta(idDetalleVenta);
            detalle.setIdVenta(idVenta);
            detalle.setIdProducto(idProducto);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(precioUnitario);
            detalleVentaDAO.actualizarDetalleVenta(detalle);
            JOptionPane.showMessageDialog(null, "Detalle de venta actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el detalle de venta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para eliminar un detalle de venta
    public void eliminarDetalleVenta(int idDetalleVenta) {
        try {
            detalleVentaDAO.eliminarDetalleVenta(idDetalleVenta);
            JOptionPane.showMessageDialog(null, "Detalle de venta eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el detalle de venta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método main para pruebas
    public static void main(String[] args) {
        DetalleVentaControlador controlador = new DetalleVentaControlador();

        // Crear un detalle de venta
        controlador.crearDetalleVenta(1, 4, 44, 33.22f);

        // Leer todos los detalles de venta
        List<DetalleVenta> detalles = controlador.obtenerTodosDetallesVenta();
        if (detalles != null) {
            System.out.println("Lista de detalles de venta:");
            for (DetalleVenta d : detalles) {
                System.out.println("ID: " + d.getIdDetalleVenta()
                        + ", Venta: " + d.getIdVenta()
                        + ", Producto: " + d.getIdProducto()
                        + ", Cantidad: " + d.getCantidad());
            }
        }

        // Actualizar un detalle de venta (suponiendo que ID 1 existe)
        controlador.actualizarDetalleVenta(1, 1, 4, 50, 35.50f);

        // Eliminar un detalle de venta
        controlador.eliminarDetalleVenta(1);
    }
}