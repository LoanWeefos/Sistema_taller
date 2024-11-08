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
public class VehiculoVista extends javax.swing.JFrame {

    Connection conexion;
    private ControlVehiculo controlVehiculo = new ControlVehiculo();
    private ControlCliente controlCliente = new ControlCliente();

    /**
     * Creates new form VehiculoVista
     */
    public VehiculoVista() {
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
        txtMarca.setVisible(false);
        txtModelo.setVisible(false);
        txtPlaca.setVisible(false);
        cmbRFC.setVisible(false);

        jLabel2.setVisible(false);
        jLabel3.setVisible(false);
        jLabel4.setVisible(false);
        jLabel5.setVisible(false);
        jLabel6.setVisible(false);

    }

    private void cargarRFCClientes() {
        List<Cliente> listaClientes = controlCliente.obtenerTodosLosClientes();
        cmbRFC.removeAllItems();

        for (Cliente cliente : listaClientes) {
            cmbRFC.addItem(cliente.getRfc());
        }
    }

    private void registrarVehiculo() {
        String placa = txtPlaca.getText();
        String rfc = (String) cmbRFC.getSelectedItem();
        String marca = txtMarca.getText();
        String modelo = txtModelo.getText();
        String color = txtColor.getText();

        // Validación básica de campos vacíos
        if (placa.isEmpty() || rfc.isEmpty() || marca.isEmpty() || modelo.isEmpty() || color.isEmpty()) {

            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.");
            return;
        }

        // Verificar si la placa ya está registrada
        if (existeVehiculoConPlaca(placa)) {
            JOptionPane.showMessageDialog(this, "Ya existe un vehículo registrado con la misma placa.");
            return;
        }

        // Validar si el RFC del cliente existe
        Cliente clienteEncontrado = buscarClientePorRfc(rfc);
        if (clienteEncontrado == null) {
            System.out.println("Cliente no encontrado con el RFC especificado.");
            JOptionPane.showMessageDialog(this, "Cliente no encontrado con el RFC especificado.");
            return;
        }

        // Crear el objeto Vehiculo y asignar el cliente encontrado
        Vehiculo vehiculo = new Vehiculo(placa, color, marca, modelo, clienteEncontrado);

        try {
            // Intentar agregar el vehículo
            controlVehiculo.agregarVehiculo(vehiculo);

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
                txtPlaca.setText(vehiculo.getPlaca());
                cmbRFC.setSelectedItem(vehiculo.getCliente().getRfc());
                txtMarca.setText(vehiculo.getMarca());
                txtModelo.setText(vehiculo.getModelo());
                txtColor.setText(vehiculo.getColor());

                txtColor.setVisible(true);
                txtMarca.setVisible(true);
                txtModelo.setVisible(true);
                txtPlaca.setVisible(true);
                cmbRFC.setVisible(true);

                jLabel2.setVisible(true);
                jLabel3.setVisible(true);
                jLabel4.setVisible(true);
                jLabel5.setVisible(true);
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
        txtPlaca.setText("");
        txtMarca.setText("");
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
        txtPlaca = new javax.swing.JTextField();
        txtMarca = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtModelo = new javax.swing.JTextField();
        txtColor = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        cmbRFC = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(248, 242, 206));
        setMaximumSize(new java.awt.Dimension(1230, 700));
        setMinimumSize(new java.awt.Dimension(1230, 700));
        setPreferredSize(new java.awt.Dimension(1230, 700));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(248, 242, 206));
        jPanel1.setPreferredSize(new java.awt.Dimension(900, 743));
        jPanel1.setLayout(null);

        lblTitulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesVehiculo/Vehiculos.png"))); // NOI18N
        jPanel1.add(lblTitulo);
        lblTitulo.setBounds(50, 24, 416, 54);

        btnRegresar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesCliente/Regresar.png"))); // NOI18N
        btnRegresar1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRegresar1MouseClicked(evt);
            }
        });
        jPanel1.add(btnRegresar1);
        btnRegresar1.setBounds(1102, 24, 70, 70);

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

        txtPlaca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPlacaActionPerformed(evt);
            }
        });
        jPanel1.add(txtPlaca);
        txtPlaca.setBounds(472, 124, 340, 48);
        jPanel1.add(txtMarca);
        txtMarca.setBounds(832, 124, 340, 48);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesVehiculo/Placa.png"))); // NOI18N
        jPanel1.add(jLabel2);
        jLabel2.setBounds(472, 178, 84, 28);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesVehiculo/Marca.png"))); // NOI18N
        jPanel1.add(jLabel3);
        jLabel3.setBounds(832, 178, 94, 28);

        txtModelo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtModeloKeyTyped(evt);
            }
        });
        jPanel1.add(txtModelo);
        txtModelo.setBounds(472, 236, 340, 48);
        jPanel1.add(txtColor);
        txtColor.setBounds(832, 236, 340, 48);

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesVehiculo/Modelo.png"))); // NOI18N
        jPanel1.add(jLabel5);
        jLabel5.setBounds(472, 290, 105, 28);

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesVehiculo/Color.png"))); // NOI18N
        jPanel1.add(jLabel6);
        jLabel6.setBounds(832, 290, 87, 28);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesVehiculo/Rfc.png"))); // NOI18N
        jPanel1.add(jLabel4);
        jLabel4.setBounds(472, 405, 201, 35);

        jButton2.setBackground(new java.awt.Color(248, 242, 206));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesVehiculo/RegistrarBoton.png"))); // NOI18N
        jButton2.setBorderPainted(false);
        jButton2.setContentAreaFilled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2);
        jButton2.setBounds(892, 500, 280, 69);

        jLabel1.setBackground(new java.awt.Color(248, 242, 206));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesVehiculo/RegistrarVehiculoBoton.png"))); // NOI18N
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel1);
        jLabel1.setBounds(548, 24, 548, 70);

        cmbRFC.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel1.add(cmbRFC);
        cmbRFC.setBounds(470, 360, 340, 40);

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesCliente/fondo.png"))); // NOI18N
        jPanel1.add(jLabel11);
        jLabel11.setBounds(260, 0, 740, 720);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 2172, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 748, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 225, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtPlacaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPlacaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPlacaActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        this.registrarVehiculo();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtModeloKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtModeloKeyTyped

    }//GEN-LAST:event_txtModeloKeyTyped

    private void btnRegresar1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegresar1MouseClicked
        MenuVista clienteVista = new MenuVista();
        clienteVista.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnRegresar1MouseClicked

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        txtColor.setVisible(true);
        txtMarca.setVisible(true);
        txtModelo.setVisible(true);
        txtPlaca.setVisible(true);
        cmbRFC.setVisible(true);

        jLabel2.setVisible(true);
        jLabel3.setVisible(true);
        jLabel4.setVisible(true);
        jLabel5.setVisible(true);
        jLabel6.setVisible(true);
    }//GEN-LAST:event_jLabel1MouseClicked

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
            java.util.logging.Logger.getLogger(VehiculoVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VehiculoVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VehiculoVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VehiculoVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VehiculoVista().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnRegresar1;
    private javax.swing.JComboBox<String> cmbRFC;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTable tblVehiculos;
    private javax.swing.JTextField txtColor;
    private javax.swing.JTextField txtMarca;
    private javax.swing.JTextField txtModelo;
    private javax.swing.JTextField txtPlaca;
    // End of variables declaration//GEN-END:variables

}
