
package Controlador;

import DAO.UsuarioDAO;
import Modelo.Usuario;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author welvin
 */
public class UsuarioControlador {
    

    private final UsuarioDAO usuarioDAO;

    public UsuarioControlador() {
        this.usuarioDAO = new UsuarioDAO();
    }

    // Método para crear un nuevo usuario
    public void crearUsuario(String usuario, String contrasena) {
        try {
            Usuario user = new Usuario();
            user.setUsuario(usuario);
            user.setContrasena(contrasena);
            usuarioDAO.crearUsuario(user);
            JOptionPane.showMessageDialog(null, "Usuario creado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al crear el usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para obtener todos los usuarios
    public List<Usuario> obtenerTodosUsuarios() {
        try {
            return usuarioDAO.leerTodosUsuarios();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al leer los usuarios: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // Método para actualizar un usuario existente
    public void actualizarUsuario(int idUsuario, String usuario, String contrasena) {
        try {
            Usuario user = new Usuario();
            user.setIdUsuario(idUsuario);
            user.setUsuario(usuario);
            user.setContrasena(contrasena);
            usuarioDAO.actualizarUsuario(user);
            JOptionPane.showMessageDialog(null, "Usuario actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para eliminar un usuario
    public void eliminarUsuario(int idUsuario) {
        try {
            usuarioDAO.eliminarUsuario(idUsuario);
            JOptionPane.showMessageDialog(null, "Usuario eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método main para pruebas
    public static void main(String[] args) {
        UsuarioControlador controlador = new UsuarioControlador();

        // Crear un usuario
        controlador.crearUsuario("ana", "ana2025");

        
        
        // Leer todos los usuarios
        List<Usuario> usuarios = controlador.obtenerTodosUsuarios();
        if (usuarios != null) {
            System.out.println("Lista de usuarios:");
            for (Usuario u : usuarios) {
                System.out.println("ID: " + u.getIdUsuario()
                        + ", Usuario: " + u.getUsuario());
            }
        }

        // Actualizar un usuario (suponiendo que ID 1 existe)
        controlador.actualizarUsuario(1, "ana_maria", "nueva2025");

        // Eliminar un usuario
        controlador.eliminarUsuario(1);
    }

    public Usuario validarCredenciales(String usuario, String contrasena) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}



