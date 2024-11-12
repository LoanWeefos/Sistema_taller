/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Presentacion;

import Dominio.Cliente;
import Dominio.Domicilio;
import Dominio.Vehiculo;
import Negocio.ControlCliente;
import Persistencia.Conexion;
import java.awt.event.MouseAdapter;
import Negocio.ControlVehiculo;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author hoshi
 */
public class ReparacionesVista extends javax.swing.JFrame {

    Connection conexion;
    private ControlVehiculo controlVehiculo = new ControlVehiculo();
    private ControlCliente controlCliente = new ControlCliente();

    /**
     * Creates new form VehiculoVista
     */
    public ReparacionesVista() {
        initComponents();
        setLocationRelativeTo(null);

        // Abre la conexión aquí
        this.conexion = Conexion.getConnection();
        cargarDatosVehiculos();
        cargarRFCClientes();
        tblVehiculos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                tablaVehiculosMouseClicked(evt);
            }

        });

        txtColor.setVisible(false);
        txtModelo.setVisible(false);
        cmbServicios.setVisible(false);
        cmbPlaca.setVisible(false);

        jLabel2.setVisible(false);
        jLabel4.setVisible(false);
        jLabel6.setVisible(false);
        lblFecha.setVisible(false);
        txtAnio.setVisible(false);
        
        btnAgregar.setVisible(false);
    }

    private void cargarRFCClientes() {
        List<Cliente> listaClientes = controlCliente.obtenerTodosLosClientes();
        cmbServicios.removeAllItems();

        for (Cliente cliente : listaClientes) {
            cmbServicios.addItem(cliente.getRfc());
        }
    }

    private void registrarVehiculo() {
        String rfc = (String) cmbServicios.getSelectedItem();
        String modelo = txtModelo.getText();
        String color = txtColor.getText();

        // Validación básica de campos vacíos
        if (rfc.isEmpty() || modelo.isEmpty() || color.isEmpty()) {

            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.");
            return;
        }

        // Verificar si la placa ya está registrada
//        if (existeVehiculoConPlaca(placa)) {
//            JOptionPane.showMessageDialog(this, "Ya existe un vehículo registrado con la misma placa.");
//            return;
//        }

        // Validar si el RFC del cliente existe
        Cliente clienteEncontrado = buscarClientePorRfc(rfc);
        if (clienteEncontrado == null) {
            System.out.println("Cliente no encontrado con el RFC especificado.");
            JOptionPane.showMessageDialog(this, "Cliente no encontrado con el RFC especificado.");
            return;
        }

        // Crear el objeto Vehiculo y asignar el cliente encontrado
//        Vehiculo vehiculo = new Vehiculo(placa, color, marca, modelo, clienteEncontrado);

        try {
            // Intentar agregar el vehículo
//            controlVehiculo.agregarVehiculo(vehiculo);

            // Mensaje de éxito si no hay excepción
            System.out.println("Vehículo registrado exitosamente.");
            JOptionPane.showMessageDialog(this, "Vehículo registrado exitosamente.");
            limpiarCampos(); // Limpia los campos tras la inserción
            cargarDatosVehiculos(); // Actualiza la tabla con los nuevos datos
        } catch (Exception e) {
            // Manejo de error en caso de fallo
            System.out.println("Error al registrar el vehículo: " + e.getMessage());
        }
    }

// Método auxiliar para buscar un cliente por RFC
    private Cliente buscarClientePorRfc(String rfc) {
        List<Cliente> listaClientes = controlCliente.obtenerTodosLosClientes();
        for (Cliente cliente : listaClientes) {
            if (cliente.getRfc().equalsIgnoreCase(rfc)) {
                return cliente; // Cliente encontrado
            }
        }
        return null; // Cliente no encontrado
    }

// Método auxiliar para verificar si existe un vehículo con la misma placa
    private boolean existeVehiculoConPlaca(String placa) {
        try {
            Vehiculo vehiculo = controlVehiculo.obtenerVehiculoPorPlaca(placa);
            return vehiculo != null; // Si el vehículo existe, devuelve true
        } catch (IllegalArgumentException e) {
            return false; // Si lanza excepción, el vehículo no existe
        }
    }

    private void tablaVehiculosMouseClicked(MouseEvent evt) {

        int filaSeleccionada = tblVehiculos.getSelectedRow();
        if (filaSeleccionada >= 0) {
            String placa = tblVehiculos.getValueAt(filaSeleccionada, 0).toString(); // Asumiendo que el RFC está en la segunda columna
            Vehiculo vehiculo = controlVehiculo.obtenerVehiculoPorPlaca(placa); // Método que debes implementar

            if (vehiculo != null) {
                cmbServicios.setSelectedItem(vehiculo.getCliente().getRfc());
                txtModelo.setText(vehiculo.getModelo());
                txtColor.setText(vehiculo.getColor());

                txtColor.setVisible(true);
                txtModelo.setVisible(true);
                cmbServicios.setVisible(true);

                jLabel2.setVisible(true);
                jLabel4.setVisible(true);
                jLabel6.setVisible(true);
            } else {
                System.out.println("Vehiculo no encontrado con Placa: " + placa);
                limpiarCampos(); // Limpiar campos si no se encuentra cliente
            }
        }
    }

    private void cargarDatosVehiculos() {
        // Modelo de la tabla con columnas Nombre y RFC
        DefaultTableModel modeloTabla = new DefaultTableModel(new Object[]{"Placa", "RFC"}, 0);

        try {
            // Usa la conexión existente
            String consultaSQL = "SELECT Placa, rfc_cliente FROM vehiculos";
            PreparedStatement ps = this.conexion.prepareStatement(consultaSQL); // Usa la conexión de la clase
            ResultSet rs = ps.executeQuery();

            // Agrega cada fila de la base de datos al modelo de la tabla
            while (rs.next()) {
                String placa = rs.getString("Placa");
                String rfc = rs.getString("rfc_cliente");
                modeloTabla.addRow(new Object[]{placa, rfc});
            }

            // Cierra el ResultSet y el PreparedStatement
            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al cargar los datos de vehiculos");
        }

        // Asigna el modelo a la tabla
        tblVehiculos.setModel(modeloTabla);
    }

    public void closeConnection() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void limpiarCampos() {
        txtModelo.setText("");
        txtColor.setText("");
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
        lblTitulo = new javax.swing.JLabel();
        btnRegresar1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblVehiculos = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        txtModelo = new javax.swing.JTextField();
        txtColor = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnAgregar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        cmbServicios = new javax.swing.JComboBox<>();
        jButton3 = new javax.swing.JButton();
        cmbPlaca = new javax.swing.JComboBox<>();
        txtAnio = new com.toedter.calendar.JDateChooser();
        lblFecha = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(248, 242, 206));
        setMinimumSize(new java.awt.Dimension(1230, 700));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(248, 242, 206));
        jPanel1.setPreferredSize(new java.awt.Dimension(900, 743));
        jPanel1.setLayout(null);

        lblTitulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesReparaciones/Reparaciones.png"))); // NOI18N
        jPanel1.add(lblTitulo);
        lblTitulo.setBounds(50, 24, 600, 44);

        btnRegresar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesCliente/Regresar.png"))); // NOI18N
        btnRegresar1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRegresar1MouseClicked(evt);
            }
        });
        jPanel1.add(btnRegresar1);
        btnRegresar1.setBounds(1270, 20, 70, 70);

        tblVehiculos.setBackground(new java.awt.Color(216, 217, 137));
        tblVehiculos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Placa", "Cliente"
            }
        ));
        jScrollPane1.setViewportView(tblVehiculos);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(50, 124, 319, 500);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesReparaciones/Servicios.png"))); // NOI18N
        jPanel1.add(jLabel2);
        jLabel2.setBounds(640, 180, 160, 31);

        txtModelo.setEnabled(false);
        txtModelo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtModeloKeyTyped(evt);
            }
        });
        jPanel1.add(txtModelo);
        txtModelo.setBounds(640, 240, 700, 48);
        jPanel1.add(txtColor);
        txtColor.setBounds(640, 320, 700, 48);

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesReparaciones/Empleado.png"))); // NOI18N
        jPanel1.add(jLabel6);
        jLabel6.setBounds(640, 380, 160, 31);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesReparaciones/Placa.png"))); // NOI18N
        jPanel1.add(jLabel4);
        jLabel4.setBounds(640, 590, 94, 31);

        btnAgregar.setBackground(new java.awt.Color(248, 242, 206));
        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesReparaciones/Agregar1.png"))); // NOI18N
        btnAgregar.setBorderPainted(false);
        btnAgregar.setContentAreaFilled(false);
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });
        jPanel1.add(btnAgregar);
        btnAgregar.setBounds(1040, 120, 300, 69);

        jLabel1.setBackground(new java.awt.Color(248, 242, 206));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesReparaciones/NuevaReparacion.png"))); // NOI18N
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel1);
        jLabel1.setBounds(710, 20, 548, 70);

        cmbServicios.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel1.add(cmbServicios);
        cmbServicios.setBounds(640, 120, 340, 40);

        jButton3.setBackground(new java.awt.Color(248, 242, 206));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesReparaciones/Agregar2.png"))); // NOI18N
        jButton3.setBorderPainted(false);
        jButton3.setContentAreaFilled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3);
        jButton3.setBounds(1060, 540, 280, 69);

        cmbPlaca.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel1.add(cmbPlaca);
        cmbPlaca.setBounds(640, 540, 340, 40);
        jPanel1.add(txtAnio);
        txtAnio.setBounds(640, 440, 340, 40);

        lblFecha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesReparaciones/Fecha.png"))); // NOI18N
        jPanel1.add(lblFecha);
        lblFecha.setBounds(640, 490, 100, 31);

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesCliente/fondo.png"))); // NOI18N
        jPanel1.add(jLabel11);
        jLabel11.setBounds(260, 0, 740, 720);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1414, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        // TODO add your handling code here:
        this.registrarVehiculo();
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void txtModeloKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtModeloKeyTyped

    }//GEN-LAST:event_txtModeloKeyTyped

    private void btnRegresar1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegresar1MouseClicked
        MenuVista clienteVista = new MenuVista();
        clienteVista.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnRegresar1MouseClicked

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        txtColor.setVisible(true);
        txtModelo.setVisible(true);
        cmbServicios.setVisible(true);
        cmbPlaca.setVisible(true);

        jLabel2.setVisible(true);
        jLabel4.setVisible(true);
        jLabel6.setVisible(true);
        lblFecha.setVisible(true);
        txtAnio.setVisible(true);
        
        btnAgregar.setVisible(true);
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ReparacionesVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReparacionesVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReparacionesVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReparacionesVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ReparacionesVista().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JLabel btnRegresar1;
    private javax.swing.JComboBox<String> cmbPlaca;
    private javax.swing.JComboBox<String> cmbServicios;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTable tblVehiculos;
    private com.toedter.calendar.JDateChooser txtAnio;
    private javax.swing.JTextField txtColor;
    private javax.swing.JTextField txtModelo;
    // End of variables declaration//GEN-END:variables

}
