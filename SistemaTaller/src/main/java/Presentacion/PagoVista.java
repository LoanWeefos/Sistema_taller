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
        setLocationRelativeTo(null);

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
        btnPagar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        txtPlaca = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btnRegresar1 = new javax.swing.JLabel();
        txtServicios = new javax.swing.JTextField();
        lblServicios = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        cmbMetodoPago = new javax.swing.JComboBox<>();
        lblTotal = new javax.swing.JLabel();
        lblMetodoDePago = new javax.swing.JLabel();
        txtAnio = new com.toedter.calendar.JDateChooser();
        lblFecha = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1221, 850));
        setMinimumSize(new java.awt.Dimension(1221, 850));
        setPreferredSize(new java.awt.Dimension(1221, 850));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(248, 242, 206));
        jPanel1.setLayout(null);

        lblPagos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesPagos/Pagos.png"))); // NOI18N
        jPanel1.add(lblPagos);
        lblPagos.setBounds(50, 24, 251, 44);

        tblPagos.setBackground(new java.awt.Color(216, 217, 137));
        tblPagos.setForeground(new java.awt.Color(73, 61, 63));
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

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(50, 99, 393, 677);

        btnPagar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesPagos/PagosBoton.png"))); // NOI18N
        btnPagar.setBorderPainted(false);
        btnPagar.setContentAreaFilled(false);
        btnPagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPagarActionPerformed(evt);
            }
        });
        jPanel1.add(btnPagar);
        btnPagar.setBounds(896, 704, 280, 72);

        jPanel4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
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

        jPanel1.add(jPanel4);
        jPanel4.setBounds(578, 514, 598, 160);

        btnRegresar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesCliente/Regresar.png"))); // NOI18N
        btnRegresar1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRegresar1MouseClicked(evt);
            }
        });
        jPanel1.add(btnRegresar1);
        btnRegresar1.setBounds(1106, 24, 70, 70);
        jPanel1.add(txtServicios);
        txtServicios.setBounds(476, 165, 700, 40);

        lblServicios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesPagos/Servicios.png"))); // NOI18N
        jPanel1.add(lblServicios);
        lblServicios.setBounds(476, 211, 138, 28);
        jPanel1.add(txtTotal);
        txtTotal.setBounds(476, 269, 700, 39);

        cmbMetodoPago.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cmbMetodoPago.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Efectivo", "Tarjeta" }));
        jPanel1.add(cmbMetodoPago);
        cmbMetodoPago.setBounds(476, 372, 340, 40);

        lblTotal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesPagos/Total.png"))); // NOI18N
        jPanel1.add(lblTotal);
        lblTotal.setBounds(476, 314, 77, 28);

        lblMetodoDePago.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesPagos/MetodoDePago.png"))); // NOI18N
        jPanel1.add(lblMetodoDePago);
        lblMetodoDePago.setBounds(476, 418, 226, 36);
        jPanel1.add(txtAnio);
        txtAnio.setBounds(836, 372, 340, 40);

        lblFecha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesPagos/Fecha.png"))); // NOI18N
        jPanel1.add(lblFecha);
        lblFecha.setBounds(836, 426, 82, 28);

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesCliente/fondo.png"))); // NOI18N
        jPanel1.add(jLabel11);
        jLabel11.setBounds(230, 70, 740, 720);

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

    private void btnRegresar1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegresar1MouseClicked
        MenuVista clienteVista = new MenuVista();
        clienteVista.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnRegresar1MouseClicked

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
    private javax.swing.JLabel btnRegresar1;
    private javax.swing.JComboBox<String> cmbMetodoPago;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
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
