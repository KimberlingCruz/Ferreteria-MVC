/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author Edith Cruz
 */
public class ConexionDB {
            private static final String URL = "jdbc:mysql://localhost:3306/ferreteria_bd?useSSL=false";
   private static final String USER = "root"; // Cambia por tu usuario
    private static final String PASSWORD = "KCRUZ15"; // Cambia por tu contrasena
    
   public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver de MySQL no encontrado", e);
        }
   }
   
   // Método temporal para probar
    public static void main(String[] args) {
        try {
            Connection c = getConnection();
            System.out.println("¡Conexión exitosa a ferreteria_bd!");
            c.close();
        } catch (SQLException e) {
            System.err.println("Error de conexión: " + e.getMessage());

}
        }}