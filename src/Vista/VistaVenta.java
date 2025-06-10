
package Vista;

import Controlador.VentaControlador;
import Modelo.Venta;
import Controlador.DetalleVentaControlador;
import Modelo.DetalleVenta;
import Controlador.ClienteControlador;
import Modelo.Cliente;
import Controlador.EmpleadoControlador;
import Modelo.Empleado;
import Controlador.ProductoControlador;
import Modelo.Producto;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import java.util.Date;
import java.util.TimeZone;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.Timer;

/**
 *
 * @author Edith Cruz
 */
public class VistaVenta extends javax.swing.JPanel {

    private final VentaControlador ventaControlador;
    private final DetalleVentaControlador detalleVentaControlador;
    private final EmpleadoControlador empleadoControlador;
    private final ClienteControlador clienteControlador;
    private final ProductoControlador productoControlador;
    private Integer idEmpleadoSeleccionado = null;
    private Integer idClienteSeleccionado = null;
    private Integer idProductoSeleccionado = null;
    private Timer timer; // Variable de instancia para el Timer
    private boolean horabd = false;

    /**
     * Creates new form VsitaEmpleados
     */
    public VistaVenta() {
        initComponents();
        this.ventaControlador = new VentaControlador();
        this.detalleVentaControlador = new DetalleVentaControlador();
        this.empleadoControlador = new EmpleadoControlador();
        this.clienteControlador = new ClienteControlador();
        this.productoControlador = new ProductoControlador();
        eventoComboProductos();

        selectorfechaVenta.setDate(new Date());
        ((JTextField) selectorfechaVenta.getDateEditor().getUiComponent()).setEditable(false);

        // Limpiar las filas vacías de tablaDetalles
        DefaultTableModel modelDetalles = (DefaultTableModel) tablaDetalles.getModel();
        modelDetalles.setRowCount(0);

        cargarDatosTablaVentas();
        cargarClientes();
        cargarEmpleados();
        cargarProductos();
        actualizarHora();
    }

    private void limpiar() {
        textBuscar.setText("");
        idEmpleadoSeleccionado = null;
        selectorfechaVenta.setDate(new Date());

        // Limpiar la tabla de detalles
        tablaDetalles.setModel(new DefaultTableModel(new Object[][]{}, new String[]{"ID Producto", "Producto", "Precio Unitario", "Cantidad", "Subtotal"}));

        cargarDatosTablaVentas();
        cargarClientes();
        cargarEmpleados();
        cargarProductos();

        btnEliminar.setEnabled(true);
        btnGuardar.setEnabled(true);

        horabd = false; // Restablece para mostrar hora actual
        actualizarHora(); // Vuelve a iniciar el timer
    }

    private void actualizarHora() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("America/Managua"));

        // Detener el timer anterior si existe
        if (timer != null) {
            timer.stop();
        }

        if (horabd) {
            return; // No actualiza la hora si está cargada desde la base de datos
        }

        timer = new Timer(1000, e -> {
            Date now = new Date();
            hora.setText(sdf.format(now));
        });
        timer.start();
    }

    private void cargarDatosTablaVentas() {
        List<Venta> ventas = ventaControlador.obtenerTodasVentas();

        if (ventas != null) {
            DefaultTableModel model = (DefaultTableModel) tablaVentas.getModel();
            model.setRowCount(0);

            for (Venta v : ventas) {
                Cliente cliente = clienteControlador.obtenerClientePorId(v.getIdCliente());
                String nombreCliente = cliente.getPrimerNombre() + " " + cliente.getPrimerApellido();

                Empleado empleado = empleadoControlador.obtenerEmpleadoPorId(v.getIdEmpleado());
                String nombreEmpleado = empleado.getPrimerNombre() + " " + empleado.getPrimerApellido();

                Object[] row = {
                    v.getIdVenta(),
                    v.getFechaVenta(),
                    nombreCliente,
                    nombreEmpleado,
                    v.getTotalVenta()
                };
                model.addRow(row);
            }
        }
    }

    private void cargarClientes() {
        try {
            // Obtener las categorías desde el controlador
            List<Cliente> clientes = clienteControlador.obtenerTodosClientes();

            // Limpiar el combo box por si tiene datos
            comboClientes.removeAllItems();

            // Agregar cada categoría al combo box
            for (Cliente c : clientes) {
                comboClientes.addItem(c.getPrimerNombre() + " " + c.getPrimerApellido()); // Mostrar el nombre
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar los clientes: " + e.getMessage());
        }
    }

    private void cargarEmpleados() {
        try {
            // Obtener las categorías desde el controlador
            List<Empleado> empleados = empleadoControlador.obtenerTodosEmpleados();

            // Limpiar el combo box por si tiene datos
            comboEmpleados.removeAllItems();

            // Agregar cada categoría al combo box
            for (Empleado e : empleados) {
                comboEmpleados.addItem(e.getPrimerNombre() + " " + e.getPrimerApellido()); // Mostrar el nombre
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar los empleados: " + e.getMessage());
        }
    }

    private void cargarProductos() {
        try {
            // Obtener las categorías desde el controlador
            List<Producto> productos = productoControlador.obtenerTodosProductos();

            // Limpiar el combo box por si tiene datos
            comboProductos.removeAllItems();

            // Agregar cada categoría al combo box
            for (Producto p : productos) {
                comboProductos.addItem(p.getNombreProducto()); // Mostrar el nombre
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar los productos: " + e.getMessage());
        }
    }

    private void eventoComboProductos() {
        comboProductos.addActionListener(e -> {
            // Obtener el índice seleccionado
            int indiceSeleccionado = comboProductos.getSelectedIndex();

            if (indiceSeleccionado >= 0) { // Verificar que se haya seleccionado algo
                try {
                    // Obtener la lista de categorías desde el controlador o memoria
                    List<Producto> productos = productoControlador.obtenerTodosProductos();

                    // Obtener el objeto de categoría correspondiente al índice seleccionado
                    Producto productoSeleccionado = productos.get(indiceSeleccionado);

                    // Actualizar la variable global con el ID de la categoría seleccionada
                    idProductoSeleccionado = productoSeleccionado.getIdProducto();

                    // Actualizar el campo textPrecio con el precio unitario del producto
                    textPrecio.setText(String.valueOf(productoSeleccionado.getPrecioUnitario()));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al seleccionar categoría: " + ex.getMessage());
                }
            }
        });
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        textPrecio = new javax.swing.JTextField();
        textBuscar = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaDetalles = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        comboClientes = new javax.swing.JComboBox<>();
        comboEmpleados = new javax.swing.JComboBox<>();
        selectorfechaVenta = new com.toedter.calendar.JDateChooser();
        comboProductos = new javax.swing.JComboBox<>();
        hora = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        textCantidad = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaVentas = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText(" Clientes");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, -1, -1));

        jLabel2.setText(" Empleado");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 0, -1, -1));

        jLabel3.setText("Producto");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 0, -1, -1));

        jLabel4.setText("Fecha ");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, -1, -1));

        jLabel5.setText("Precio");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 0, -1, -1));
        jPanel1.add(textPrecio, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 20, 70, -1));

        textBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textBuscarKeyTyped(evt);
            }
        });
        jPanel1.add(textBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 210, 130, -1));

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
                accionBotonGuardar(evt);
            }
        });
        jPanel1.add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 380, 120, -1));

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });
        jPanel1.add(btnLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 380, 120, -1));

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
                accionBotonEliminar(evt);
            }
        });
        jPanel1.add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 380, 120, -1));

        btnActualizar.setText("Actualizar Venta");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accionBotonActualizar(evt);
            }
        });
        jPanel1.add(btnActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 380, 120, -1));

        tablaDetalles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID Producto", "Producto", "Precio Unitario", "Cantidad", "Subtotal"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Float.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablaDetalles);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 730, 140));

        jLabel6.setText("Buscar");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, -1, -1));

        comboClientes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel1.add(comboClientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 90, -1));

        comboEmpleados.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel1.add(comboEmpleados, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, 70, -1));
        jPanel1.add(selectorfechaVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, 130, -1));

        comboProductos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel1.add(comboProductos, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 20, 90, -1));

        hora.setText("00:00:00");
        jPanel1.add(hora, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 0, -1, -1));

        jLabel8.setText("Cantidad");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 0, -1, -1));

        textCantidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textCantidadActionPerformed(evt);
            }
        });
        jPanel1.add(textCantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 20, 90, -1));

        jButton1.setText("Agregar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accionBotonAgregar(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 20, 90, -1));

        tablaVentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID Ventas", "Fecha/Hora", "Cliente", " Vendedor", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaVentas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaVentasMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tablaVentas);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 740, 120));

        jButton2.setText("Quitar Detalle");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accionBotonQuitarDeatlle(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 210, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 776, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void textCantidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textCantidadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textCantidadActionPerformed

    private void accionBotonGuardar(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accionBotonGuardar
       try {
            // Obtener el índice seleccionado de clientes y empleados
            int indiceCliente = comboClientes.getSelectedIndex();
            int indiceEmpleado = comboEmpleados.getSelectedIndex();
            if (indiceCliente < 0 || indiceEmpleado < 0) {
                JOptionPane.showMessageDialog(this, "Seleccione un cliente y un empleado.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Obtener la lista de clientes y empleados
            List<Cliente> clientes = clienteControlador.obtenerTodosClientes();
            List<Empleado> empleados = empleadoControlador.obtenerTodosEmpleados();
            int idCliente = clientes.get(indiceCliente).getIdCliente();
            int idEmpleado = empleados.get(indiceEmpleado).getIdEmpleado();

            // Obtener la fecha seleccionada
            Date fechaVenta = selectorfechaVenta.getDate();
            if (fechaVenta == null) {
                JOptionPane.showMessageDialog(this, "Seleccione una fecha.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Obtener los detalles de la tabla tablaDetalles
            DefaultTableModel modelDetalles = (DefaultTableModel) tablaDetalles.getModel();
            int rowCount = modelDetalles.getRowCount();
            if (rowCount == 0) {
                JOptionPane.showMessageDialog(this, "Agregue al menos un producto a la venta.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Crear lista de detalles y calcular total
            List<DetalleVenta> detalles = new ArrayList<>();
            float totalVenta = 0;
            for (int i = 0; i < rowCount; i++) {
                int idProducto = (int) modelDetalles.getValueAt(i, 0); // ID Producto como Integer
                float precioUnitario = ((Number) modelDetalles.getValueAt(i, 2)).floatValue(); // Precio Unitario como Float
                int cantidad = (int) modelDetalles.getValueAt(i, 3); // Cantidad como Integer
                float subtotal = ((Number) modelDetalles.getValueAt(i, 4)).floatValue(); // Subtotal como Float

                // Crear objeto DetalleVenta
                DetalleVenta detalle = new DetalleVenta();
                detalle.setIdProducto(idProducto);
                detalle.setCantidad(cantidad);
                detalle.setPrecioUnitario(precioUnitario);
                detalles.add(detalle);

                totalVenta += subtotal;
            }

            // Crear y guardar la venta
            ventaControlador.crearVenta(idCliente, idEmpleado, fechaVenta, totalVenta, detalles);

            limpiar();

            // Recargar la tabla de ventas
            cargarDatosTablaVentas();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar la venta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    
    }//GEN-LAST:event_accionBotonGuardar

    private void accionBotonEliminar(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accionBotonEliminar
       try {
        // Obtener el índice de la fila seleccionada en tablaVentas
        int filaSeleccionada = tablaVentas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una venta para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener el idVenta de la fila seleccionada
        DefaultTableModel model = (DefaultTableModel) tablaVentas.getModel();
        int idVenta = (int) model.getValueAt(filaSeleccionada, 0);

        // Confirmar con el usuario antes de eliminar
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea eliminar la venta con ID " + idVenta + "?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            // Eliminar la venta
            ventaControlador.eliminarVenta(idVenta);

            // Recargar la tabla de ventas
            cargarDatosTablaVentas();
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al eliminar la venta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_accionBotonEliminar

    private void accionBotonActualizar(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accionBotonActualizar
        try {
        // Obtener el índice de la fila seleccionada en tablaVentas
        int filaSeleccionada = tablaVentas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una venta para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener el idVenta de la fila seleccionada
        DefaultTableModel model = (DefaultTableModel) tablaVentas.getModel();
        int idVenta = (int) model.getValueAt(filaSeleccionada, 0);

        // Confirmar con el usuario antes de eliminar
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea eliminar la venta con ID " + idVenta + "?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            // Eliminar la venta
            ventaControlador.eliminarVenta(idVenta);

            // Recargar la tabla de ventas
            cargarDatosTablaVentas();
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al eliminar la venta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    }//GEN-LAST:event_accionBotonActualizar

    private void accionBotonQuitarDeatlle(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accionBotonQuitarDeatlle
       try {
            // Obtener el índice de la fila seleccionada en tablaDetalles
            int filaSeleccionada = tablaDetalles.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un detalle para quitar.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Eliminar la fila seleccionada del modelo de la tabla
            DefaultTableModel model = (DefaultTableModel) tablaDetalles.getModel();
            model.removeRow(filaSeleccionada);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al quitar el detalle: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_accionBotonQuitarDeatlle

    private void accionBotonAgregar(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accionBotonAgregar
       try {
        // Obtener el índice seleccionado del comboProductos
        int indiceSeleccionado = comboProductos.getSelectedIndex();
        if (indiceSeleccionado < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener la lista de productos
        List<Producto> productos = productoControlador.obtenerTodosProductos();
        Producto productoSeleccionado = productos.get(indiceSeleccionado);

        // Obtener el precio unitario del producto
        float precioUnitario = productoSeleccionado.getPrecioUnitario();

        // Obtener la cantidad ingresada
        String cantidadStr = textCantidad.getText().trim();
        if (cantidadStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una cantidad.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadStr);
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser un número entero válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Calcular el subtotal
        float subtotal = precioUnitario * cantidad;

        // Agregar los datos a la tabla tablaDetalles
        DefaultTableModel model = (DefaultTableModel) tablaDetalles.getModel();
        Object[] row = {
            productoSeleccionado.getIdProducto(),
            productoSeleccionado.getNombreProducto(),
            precioUnitario,
            cantidad,
            subtotal
        };
        model.addRow(row);

        // Limpiar los campos después de agregar
        textCantidad.setText("");
        textPrecio.setText("");
        cargarProductos();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al agregar el producto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_accionBotonAgregar

    private void textBuscarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textBuscarKeyTyped
       String textoBusqueda = textBuscar.getText().trim().toLowerCase();
        List<Venta> ventas = ventaControlador.obtenerTodasVentas();

        DefaultTableModel modelo = (DefaultTableModel) tablaVentas.getModel();
        modelo.setRowCount(0);

        if (ventas != null) {
            for (Venta v : ventas) {
                Cliente cliente = clienteControlador.obtenerClientePorId(v.getIdCliente());
                String nombreCliente = cliente.getPrimerNombre() + " " + cliente.getPrimerApellido();

                Empleado empleado = empleadoControlador.obtenerEmpleadoPorId(v.getIdEmpleado());
                String nombreEmpleado = empleado.getPrimerNombre() + " " + empleado.getPrimerApellido();

                if (textoBusqueda.isEmpty()
                        || nombreCliente.toLowerCase().contains(textoBusqueda)
                        || nombreEmpleado.toLowerCase().contains(textoBusqueda)) {
                    Object[] fila = {
                        v.getIdVenta(),
                        v.getFechaVenta(),
                        nombreCliente,
                        nombreEmpleado,
                        v.getTotalVenta()
                    };
                    modelo.addRow(fila);
                }
            }
            }
    }//GEN-LAST:event_textBuscarKeyTyped

    private void tablaVentasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaVentasMouseClicked
        if (evt.getClickCount() == 2) {
        try {
            btnEliminar.setEnabled(false);
            btnGuardar.setEnabled(false);
            
            // Obtener el índice de la fila seleccionada en tablaVentas
            int filaSeleccionada = tablaVentas.getSelectedRow();
            if (filaSeleccionada == -1) {
                return; // No hacer nada si no hay fila seleccionada
            }

            // Obtener el idVenta de la fila seleccionada
            DefaultTableModel modelVentas = (DefaultTableModel) tablaVentas.getModel();
            int idVenta = (int) modelVentas.getValueAt(filaSeleccionada, 0);

            // Obtener la venta seleccionada para acceder a sus datos
            List<Venta> ventas = ventaControlador.obtenerTodasVentas();
            Venta ventaSeleccionada = null;
            for (Venta v : ventas) {
                if (v.getIdVenta() == idVenta) {
                    ventaSeleccionada = v;
                    break;
                }
            }
            if (ventaSeleccionada == null) {
                JOptionPane.showMessageDialog(this, "Venta no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Cargar cliente en comboClientes
            List<Cliente> clientes = clienteControlador.obtenerTodosClientes();
            int indiceCliente = -1;
            for (int i = 0; i < clientes.size(); i++) {
                if (clientes.get(i).getIdCliente() == ventaSeleccionada.getIdCliente()) {
                    indiceCliente = i;
                    break;
                }
            }
            if (indiceCliente != -1) {
                idClienteSeleccionado = ventaSeleccionada.getIdCliente();
                comboClientes.setSelectedIndex(indiceCliente);
            } else {
                JOptionPane.showMessageDialog(this, "Cliente no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Cargar empleado en comboEmpleados
            List<Empleado> empleados = empleadoControlador.obtenerTodosEmpleados();
            int indiceEmpleado = -1;
            for (int i = 0; i < empleados.size(); i++) {
                if (empleados.get(i).getIdEmpleado() == ventaSeleccionada.getIdEmpleado()) {
                    indiceEmpleado = i;
                    break;
                }
            }
            if (indiceEmpleado != -1) {
                idEmpleadoSeleccionado = ventaSeleccionada.getIdEmpleado();
                comboEmpleados.setSelectedIndex(indiceEmpleado);
            } else {
                JOptionPane.showMessageDialog(this, "Empleado no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Detener el timer actual
            if (timer != null) {
                timer.stop();
            }
            
            // Asignar la hora al label
            horabd = true;
            java.text.SimpleDateFormat horaFormato = new java.text.SimpleDateFormat("HH:mm:ss");
            String horaVenta = horaFormato.format(ventaSeleccionada.getFechaVenta());
            hora.setText(horaVenta); // Ajusta 'horaLabel' al nombre real de tu JLabel

            // Cargar la fecha en selectorfechaContratacion
            selectorfechaVenta.setDate(ventaSeleccionada.getFechaVenta());

            // Limpiar y cargar los detalles en tablaDetalles
            DefaultTableModel modelDetalles = (DefaultTableModel) tablaDetalles.getModel();
            modelDetalles.setRowCount(0);

            List<DetalleVenta> detalles = detalleVentaControlador.obtenerTodosDetallesVenta();
            if (detalles != null) {
                for (DetalleVenta detalle : detalles) {
                    if (detalle.getIdVenta() == idVenta) {
                        Producto producto = productoControlador.obtenerProductoPorId(detalle.getIdProducto());
                        String nombreProducto = (producto != null) ? producto.getNombreProducto() : "Desconocido";

                        Object[] row = {
                            detalle.getIdProducto(),
                            nombreProducto,
                            detalle.getPrecioUnitario(),
                            detalle.getCantidad(),
                            detalle.getPrecioUnitario() * detalle.getCantidad() // Subtotal
                        };
                        modelDetalles.addRow(row);
                    }
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los datos de la venta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        }
    }//GEN-LAST:event_tablaVentasMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JComboBox<String> comboClientes;
    private javax.swing.JComboBox<String> comboEmpleados;
    private javax.swing.JComboBox<String> comboProductos;
    private javax.swing.JLabel hora;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private com.toedter.calendar.JDateChooser selectorfechaVenta;
    private javax.swing.JTable tablaDetalles;
    private javax.swing.JTable tablaVentas;
    private javax.swing.JTextField textBuscar;
    private javax.swing.JTextField textCantidad;
    private javax.swing.JTextField textPrecio;
    // End of variables declaration//GEN-END:variables
}
