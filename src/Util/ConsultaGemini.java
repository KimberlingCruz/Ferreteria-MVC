/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;

import okhttp3.*;
import org.json.JSONObject;

/**
 *
 * @author welvi
 */
public class ConsultaGemini  {

    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";
    private static final String API_KEY = "AIzaSyCBH7fdN76PWQX0e1IDW9n8HzOwHnDlrpI"; // Reemplaza con tu clave de API de Gemini

    public String generarConsultaSQL(String consultaNatural, String esquemaBD) throws Exception {
        OkHttpClient client = new OkHttpClient();
        String prompt = """
            Genera una consulta SQL válida para una base de datos MySQL con las siguientes tablas y relaciones:
            %s
            Instrucciones:
            - Usa solo las columnas listadas en cada tabla.
            - Respeta el orden de los atributos de cada tabla según el esquemade la base de datos.
            - Asegúrate de que los JOINs sean correctos y utilicen las claves foráneas especificadas.
            - Si se solicita información de múltiples tablas, usa JOINs explícitos.
            - No generes subconsultas complejas ni funciones avanzadas a menos que sean explícitamente solicitadas.
            - Devuelve la consulta SQL en una sola línea, sin saltos de línea, comillas triples, ni formato adicional.
            Pregunta del usuario: "%s"
            """.formatted(esquemaBD, consultaNatural);

        JSONObject json = new JSONObject();
        json.put("contents", new JSONObject().put("parts", new JSONObject().put("text", prompt)));
        json.put("generationConfig", new JSONObject().put("response_mime_type", "text/plain"));

        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(API_URL + "?key=" + API_KEY)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("Error en la solicitud a Gemini: " + response.code());
            }
            String respuesta = response.body().string();
            return new JSONObject(respuesta).getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text");
        }
    }

    // Método para obtener el esquema de la base de datos
    public String obtenerEsquemaBD() {
        return """
            Tabla: Clientes (
                id_cliente INT AUTO_INCREMENT PRIMARY KEY,
                primer_nombre VARCHAR(20),
                segundo_nombre VARCHAR(20),
                primer_apellido VARCHAR(20),
                segundo_apellido VARCHAR(20),
                celular VARCHAR(8),
                direccion VARCHAR(150),
                cedula VARCHAR(14)
            )
            Tabla: Empleados (
                id_empleado INT AUTO_INCREMENT PRIMARY KEY,
                primer_nombre VARCHAR(20),
                segundo_nombre VARCHAR(20),
                primer_apellido VARCHAR(20),
                segundo_apellido VARCHAR(20),
                celular VARCHAR(12),
                cargo VARCHAR(20),
                fecha_contratacion DATE
            )
            Tabla: Usuarios (
                id_usuario INT AUTO_INCREMENT PRIMARY KEY,
                usuario VARCHAR(20),
                contraseña VARCHAR(20)
            )
            Tabla: Categorias (
                id_categoria INT AUTO_INCREMENT PRIMARY KEY,
                nombre_categoria VARCHAR(20),
                descripcion_categoria VARCHAR(100)
            )
            Tabla: Productos (
                id_producto INT AUTO_INCREMENT PRIMARY KEY,
                nombre_producto VARCHAR(20),
                descripcion_producto VARCHAR(100),
                id_categoria INT,
                precio_unitario FLOAT,
                stock INT,
                imagen LONGTEXT
            )
            Tabla: Compras (
                id_compra INT AUTO_INCREMENT PRIMARY KEY,
                id_empleado INT,
                fecha_compra DATE,
                total_compra FLOAT
            )
            Tabla: Detalles_Compras (
                id_detalle_compra INT AUTO_INCREMENT PRIMARY KEY,
                id_compra INT,
                id_producto INT,
                cantidad INT,
                precio_unitario FLOAT
            )
            Tabla: Ventas (
                id_venta INT AUTO_INCREMENT PRIMARY KEY,
                id_cliente INT,
                id_empleado INT,
                fecha_venta DATETIME,
                total_venta FLOAT
            )
            Tabla: Detalles_Ventas (
                id_detalle_venta INT AUTO_INCREMENT PRIMARY KEY,
                id_venta INT,
                id_producto INT,
                cantidad INT,
                precio_unitario FLOAT
            )
            Relaciones:
            - Productos.id_categoria -> Categorias.id_categoria
            - Compras.id_empleado -> Empleados.id_empleado
            - Ventas.id_cliente -> Clientes.id_cliente
            - Ventas.id_empleado -> Empleados.id_empleado
            - Detalles_Compras.id_compra -> Compras.id_compra (ON DELETE CASCADE)
            - Detalles_Compras.id_producto -> Productos.id_producto
            - Detalles_Ventas.id_venta -> Ventas.id_venta (ON DELETE CASCADE)
            - Detalles_Ventas.id_producto -> Productos.id_producto
            """;
    }

    // Validación de consultas SQL
    public boolean esConsultaSegura(String sql) {
        String consulta = sql.toLowerCase().trim();
        return consulta.startsWith("select")
                && !consulta.contains("drop")
                && !consulta.contains("update")
                && !consulta.contains("insert");
    }

}
