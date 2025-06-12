/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Vista;

import Controlador.UsuarioControlador;
import Modelo.Usuario;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author welvi
 */
public class VistaUsuarios extends javax.swing.JPanel {

    private final UsuarioControlador usuariosControlador;
    private Integer idUsuarioSeleccionado = null;

    /**
     * Creates new form VsitaEmpleados
     */
    public VistaUsuarios() {
        initComponents();
        this.usuariosControlador = new UsuarioControlador();
        cargarDatosTabla();
    }

    private void cargarDatosTabla() {
        List<Usuario> usuario = usuariosControlador.obtenerTodosUsuarios();
        if (usuario != null) {
            DefaultTableModel model = (DefaultTableModel) tablaUsuario.getModel();
            model.setRowCount(0);
            for (Usuario usua : usuario) {
                Object[] row = {
                    usua.getIdUsuario(),
                    usua.getUsuario(),
                    usua.getContrasena()
                };
                model.addRow(row);
            }
        }
    }

    private void limpiar() {
        textusuario.setText("");
        textcontraseña = new javax.swing.JPasswordField();
        btnEliminar.setEnabled(true);
        btnGuardar.setEnabled(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        textusuario = new javax.swing.JTextField();
        textcontraseña = new javax.swing.JTextField();
        textBuscar = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaUsuario = new javax.swing.JTable();

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Usuario");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Contraseña");

        textBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textBuscarKeyTyped(evt);
            }
        });

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accionBotonGuardar(evt);
            }
        });

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accionBotonEliminar(evt);
            }
        });

        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accionBotonActualizar(evt);
            }
        });

        tablaUsuario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID Usuario", "Usuario", "Contraseña"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaUsuarioMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaUsuario);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel1)
                                    .addGap(155, 155, 155)
                                    .addComponent(jLabel2)
                                    .addGap(141, 141, 141))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(textusuario, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(54, 54, 54)
                                    .addComponent(textcontraseña, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(77, 77, 77)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(textBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 719, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textusuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textcontraseña, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnGuardar)
                            .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(btnActualizar)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void accionBotonGuardar(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accionBotonGuardar
        String Usuario = textusuario.getText();
        String Contraseña = textcontraseña.getText();

        if (Usuario != null && !Contraseña.isEmpty()) {
            try {
                usuariosControlador.crearUsuario(Usuario, Contraseña);
                cargarDatosTabla();
                limpiar();
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        } else {
        }
    }//GEN-LAST:event_accionBotonGuardar

    private void accionBotonEliminar(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accionBotonEliminar
        int filaSeleccionada = tablaUsuario.getSelectedRow();
        if (filaSeleccionada != -1) {
            int idUsuario = (int) tablaUsuario.getValueAt(filaSeleccionada, 0);
            usuariosControlador.eliminarUsuario(idUsuario);
            cargarDatosTabla(); // corregido
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Selecciona una fila para eliminar. ", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_accionBotonEliminar

    private void accionBotonActualizar(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accionBotonActualizar
        String Usuario = textusuario.getText();
        String Contraseña = textcontraseña.getText();

        if (Usuario != null && !Contraseña.isEmpty()) {
            usuariosControlador.actualizarUsuario(0, Usuario, Contraseña);
            textusuario.setText("");
            textcontraseña = new javax.swing.JPasswordField();

            btnEliminar.setEnabled(true);
            btnGuardar.setEnabled(true);
        }
    }//GEN-LAST:event_accionBotonActualizar

    private void tablaUsuarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaUsuarioMouseClicked
        if (evt.getClickCount() == 2) {

            int filaSeleccionada = tablaUsuario.getSelectedRow();

            if (filaSeleccionada != -1) {

                idUsuarioSeleccionado = (int) tablaUsuario.getValueAt(filaSeleccionada, 0);
                String Usuario = (String) tablaUsuario.getValueAt(filaSeleccionada, 1);
                String contraseña = (String) tablaUsuario.getValueAt(filaSeleccionada, 2);

                textusuario.setText(Usuario);
                textcontraseña = new javax.swing.JPasswordField(contraseña);

                btnEliminar.setEnabled(false);
                btnGuardar.setEnabled(false);

            }
        }
    }//GEN-LAST:event_tablaUsuarioMouseClicked

    private void textBuscarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textBuscarKeyTyped
        String textoBusqueda = textBuscar.getText().trim().toLowerCase();
        List<Usuario> usuario = usuariosControlador.obtenerTodosUsuarios();

        DefaultTableModel modelo = (DefaultTableModel) tablaUsuario.getModel();
        modelo.setRowCount(0);

        if (usuario != null) {
            for (Usuario usua : usuario) {
                if (textoBusqueda.isEmpty()
                        || usua.getUsuario().toLowerCase().contains(textoBusqueda)
                        || usua.getContrasena().toLowerCase().contains(textoBusqueda)) {

                    Object[] row = {
                        usua.getIdUsuario(),
                        usua.getUsuario(),
                        usua.getContrasena()
                    };
                    modelo.addRow(row);
                }
            }
        }
    }//GEN-LAST:event_textBuscarKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaUsuario;
    private javax.swing.JTextField textBuscar;
    private javax.swing.JTextField textcontraseña;
    private javax.swing.JTextField textusuario;
    // End of variables declaration//GEN-END:variables
}
