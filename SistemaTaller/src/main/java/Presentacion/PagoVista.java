/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Presentacion;

import Dominio.Cliente;
import Dominio.Pago;
import Dominio.Reparacion;
import Dominio.Vehiculo;
import Negocio.ControlPago;
import Negocio.ControlReparacion;
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
import java.util.List;
import javax.swing.JFrame;
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

    /**
     * Creates new form PagoVista
     */
    public PagoVista() {
        initComponents();

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        // Abre la conexión aquí
        this.conexion = Conexion.getConnection();
        cargarDatosPagos();
        tblPagos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                tablaPagosMouseClicked(evt);
            }

        });

    }

    private void cargarDatosPagos() {
        // Modelo de la tabla con columnas Nombre y RFC
        DefaultTableModel modeloTabla = new DefaultTableModel(new Object[]{"Id", "Fecha", "Total"}, 0);

        try {
            // Usa la conexión existente
            String consultaSQL = "SELECT ID, Fecha ,Total FROM pagos";
            PreparedStatement ps = this.conexion.prepareStatement(consultaSQL); // Usa la conexión de la clase
            ResultSet rs = ps.executeQuery();

            // Agrega cada fila de la base de datos al modelo de la tabla
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

        // Asigna el modelo a la tabla
        tblPagos.setModel(modeloTabla);

    }

    private void tablaPagosMouseClicked(MouseEvent evt) {
//        int filaSeleccionada = tblPagos.getSelectedRow();
//        if (filaSeleccionada >= 0) {
//            String rfc = tblPagos.getValueAt(filaSeleccionada, 1).toString(); // Asumiendo que el RFC está en la segunda columna
//            Pago pago = controlPago.obtenerClientePorRfc(rfc); // Método que debes implementar
//
//            if (cliente != null) {
//                // Cargar los datos del cliente en los campos de texto
//                txtRFC.setText(cliente.getRfc());
//                txtNombre.setText(cliente.getNombre());
//                txtCorreo.setText(cliente.getCorreo());
//                txtTelefono.setText(cliente.getTelefono());
//                txtCalle.setText(cliente.getDomicilio().getCalle());
//                txtColonia.setText(cliente.getDomicilio().getColonia());
//                txtNumero.setText(cliente.getDomicilio().getNumero());
//                txtFechaN.setDate(cliente.getFechaNacimiento());
//            } else {
//                System.out.println("Cliente no encontrado con RFC: " + rfc);
//                limpiarCampos(); // Limpiar campos si no se encuentra cliente
//            }
//        }
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
    }

    private void registrarPago() {
        String servicios = txtServicios.getText();
        String total = txtTotal.getText();
        String metodoPago = String.valueOf(cmbMetodoPago.getSelectedIndex());
        LocalDateTime anio = txtAnio.getDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        if (servicios.isEmpty() || total.isEmpty() || anio == null || txtPlaca.getText().trim().isEmpty()) {
            System.out.println("Por favor, complete todos los campos.");
            return;
        }

        // Utiliza el nuevo método para obtener la reparación por placa
        System.out.println("Placa ingresada: " + txtPlaca.getText().trim());

        Reparacion reparacionEncontrada = controlReparacion.obtenerReparacionPorPlaca(txtPlaca.getText());

        if (reparacionEncontrada == null) {
            JOptionPane.showMessageDialog(this, "Placa inexistente");
            System.out.println("Servicios no encontrados con el especificado.");
            return;
        }

        // Crear el objeto Pago con la reparación encontrada
        Pago pago = new Pago(Double.parseDouble(total), metodoPago, anio, reparacionEncontrada);
        

        try {
            controlPago.agregarPago(pago);
            JOptionPane.showMessageDialog(this, "Pago registrado exitosamente.");
            limpiarCampos();
            cargarDatosPagos();
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
        tblPagos = new javax.swing.JTable();
        btnRegresar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        txtServicios = new javax.swing.JTextField();
        lblServicios = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        lblMetodoDePago = new javax.swing.JLabel();
        lblFecha = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        cmbMetodoPago = new javax.swing.JComboBox<>();
        txtAnio = new com.toedter.calendar.JDateChooser();
        btnPagar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        txtPlaca = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1221, 687));

        jPanel1.setBackground(new java.awt.Color(248, 242, 206));

        lblPagos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesPagos/Pagos.png"))); // NOI18N

        tblPagos.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblPagos);

        btnRegresar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesCliente/Regresar.png"))); // NOI18N
        btnRegresar.setBorderPainted(false);
        btnRegresar.setContentAreaFilled(false);
        btnRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegresarActionPerformed(evt);
            }
        });

        jPanel2.setOpaque(false);

        lblServicios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesPagos/Servicios.png"))); // NOI18N

        lblTotal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesPagos/Total.png"))); // NOI18N

        lblMetodoDePago.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesPagos/MetodoDePago.png"))); // NOI18N

        lblFecha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesPagos/Fecha.png"))); // NOI18N

        cmbMetodoPago.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cmbMetodoPago.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Efectivo", "Tarjeta" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(42, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblFecha)
                    .addComponent(lblMetodoDePago)
                    .addComponent(lblTotal)
                    .addComponent(lblServicios)
                    .addComponent(txtServicios)
                    .addComponent(txtTotal)
                    .addComponent(txtAnio, javax.swing.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
                    .addComponent(cmbMetodoPago, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(39, 39, 39))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtServicios, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblServicios)
                .addGap(18, 18, 18)
                .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotal)
                .addGap(18, 18, 18)
                .addComponent(cmbMetodoPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblMetodoDePago)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtAnio, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblFecha)
                .addContainerGap(87, Short.MAX_VALUE))
        );

        btnPagar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesPagos/PagosBoton.png"))); // NOI18N
        btnPagar.setBorderPainted(false);
        btnPagar.setContentAreaFilled(false);
        btnPagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPagarActionPerformed(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.setOpaque(false);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesVehiculo/BuscarBoton.png"))); // NOI18N
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesVehiculo/Placa.png"))); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7)
                        .addGap(491, 491, 491))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(84, 84, 84)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPagos)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnPagar))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 598, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(btnRegresar)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(49, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnPagar))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(lblPagos))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(btnRegresar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPagarActionPerformed
        // TODO add your handling code here:
        this.registrarPago();
    }//GEN-LAST:event_btnPagarActionPerformed

    private void btnRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresarActionPerformed
        // TODO add your handling code here:
        MenuVista menuVista = new MenuVista();
        menuVista.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnRegresarActionPerformed

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
    private javax.swing.JButton btnPagar;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JComboBox<String> cmbMetodoPago;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblMetodoDePago;
    private javax.swing.JLabel lblPagos;
    private javax.swing.JLabel lblServicios;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JTable tblPagos;
    private com.toedter.calendar.JDateChooser txtAnio;
    private javax.swing.JTextField txtPlaca;
    private javax.swing.JTextField txtServicios;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
