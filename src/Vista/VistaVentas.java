/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
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
 * @author welvi
 */
public class VistaVentas extends javax.swing.JPanel {

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
    public VistaVentas() {
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        textBuscar = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        selectorfechaVenta = new com.toedter.calendar.JDateChooser();
        comboClientes = new javax.swing.JComboBox<>();
        comboEmpleados = new javax.swing.JComboBox<>();
        hora = new javax.swing.JLabel();
        comboProductos = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        textPrecio = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        textCantidad = new javax.swing.JTextField();
        btnAgregar = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaVentas = new javax.swing.JTable();
        btnQuitarDetalle = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaDetalles = new javax.swing.JTable();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setBackground(new java.awt.Color(51, 255, 204));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText(" Cliente");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Empleado");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Fecha");

        textBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textBuscarKeyTyped(evt);
            }
        });

        btnGuardar.setText("Guardar Venta");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accionBotonGuardar(evt);
            }
        });

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accionBotonLimpiar(evt);
            }
        });

        btnEliminar.setText("Eliminar Venta");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accionBotonEliminar(evt);
            }
        });

        btnActualizar.setText("Actualizar Venta");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accionBotonActualizar(evt);
            }
        });

        comboClientes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cliente1" }));
        comboClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboClientesActionPerformed(evt);
            }
        });

        comboEmpleados.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Empleado1" }));

        hora.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        hora.setText("00:00:00");

        comboProductos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Producto1" }));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Producto");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Precio");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setText("Cantidad");

        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accionBotonAgregarDetalle(evt);
            }
        });

        tablaVentas.setModel(new javax.swing.table.DefaultTableModel(
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
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID Ventas", "Fecha/Hora", "Cliente", "Vendedor", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Float.class
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
        tablaVentas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaVentasMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tablaVentas);

        btnQuitarDetalle.setText("Quitar Detalle");
        btnQuitarDetalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accionBotonQuitarDetalle(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Buscar");

        tablaDetalles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID Producto", "Producto", "Precio Unitario", "Catidad", "Subtotal"
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
        jScrollPane2.setViewportView(tablaDetalles);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 760, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnGuardar)
                                .addGap(18, 18, 18)
                                .addComponent(btnEliminar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(6, 6, 6)
                                .addComponent(textBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnQuitarDetalle, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 753, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(comboClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(comboEmpleados, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addComponent(jLabel1)
                                .addGap(73, 73, 73)
                                .addComponent(jLabel2)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(hora))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(selectorfechaVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(comboProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(textPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(17, 17, 17)
                                .addComponent(textCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(7, 7, 7)
                                .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addComponent(jLabel6)
                                .addGap(49, 49, 49)
                                .addComponent(jLabel7)
                                .addGap(54, 54, 54)
                                .addComponent(jLabel8)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jLabel4)
                        .addComponent(hora)
                        .addComponent(jLabel6)
                        .addComponent(jLabel7)
                        .addComponent(jLabel8)))
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comboClientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboEmpleados, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectorfechaVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboProductos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAgregar))))
                .addGap(16, 16, 16)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel5))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(textBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnQuitarDetalle, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(12, 12, 12)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnLimpiar)
                    .addComponent(btnEliminar)
                    .addComponent(btnActualizar)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void accionBotonLimpiar(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accionBotonLimpiar
        limpiar();
    }//GEN-LAST:event_accionBotonLimpiar

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
                JOptionPane.showMessageDialog(this, "Seleccione una venta para actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Obtener el idVenta de la fila seleccionada
            DefaultTableModel modelVentas = (DefaultTableModel) tablaVentas.getModel();
            int idVenta = (int) modelVentas.getValueAt(filaSeleccionada, 0);

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

            // Calcular el total de la venta
            float totalVenta = 0;
            for (int i = 0; i < rowCount; i++) {
                totalVenta += ((Number) modelDetalles.getValueAt(i, 4)).floatValue(); // Suma los subtotales
            }

            // Actualizar la venta principal
            ventaControlador.actualizarVenta(idVenta, idCliente, idEmpleado, fechaVenta, totalVenta);

            // Eliminar los detalles antiguos de la venta
            List<DetalleVenta> detallesAntiguos = detalleVentaControlador.obtenerTodosDetallesVenta();
            if (detallesAntiguos != null) {
                for (DetalleVenta detalle : detallesAntiguos) {
                    if (detalle.getIdVenta() == idVenta) {
                        detalleVentaControlador.eliminarDetalleVenta(detalle.getIdDetalleVenta());
                    }
                }
            }

            // Insertar los nuevos detalles
            List<DetalleVenta> nuevosDetalles = new ArrayList<>();
            for (int i = 0; i < rowCount; i++) {
                int idProducto = (int) modelDetalles.getValueAt(i, 0);
                float precioUnitario = ((Number) modelDetalles.getValueAt(i, 2)).floatValue();
                int cantidad = (int) modelDetalles.getValueAt(i, 3);

                // Crear y guardar el nuevo detalle
                DetalleVenta detalle = new DetalleVenta();
                detalle.setIdVenta(idVenta);
                detalle.setIdProducto(idProducto);
                detalle.setCantidad(cantidad);
                detalle.setPrecioUnitario(precioUnitario);
                nuevosDetalles.add(detalle);
                detalleVentaControlador.crearDetalleVenta(idVenta, idProducto, cantidad, precioUnitario);
            }

            // Limpiar la tabla de detalles y el formulario
            tablaDetalles.setModel(new DefaultTableModel(new Object[][]{}, new String[]{"ID Producto", "Producto", "Precio Unitario", "Cantidad", "Subtotal"}));
            limpiar();

            // Recargar la tabla de ventas
            cargarDatosTablaVentas();

            // Habilitar botones nuevamente
            btnEliminar.setEnabled(true);
            btnGuardar.setEnabled(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar la venta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_accionBotonActualizar

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

    private void accionBotonAgregarDetalle(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accionBotonAgregarDetalle
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
    }//GEN-LAST:event_accionBotonAgregarDetalle

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

    private void comboClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboClientesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboClientesActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnQuitarDetalle;
    private javax.swing.JComboBox<String> comboClientes;
    private javax.swing.JComboBox<String> comboEmpleados;
    private javax.swing.JComboBox<String> comboProductos;
    private javax.swing.JLabel hora;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private com.toedter.calendar.JDateChooser selectorfechaVenta;
    private javax.swing.JTable tablaDetalles;
    private javax.swing.JTable tablaVentas;
    private javax.swing.JTextField textBuscar;
    private javax.swing.JTextField textCantidad;
    private javax.swing.JTextField textPrecio;
    // End of variables declaration//GEN-END:variables
}
