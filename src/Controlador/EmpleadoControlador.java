package Controlador;

import DAO.EmpleadoDAO;
import Modelo.Empleado;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Edith
 */
public class EmpleadoControlador {

    private final EmpleadoDAO empleadoDAO;

    public EmpleadoControlador() {
        this.empleadoDAO = new EmpleadoDAO();
    }

    // Método para crear un nuevo empleado
    public void crearEmpleado(String primerNombre, String segundoNombre, String primerApellido,
            String segundoApellido, String celular, String cargo, Date fechaContratacion) {
        try {
            Empleado empleado = new Empleado();
            empleado.setPrimerNombre(primerNombre);
            empleado.setSegundoNombre(segundoNombre);
            empleado.setPrimerApellido(primerApellido);
            empleado.setSegundoApellido(segundoApellido);
            empleado.setCelular(celular);
            empleado.setCargo(cargo);
            empleado.setFechaContratacion(fechaContratacion);
            empleadoDAO.crearEmpleado(empleado);
            JOptionPane.showMessageDialog(null, "Empleado creado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al crear el empleado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para obtener todos los empleados
    public List<Empleado> obtenerTodosEmpleados() {
        try {
            return empleadoDAO.leerTodosEmpleados();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al leer los empleados: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public Empleado obtenerEmpleadoPorId(int idEmpleado) {
        try {
            return empleadoDAO.obtenerEmpleadoPorId(idEmpleado);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar el empleado: " + e.getMessage());
            return null;
        }
    }

    // Método para actualizar un empleado existente
    public void actualizarEmpleado(int idEmpleado, String primerNombre, String segundoNombre, String primerApellido,
            String segundoApellido, String celular, String cargo, Date fechaContratacion) {
        try {
            Empleado empleado = new Empleado();
            empleado.setIdEmpleado(idEmpleado);
            empleado.setPrimerNombre(primerNombre);
            empleado.setSegundoNombre(segundoNombre);
            empleado.setPrimerApellido(primerApellido);
            empleado.setSegundoApellido(segundoApellido);
            empleado.setCelular(celular);
            empleado.setCargo(cargo);
            empleado.setFechaContratacion(fechaContratacion);
            empleadoDAO.actualizarEmpleado(empleado);
            JOptionPane.showMessageDialog(null, "Empleado actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el empleado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para eliminar un empleado
    public void eliminarEmpleado(int idEmpleado) {
        try {
            empleadoDAO.eliminarEmpleado(idEmpleado);
            JOptionPane.showMessageDialog(null, "Empleado eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el empleado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método main para pruebas
    public static void main(String[] args) {
        EmpleadoControlador controlador = new EmpleadoControlador();

        // Crear un empleado
        controlador.crearEmpleado("María", "Luisa", "Rodríguez", "López", "87654321", "Vendedor", new Date());

        // Leer todos los empleados
        List<Empleado> empleados = controlador.obtenerTodosEmpleados();
        if (empleados != null) {
            System.out.println("Lista de empleados:");
            for (Empleado e : empleados) {
                System.out.println("ID: " + e.getIdEmpleado()
                        + ", Nombre: " + e.getPrimerNombre() + " " + e.getPrimerApellido()
                        + ", Cargo: " + e.getCargo());
            }
        }

        // Actualizar un empleado (suponiendo que ID 1 existe)
        controlador.actualizarEmpleado(1, "María", "Elena", "Rodríguez", "Gómez", "12345678", "Gerente", new Date());

        // Eliminar un empleado
        controlador.eliminarEmpleado(1);
    }
}