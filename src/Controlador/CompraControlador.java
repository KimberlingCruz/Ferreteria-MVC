
package Controlador;

import DAO.CompraDAO;
import DAO.DetalleCompraDAO;
import Modelo.Compra;
import Modelo.DetalleCompra;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Edith Cruz
 */
public class CompraControlador {
    
    private final CompraDAO compraDAO;
    private final DetalleCompraDAO detalleCompraDAO;

    public CompraControlador() {
        this.compraDAO = new CompraDAO();
        this.detalleCompraDAO = new DetalleCompraDAO();
    }

    // Método para crear una nueva compra con sus detalles
    public void crearCompra(int idEmpleado, Date fechaCompra, float totalCompra) {
        try {
            Compra compra = new Compra();
            compra.setIdEmpleado(idEmpleado);
            compra.setFechaCompra(fechaCompra);
            compra.setTotalCompra(totalCompra);
            int idCompra = compraDAO.crearCompra(compra);

            if (idCompra == -1) {
                throw new SQLException("No se pudo obtener el ID de la compra.");
            }

           

            JOptionPane.showMessageDialog(null, "Compra y detalles creados exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al crear la compra: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para obtener todas las compras
    public List<Compra> obtenerTodasCompras() {
        try {
            return compraDAO.leerTodasCompras();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al leer las compras: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // Método para actualizar una compra existente
    public void actualizarCompra(int idCompra, int idEmpleado, Date fechaCompra, float totalCompra) {
        try {
            Compra compra = new Compra();
            compra.setIdCompra(idCompra);
            compra.setIdEmpleado(idEmpleado);
            compra.setFechaCompra(fechaCompra);
            compra.setTotalCompra(totalCompra);
            compraDAO.actualizarCompra(compra);
            JOptionPane.showMessageDialog(null, "Compra actualizada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar la compra: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para eliminar una compra
    public void eliminarCompra(int idCompra) {
        try {
            compraDAO.eliminarCompra(idCompra);
            JOptionPane.showMessageDialog(null, "Compra eliminada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar la compra: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método main para pruebas
    public static void main(String[] args) {
        CompraControlador controlador = new CompraControlador();

        // Crear una lista de detalles de compra
        List<DetalleCompra> detalles = new ArrayList<>();
        DetalleCompra detalle1 = new DetalleCompra();
        detalle1.setIdProducto(4);
        detalle1.setCantidad(11);
        detalle1.setPrecioUnitario(150.50f);
        detalles.add(detalle1);

        // Crear una compra con detalles
        controlador.crearCompra(1, new Date(), 150.50f);

        // Leer todas las compras
        List<Compra> compras = controlador.obtenerTodasCompras();
        if (compras != null) {
            System.out.println("Lista de compras:");
            for (Compra c : compras) {
                System.out.println("ID: " + c.getIdCompra()
                        + ", Empleado: " + c.getIdEmpleado()
                        + ", Total: " + c.getTotalCompra());
            }
        }

        // Actualizar una compra (suponiendo que ID 1 existe)
        controlador.actualizarCompra(1, 2, new Date(), 200.75f);

        // Eliminar una compra
        controlador.eliminarCompra(1);
    }
}