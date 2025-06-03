/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Vista;

import Controlador.CompraControlador;
import Modelo.Compra;
import Controlador.DetalleCompraControlador;
import Modelo.DetalleCompra;
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
 * @author welvi
 */
public class VistaCompra extends javax.swing.JPanel {

    private final CompraControlador compraControlador;
    private final DetalleCompraControlador detalleCompraControlador;
    private final EmpleadoControlador empleadoControlador;
    private final ProductoControlador productoControlador;

    private Integer idEmpleadoSeleccionado = null;
    private Integer idProductoSeleccionado = null;

    private Timer timer; // Variable de instancia para el Timer
    private boolean horabd = false;

    public VistaCompra() {
        initComponents();
        this.compraControlador = new CompraControlador();
        this.detalleCompraControlador = new DetalleCompraControlador();
        this.empleadoControlador = new EmpleadoControlador();
        this.productoControlador = new ProductoControlador();
        eventoComboProductos();

        selectorfechaCompra.setDate(new Date());
        ((JTextField) selectorfechaCompra.getDateEditor().getUiComponent()).setEditable(false);

        // Limpiar las filas vacías de tablaDetalles
        DefaultTableModel modelDetalles = (DefaultTableModel) tablaDetalles.getModel();
        modelDetalles.setRowCount(0);

        cargarDatosTablaCompras();
        cargarEmpleados();
        cargarProductos();
        actualizarHora();
    }

    private void limpiar() {
        textBuscar.setText("");
        idEmpleadoSeleccionado = null;
        selectorfechaCompra.setDate(new Date());

        // Limpiar la tabla de detalles
        tablaDetalles.setModel(new DefaultTableModel(new Object[][]{}, new String[]{"ID Producto", "Producto", "Precio Unitario", "Cantidad", "Subtotal"}));

        cargarDatosTablaCompras();
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

    private void cargarDatosTablaCompras() {
        List<Compra> compras = compraControlador.obtenerTodasCompras();

        if (compras != null) {

            DefaultTableModel model = (DefaultTableModel) tablaCompras.getModel();
            model.setRowCount(0);

            for (Compra c : compras) {

                Empleado empleado = empleadoControlador.obtenerEmpleadoPorId(c.getIdEmpleado());
                String nombreEmpleado = empleado.getPrimerNombre() + " " + empleado.getPrimerApellido();

                Object[] row = {
                    c.getIdCompra(),
                    c.getFechaCompra(),
                    nombreEmpleado,
                    c.getTotalCompra()
                };
                model.addRow(row);
            }
        }
    }

    private void cargarEmpleados() {
        try {
            // Obtener las categorías desde el controlador
            List<Empleado> empleados = empleadoControlador.obtenerTodosEmpleados();

            // Limpiar el combo box por si tiene datos
            comboEmpleado.removeAllItems();

            // Agregar cada categoría al combo box
            for (Empleado e : empleados) {
                comboEmpleado.addItem(e.getPrimerNombre() + " " + e.getPrimerApellido()); // Mostrar el nombre
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaDetalles = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        comboEmpleado = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        textPrecio = new javax.swing.JTextField();
        selectorfechaCompra = new com.toedter.calendar.JDateChooser();
        textBuscar = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        hora = new javax.swing.JLabel();
        comboProductos = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        textCantidad = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaCompras = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(51, 255, 204));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablaDetalles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID  Producto", "Producto", "Precio unitario", "Cantidad", "Subtotal"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Float.class, java.lang.Float.class
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
        tablaDetalles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaDetallesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaDetalles);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 58, 659, 89));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText(" Empleado");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 6, -1, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Fecha");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 6, -1, -1));

        comboEmpleado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20" }));
        jPanel1.add(comboEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 29, 107, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Precio");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 10, -1, -1));
        jPanel1.add(textPrecio, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 30, 70, -1));
        jPanel1.add(selectorfechaCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 29, 121, -1));

        textBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textBuscarActionPerformed(evt);
            }
        });
        textBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textBuscarKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textBuscarKeyTyped(evt);
            }
        });
        jPanel1.add(textBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(67, 175, 330, -1));

        btnGuardar.setBackground(new java.awt.Color(153, 255, 255));
        btnGuardar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accionBotonGuardar(evt);
            }
        });
        jPanel1.add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(555, 342, 119, -1));

        btnEliminar.setBackground(new java.awt.Color(153, 255, 255));
        btnEliminar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accionBotonEliminar(evt);
            }
        });
        jPanel1.add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(305, 342, 119, -1));

        btnActualizar.setBackground(new java.awt.Color(153, 255, 255));
        btnActualizar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accionBotonActualizar(evt);
            }
        });
        jPanel1.add(btnActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 340, 119, -1));

        btnLimpiar.setBackground(new java.awt.Color(153, 255, 255));
        btnLimpiar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accionBotonLimpiar(evt);
            }
        });
        jPanel1.add(btnLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 342, 119, -1));

        hora.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        hora.setText("00:00:00");
        jPanel1.add(hora, new org.netbeans.lib.awtextra.AbsoluteConstraints(201, 6, -1, -1));

        comboProductos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel1.add(comboProductos, new org.netbeans.lib.awtextra.AbsoluteConstraints(291, 29, 81, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Producto");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(291, 6, -1, -1));
        jPanel1.add(textCantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(491, 29, 90, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Cantidad");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(497, 6, -1, -1));

        jButton1.setText("Agregar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accionBotonAgregar(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(592, 29, -1, -1));

        jLabel4.setText("Buscar");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 178, -1, -1));

        tablaCompras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID Compra", "Fecha/Hora", "Empleado", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tablaCompras);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 659, 121));

        jButton2.setText("Quitar Detalle");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accionBotonQuitarDetalle(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 170, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void textBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textBuscarActionPerformed
        
    }//GEN-LAST:event_textBuscarActionPerformed

    private void accionBotonGuardar(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accionBotonGuardar
        try {
            // Obtener el índice seleccionado de empleados
            int indiceEmpleado = comboEmpleado.getSelectedIndex();
            if (indiceEmpleado < 0) {
                JOptionPane.showMessageDialog(this, "Seleccione un empleado.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Obtener la lista de  empleados
            List<Empleado> empleados = empleadoControlador.obtenerTodosEmpleados();
            int idEmpleado = empleados.get(indiceEmpleado).getIdEmpleado();

            // Obtener la fecha seleccionada
            Date fechaVenta = selectorfechaCompra.getDate();
            if (fechaVenta == null) {
                JOptionPane.showMessageDialog(this, "Seleccione una fecha.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Obtener los detalles de la tabla tablaDetalles
            DefaultTableModel modelDetalles = (DefaultTableModel) tablaDetalles.getModel();
            int rowCount = modelDetalles.getRowCount();
            if (rowCount == 0) {
                JOptionPane.showMessageDialog(this, "Agregue al menos un producto a la compra.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Crear lista de detalles y calcular total
            List<DetalleCompra> detalles = new ArrayList<>();
            float totalVenta = 0;
            for (int i = 0; i < rowCount; i++) {
                int idProducto = (int) modelDetalles.getValueAt(i, 0); // ID Producto como Integer
                float precioUnitario = ((Number) modelDetalles.getValueAt(i, 2)).floatValue(); // Precio Unitario como Float
                int cantidad = (int) modelDetalles.getValueAt(i, 3); // Cantidad como Integer
                float subtotal = ((Number) modelDetalles.getValueAt(i, 4)).floatValue(); // Subtotal como Float

                // Crear objeto DetalleCompra
                DetalleCompra detalle = new DetalleCompra();
                detalle.setIdProducto(idProducto);
                detalle.setCantidad(cantidad);
                detalle.setPrecioUnitario(precioUnitario);
                detalles.add(detalle);

                totalVenta += subtotal;
            }

            // Crear y guardar la compra
            compraControlador.crearCompra(0, fechaVenta, 0);

            limpiar();

            // Recargar la tabla de compras
            cargarDatosTablaCompras();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar la compra: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_accionBotonGuardar

    private void accionBotonEliminar(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accionBotonEliminar
        try {
            // Obtener el índice de la fila seleccionada en tablaVentas
            int filaSeleccionada = tablaCompras.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione una venta para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Obtener el idVenta de la fila seleccionada
            DefaultTableModel model = (DefaultTableModel) tablaCompras.getModel();
            int idCompra = (int) model.getValueAt(filaSeleccionada, 0);

            // Confirmar con el usuario antes de eliminar
            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de que desea eliminar la venta con ID " + idCompra + "?",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                // Eliminar la venta
                compraControlador.eliminarCompra(idCompra);

                // Recargar la tabla de ventas
                cargarDatosTablaCompras();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar la venta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_accionBotonEliminar

    private void tablaDetallesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaDetallesMouseClicked
        // Verificar si es un doble clic
        if (evt.getClickCount() == 2) {
            try {

                btnEliminar.setEnabled(false);
                btnGuardar.setEnabled(false);

                // Obtener el índice de la fila seleccionada en tablaCompras
                int filaSeleccionada = tablaCompras.getSelectedRow();
                if (filaSeleccionada == -1) {
                    return; // No hacer nada si no hay fila seleccionada
                }

                // Obtener el idVenta de la fila seleccionada
                DefaultTableModel modelVentas = (DefaultTableModel) tablaCompras.getModel();
                int idCompra = (int) modelVentas.getValueAt(filaSeleccionada, 0);

                // Obtener la venta seleccionada para acceder a sus datos
                List<Compra> compras = compraControlador.obtenerTodasCompras();
                Compra compraSeleccionada = null;
                for (Compra c : compras) {
                    if (c.getIdCompra() == idCompra) {
                        compraSeleccionada = c;
                        break;
                    }
                }
                if (compraSeleccionada == null) {
                    JOptionPane.showMessageDialog(this, "Compra no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Cargar empleado en comboEmpleados
                List<Empleado> empleados = empleadoControlador.obtenerTodosEmpleados();
                int indiceEmpleado = -1;
                for (int i = 0; i < empleados.size(); i++) {
                    if (empleados.get(i).getIdEmpleado() == compraSeleccionada.getIdEmpleado()) {
                        indiceEmpleado = i;
                        break;
                    }
                }
                if (indiceEmpleado != -1) {
                    idEmpleadoSeleccionado = compraSeleccionada.getIdEmpleado();
                    comboEmpleado.setSelectedIndex(indiceEmpleado);
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
                String horaCompra = horaFormato.format(compraSeleccionada.getFechaCompra());
                hora.setText(horaCompra); // Ajusta 'horaLabel' al nombre real de tu JLabel

                // Cargar la fecha en selectorfechaContratacion
                selectorfechaCompra.setDate(compraSeleccionada.getFechaCompra());

                // Limpiar y cargar los detalles en tablaDetalles
                DefaultTableModel modelDetalles = (DefaultTableModel) tablaDetalles.getModel();
                modelDetalles.setRowCount(0);

                List<DetalleCompra> detalles = detalleCompraControlador.obtenerTodosDetallesCompra();
                if (detalles != null) {
                    for (DetalleCompra detalle : detalles) {
                        if (detalle.getIdCompra() == idCompra) {
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
                JOptionPane.showMessageDialog(this, "Error al cargar los datos de la compra: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_tablaDetallesMouseClicked

    private void textBuscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textBuscarKeyPressed
 String textoBusqueda = textBuscar.getText().trim().toLowerCase();
        List<Compra> compras = compraControlador.obtenerTodasCompras();

        DefaultTableModel modelo = (DefaultTableModel) tablaCompras.getModel();
        modelo.setRowCount(0);

        if (compras != null) {
            for (Compra c : compras) {
                Empleado empleado = empleadoControlador.obtenerEmpleadoPorId(c.getIdEmpleado());
                String nombreEmpleado = empleado.getPrimerNombre() + " " + empleado.getPrimerApellido();
                
                if (textoBusqueda.isEmpty() ||
                    nombreEmpleado.toLowerCase().contains(textoBusqueda)) {
                    Object[] fila = {
                        c.getIdCompra(),
                        c.getFechaCompra(),
                        nombreEmpleado,
                        c.getTotalCompra()
                    };
                    modelo.addRow(fila);
                }
            }
        }
    
    }//GEN-LAST:event_textBuscarKeyPressed

    private void accionBotonActualizar(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accionBotonActualizar
          try {
            // Obtener el índice de la fila seleccionada en tablaVentas
            int filaSeleccionada = tablaCompras.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione una compra para actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Obtener el idVenta de la fila seleccionada
            DefaultTableModel modelVentas = (DefaultTableModel) tablaCompras.getModel();
            int idCompra = (int) modelVentas.getValueAt(filaSeleccionada, 0);

            // Obtener el índice seleccionado de empleados
            int indiceEmpleado = comboEmpleado.getSelectedIndex();
            if ( indiceEmpleado < 0) {
                JOptionPane.showMessageDialog(this, "Seleccione un empleado.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Obtener la lista de empleados
            List<Empleado> empleados = empleadoControlador.obtenerTodosEmpleados();
            int idEmpleado = empleados.get(indiceEmpleado).getIdEmpleado();

            // Obtener la fecha seleccionada
            Date fechaVenta = selectorfechaCompra.getDate();
            if (fechaVenta == null) {
                JOptionPane.showMessageDialog(this, "Seleccione una fecha.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Obtener los detalles de la tabla tablaDetalles
            DefaultTableModel modelDetalles = (DefaultTableModel) tablaDetalles.getModel();
            int rowCount = modelDetalles.getRowCount();
            if (rowCount == 0) {
                JOptionPane.showMessageDialog(this, "Agregue al menos un producto a la compra.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Calcular el total de la compra
            float totalCompra = 0;
            for (int i = 0; i < rowCount; i++) {
                totalCompra += ((Number) modelDetalles.getValueAt(i, 4)).floatValue(); // Suma los subtotales
            }

            // Actualizar la compra principal
            compraControlador.actualizarCompra(idCompra, idEmpleado, fechaVenta, totalCompra);

            // Eliminar los detalles antiguos de la compra
            List<DetalleCompra> detallesAntiguos = detalleCompraControlador.obtenerTodosDetallesCompra();
            if (detallesAntiguos != null) {
                for (DetalleCompra detalle : detallesAntiguos) {
                    if (detalle.getIdCompra() == idCompra) {
                        detalleCompraControlador.eliminarDetalleCompra(detalle.getIdDetalleCompra());
                    }
                }
            }

            // Insertar los nuevos detalles
            for (int i = 0; i < rowCount; i++) {
                int idProducto = (int) modelDetalles.getValueAt(i, 0);
                float precioUnitario = ((Number) modelDetalles.getValueAt(i, 2)).floatValue();
                int cantidad = (int) modelDetalles.getValueAt(i, 3);

                // Crear y guardar el nuevo detalle
                DetalleCompra detalle = new DetalleCompra();
                detalle.setIdCompra(idCompra);
                detalle.setIdProducto(idProducto);
                detalle.setCantidad(cantidad);
                detalle.setPrecioUnitario(precioUnitario);
                detalleCompraControlador.crearDetalleCompra(idCompra, idProducto, cantidad, precioUnitario);
            }

            // Limpiar la tabla de detalles y el formulario
            tablaDetalles.setModel(new DefaultTableModel(new Object[][]{}, new String[]{"ID Producto", "Producto", "Precio Unitario", "Cantidad", "Subtotal"}));
            limpiar();

            // Recargar la tabla de compras
            cargarDatosTablaCompras();

            // Habilitar botones nuevamente
            btnEliminar.setEnabled(true);
            btnGuardar.setEnabled(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar la compra: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_accionBotonActualizar

    private void accionBotonLimpiar(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accionBotonLimpiar
        limpiar();
    }//GEN-LAST:event_accionBotonLimpiar

    private void accionBotonQuitarDetalle(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accionBotonQuitarDetalle
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
    }//GEN-LAST:event_accionBotonQuitarDetalle

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
        List<Compra> compras = compraControlador.obtenerTodasCompras();

        DefaultTableModel modelo = (DefaultTableModel) tablaDetalles.getModel();
        modelo.setRowCount(0);

        if (compras != null) {
            for (Compra comp : compras) {
                String idEmpleadoStr = String.valueOf(comp.getIdEmpleado());
                String totalCompraStr = String.valueOf(comp.getTotalCompra());

                if (textoBusqueda.isEmpty()
                        || idEmpleadoStr.contains(textoBusqueda)
                        || totalCompraStr.contains(textoBusqueda)) {

                    Object[] fila = {
                        comp.getIdCompra(),
                        comp.getIdEmpleado(),
                        comp.getFechaCompra(),
                        comp.getTotalCompra()
                    };
                    modelo.addRow(fila);
                }
            }
        }
    }//GEN-LAST:event_textBuscarKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JComboBox<String> comboEmpleado;
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private com.toedter.calendar.JDateChooser selectorfechaCompra;
    private javax.swing.JTable tablaCompras;
    private javax.swing.JTable tablaDetalles;
    private javax.swing.JTextField textBuscar;
    private javax.swing.JTextField textCantidad;
    private javax.swing.JTextField textPrecio;
    // End of variables declaration//GEN-END:variables
}
