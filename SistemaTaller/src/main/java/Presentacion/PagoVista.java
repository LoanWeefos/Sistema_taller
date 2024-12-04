/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Presentacion;

import Dominio.Cliente;
import Dominio.Pago;
import Dominio.ReparacionServicio;
import Dominio.Servicio;
import Negocio.ControlPago;
import Negocio.ControlReparacion;
import Negocio.ControlReparacionServicio;
import Negocio.ControlServicio;
import Negocio.ControlVehiculo;
import Persistencia.Conexion;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author hoshi
 */
public class PagoVista extends javax.swing.JFrame {

    Connection conexion;
    private ControlPago controlPago = new ControlPago();
    private ControlReparacion controlReparacion = new ControlReparacion();
    private ControlVehiculo controlVehiculo = new ControlVehiculo();
    private ControlServicio controlServicio = new ControlServicio();
    private ControlReparacionServicio controlReparacionServicio = new ControlReparacionServicio();
    private Boolean reparaciones = true;
    private int reparacion = 0;

    /**
     * Creates new form PagoVista
     */
    public PagoVista() {
        initComponents();
        setLocationRelativeTo(null);

        // Abre la conexión aquí
        this.conexion = Conexion.getConnection();
        cargarDatosReparaciones();
        tblReparaciones.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                tablaReparacionesMouseClicked(evt);
            }

        });
    }

    private void agregarTotal(double cantidad) {
        if (txtTotal.getText().equals("")) {
            txtTotal.setText((cantidad) + "");
        } else {
            txtTotal.setText((Double.parseDouble(txtTotal.getText()) + cantidad) + "");
        }
    }

    private void tablaReparacionesMouseClicked(MouseEvent evt) {
        if (reparaciones) {
            int filaSeleccionada = tblReparaciones.getSelectedRow();
            txtServicios.setText("");
            txtTotal.setText("");
            if (filaSeleccionada >= 0) {
                reparacion = Integer.parseInt(tblReparaciones.getValueAt(filaSeleccionada, 0).toString());

                for (ReparacionServicio reparacionServicio : controlReparacionServicio.obtenerTodasLasReparacionesServicios()) {
                    if (reparacion == reparacionServicio.getReparacion().getId()) {
                        Servicio temp = controlServicio.obtenerServicioPorId(reparacionServicio.getServicio().getId_servicio());
                        if (!txtServicios.getText().equals("")) {
                            txtServicios.setText(txtServicios.getText() + ", " + temp.getDescripcion());
                        } else {
                            txtServicios.setText(temp.getDescripcion());
                        }
                        agregarTotal(temp.getCosto());
                    }
                }
            }
        }
    }

    private void cargarDatosPagos() {
        DefaultTableModel modeloTabla = new DefaultTableModel(new Object[]{"Id", "Fecha", "Total"}, 0);

        try {
            String consultaSQL = "SELECT ID, Fecha ,Total FROM pagos";
            PreparedStatement ps = this.conexion.prepareStatement(consultaSQL);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int ID = rs.getInt("ID");
                Date fecha = rs.getDate("Fecha");
                double total = rs.getDouble("Total");
                modeloTabla.addRow(new Object[]{ID, fecha, total});
            }

            // Cierra el ResultSet y el PreparedStatement
            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al cargar los datos de clientes");
        }

        tblReparaciones.setModel(modeloTabla);

    }

    private void cargarDatosReparaciones() {
        // Modelo de la tabla con columnas Nombre y RFC
        DefaultTableModel modeloTabla = new DefaultTableModel(new Object[]{"Orden", "Placa", "Cliente"}, 0);

        try {
            // Usa la conexión existente
            String consultaSQL = "SELECT ID, placa_vehiculo FROM reparaciones";
            PreparedStatement ps = this.conexion.prepareStatement(consultaSQL);
            ResultSet rs = ps.executeQuery();

            // Agrega cada fila de la base de datos al modelo de la tabla
            while (rs.next()) {
                int ID = rs.getInt("ID");
                String placa = rs.getString("placa_vehiculo");

                Cliente cliente = controlVehiculo.obtenerVehiculoPorPlaca(placa).getCliente();

                if (controlPago.obtenerPagoPorIdReparacion(ID) == null) {
                    modeloTabla.addRow(new Object[]{ID, placa, cliente.getRfc()});
                }
            }

            // Cierra el ResultSet y el PreparedStatement
            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al cargar los datos de clientes");
        }

        // Asigna el modelo a la tabla
        tblReparaciones.setModel(modeloTabla);

    }

    private void cambiarTabla() {
        if (reparaciones) {
            cargarDatosPagos();
            limpiarCampos();
        } else {
            cargarDatosReparaciones();
        }
        reparaciones = !reparaciones;
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
        txtServicios.setText("");
        txtTotal.setText("");
        cmbMetodoPago.getItemAt(0);
        txtAnio.setDate(null);
        reparacion = 0;
    }

    private void registrarPago() {
        String total = txtTotal.getText();
        String metodoPago = String.valueOf(cmbMetodoPago.getSelectedIndex());
        LocalDateTime anio = txtAnio.getDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        if (reparacion == 0 || total.isEmpty() || anio == null) {
            System.out.println("Por favor, complete todos los campos.");
            return;
        }

        Pago pago = new Pago(Double.valueOf(total), metodoPago, anio, controlReparacion.obtenerReparacionPorId(reparacion));

        try {
            controlPago.agregarPago(pago);
            JOptionPane.showMessageDialog(this, "Pago registrado exitosamente.");
            limpiarCampos();
            cargarDatosReparaciones();
        } catch (Exception e) {
            System.out.println("Error al registrar el Pago: " + e.getMessage());
        }
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
        lblPagos = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblReparaciones = new javax.swing.JTable();
        btnCambio = new javax.swing.JButton();
        btnRegresar1 = new javax.swing.JLabel();
        txtServicios = new javax.swing.JTextField();
        lblServicios = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        cmbMetodoPago = new javax.swing.JComboBox<>();
        lblTotal = new javax.swing.JLabel();
        lblMetodoDePago = new javax.swing.JLabel();
        txtAnio = new com.toedter.calendar.JDateChooser();
        lblFecha = new javax.swing.JLabel();
        btnPagar = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1263, 700));
        setMinimumSize(new java.awt.Dimension(1263, 700));
        setPreferredSize(new java.awt.Dimension(1263, 700));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(248, 242, 206));
        jPanel1.setLayout(null);

        lblPagos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesPagos/Pagos.png"))); // NOI18N
        jPanel1.add(lblPagos);
        lblPagos.setBounds(50, 24, 251, 44);

        tblReparaciones.setBackground(new java.awt.Color(216, 217, 137));
        tblReparaciones.setForeground(new java.awt.Color(73, 61, 63));
        tblReparaciones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Orden", "Placa", "Cliente"
            }
        ));
        jScrollPane1.setViewportView(tblReparaciones);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(50, 99, 393, 450);

        btnCambio.setBackground(new java.awt.Color(73, 61, 63));
        btnCambio.setFont(new java.awt.Font("Sugo Pro Classic Trial", 0, 24)); // NOI18N
        btnCambio.setForeground(new java.awt.Color(255, 255, 255));
        btnCambio.setText("CAMBIAR TABLA");
        btnCambio.setBorderPainted(false);
        btnCambio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCambioActionPerformed(evt);
            }
        });
        jPanel1.add(btnCambio);
        btnCambio.setBounds(100, 570, 280, 50);

        btnRegresar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesCliente/Regresar.png"))); // NOI18N
        btnRegresar1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRegresar1MouseClicked(evt);
            }
        });
        jPanel1.add(btnRegresar1);
        btnRegresar1.setBounds(1160, 10, 70, 70);

        txtServicios.setEnabled(false);
        jPanel1.add(txtServicios);
        txtServicios.setBounds(530, 110, 700, 40);

        lblServicios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesPagos/Servicios.png"))); // NOI18N
        jPanel1.add(lblServicios);
        lblServicios.setBounds(530, 160, 138, 28);
        jPanel1.add(txtTotal);
        txtTotal.setBounds(530, 210, 310, 39);

        cmbMetodoPago.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cmbMetodoPago.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Efectivo", "Tarjeta" }));
        jPanel1.add(cmbMetodoPago);
        cmbMetodoPago.setBounds(880, 210, 350, 40);

        lblTotal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesPagos/Total.png"))); // NOI18N
        jPanel1.add(lblTotal);
        lblTotal.setBounds(530, 260, 77, 28);

        lblMetodoDePago.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesPagos/MetodoDePago.png"))); // NOI18N
        jPanel1.add(lblMetodoDePago);
        lblMetodoDePago.setBounds(880, 260, 230, 36);
        jPanel1.add(txtAnio);
        txtAnio.setBounds(530, 320, 310, 40);

        lblFecha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesPagos/Fecha.png"))); // NOI18N
        jPanel1.add(lblFecha);
        lblFecha.setBounds(530, 370, 90, 28);

        btnPagar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesPagos/PagosBoton.png"))); // NOI18N
        btnPagar.setBorderPainted(false);
        btnPagar.setContentAreaFilled(false);
        btnPagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPagarActionPerformed(evt);
            }
        });
        jPanel1.add(btnPagar);
        btnPagar.setBounds(940, 460, 280, 72);

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesCliente/fondo.png"))); // NOI18N
        jPanel1.add(jLabel11);
        jLabel11.setBounds(270, 90, 740, 710);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1267, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 710, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCambioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCambioActionPerformed
        cambiarTabla();
    }//GEN-LAST:event_btnCambioActionPerformed

    private void btnRegresar1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegresar1MouseClicked
        MenuVista clienteVista = new MenuVista();
        clienteVista.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnRegresar1MouseClicked

    private void btnPagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPagarActionPerformed
        registrarPago();
    }//GEN-LAST:event_btnPagarActionPerformed

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
            java.util.logging.Logger.getLogger(PagoVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PagoVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PagoVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PagoVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PagoVista().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCambio;
    private javax.swing.JButton btnPagar;
    private javax.swing.JLabel btnRegresar1;
    private javax.swing.JComboBox<String> cmbMetodoPago;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblMetodoDePago;
    private javax.swing.JLabel lblPagos;
    private javax.swing.JLabel lblServicios;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JTable tblReparaciones;
    private com.toedter.calendar.JDateChooser txtAnio;
    private javax.swing.JTextField txtServicios;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
