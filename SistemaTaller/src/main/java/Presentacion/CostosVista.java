/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Presentacion;

import Dominio.Cliente;
import Dominio.Domicilio;
import Dominio.Servicio;
import Persistencia.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;
import Negocio.ControlCliente;
import Negocio.ControlServicio;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author wikit
 */
public class CostosVista extends javax.swing.JFrame {

    Connection conexion;
    private ControlCliente controlCliente = new ControlCliente();
    private ControlServicio controlServicio = new ControlServicio();

    /**
     * Creates new form MenuView
     */
    public CostosVista() {
        initComponents();
        setLocationRelativeTo(null);
        // Abre la conexión aquí
        this.conexion = Conexion.getConnection();
        cargarDatosServicios();
        tblClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                tablaClientesMouseClicked(evt);
            }
        });

        txtNombre.setVisible(false);
        txtCosto.setVisible(false);

        jLabel3.setVisible(false);
        jLabel4.setVisible(false);

    }

    private void cargarDatosServicios() {
        // Modelo de la tabla con columnas Nombre y RFC
        DefaultTableModel modeloTabla = new DefaultTableModel(new Object[]{"Id", "Servicio", "Costo"}, 0);

        try {
            // Usa la conexión existente
            String consultaSQL = "SELECT id_servicio, costo, descripcion FROM servicios";
            PreparedStatement ps = this.conexion.prepareStatement(consultaSQL); // Usa la conexión de la clase
            ResultSet rs = ps.executeQuery();

            // Agrega cada fila de la base de datos al modelo de la tabla
            while (rs.next()) {
                String id = rs.getString("id_servicio");
                String nombre = rs.getString("descripcion");
                String costo = rs.getString("costo");
                modeloTabla.addRow(new Object[]{id, nombre, costo});
            }

            // Cierra el ResultSet y el PreparedStatement
            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al cargar los datos");
        }

        // Asigna el modelo a la tabla
        tblClientes.setModel(modeloTabla);
    }

    private void tablaClientesMouseClicked(MouseEvent evt) {
        int filaSeleccionada = tblClientes.getSelectedRow();
        if (filaSeleccionada >= 0) {
            int id = Integer.parseInt(tblClientes.getValueAt(filaSeleccionada, 0).toString());
            Servicio servicio = controlServicio.obtenerServicioPorId(id);

            if (servicio != null) {

                txtNombre.setVisible(true);
                txtCosto.setVisible(true);

                jLabel3.setVisible(true);
                jLabel4.setVisible(true);

                txtCosto.setText(String.valueOf(servicio.getCosto()));
                txtNombre.setText(servicio.getDescripcion());
            } else {
                System.out.println("Servicio no encontrado con ID: " + id);
                limpiarCampos();
            }
        }
    }

    private void registrarServicio() {
        String nombre = txtNombre.getText();
        String costoTexto = txtCosto.getText();

        // Validación básica de campos vacíos
        if (nombre.isEmpty() || costoTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.");
            return;
        }

        try {
            // Convertir el costo a double
            double costo = Double.parseDouble(costoTexto);

            // Crear el objeto Servicio
            Servicio servicio = new Servicio(nombre, costo);

            // Registrar el servicio en la base de datos
            controlServicio.agregarServicio(servicio);

            // Notificar éxito
            JOptionPane.showMessageDialog(this, "Servicio registrado exitosamente.");

            // Refrescar la tabla con los nuevos datos
            cargarDatosServicios();

            // Limpiar los campos
            limpiarCampos();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El campo 'Costo' debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);

        }
    }

    // Método auxiliar para verificar si existe un cliente con el mismo RFC
    private boolean existeClienteConRfc(String rfc) {
        Cliente cliente = controlCliente.obtenerClientePorRfc(rfc);
        return cliente != null; // Devuelve true si el cliente ya existe
    }

    private void editarCliente() {
        String rfc = txtCosto.getText();
        String nombre = txtNombre.getText();

        // Crear el objeto Cliente a editar
//        Domicilio domicilio = new Domicilio(txtCalle.getText(), txtColonia.getText(), txtNumero.getText());
//        Cliente cliente = new Cliente(rfc, nombre, correo, new java.sql.Date(fechaNac.getTime()), telefono, domicilio);
        // Llamar al método en ControlCliente para editar el cliente
//        boolean exito = controlCliente.editarCliente(cliente);
//        if (exito) {
//            JOptionPane.showMessageDialog(this, "Cliente editado exitosamente.");
//            cargarDatosClientes(); // Método para actualizar la tabla con los nuevos datos
//            limpiarCampos(); // Limpia los campos después de la edición exitosa
//        } else {
//            JOptionPane.showMessageDialog(this, "Error al editar el cliente.");
//        }
    }

    private void eliminarCliente() {
        int filaSeleccionada = tblClientes.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un cliente de la tabla.");
            return; // Salir del método si no hay fila seleccionada
        }

        // Obtener el RFC del cliente seleccionado
        String rfcCliente = tblClientes.getValueAt(filaSeleccionada, 1).toString();

        // Mostrar un JOptionPane de confirmación
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que deseas eliminar al cliente con RFC: " + rfcCliente + "?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            // Llamar al método en ControlCliente para eliminar el cliente
            boolean exito = controlCliente.eliminarCliente(rfcCliente); // Implementa este método en ControlCliente

            if (exito) {
                JOptionPane.showMessageDialog(this, "Cliente eliminado exitosamente.");
                cargarDatosServicios(); // Actualiza la tabla después de eliminar
                limpiarCampos(); // Limpia los campos después de la eliminación
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el cliente.");
            }
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtCosto.setText("");
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
        jLabel1 = new javax.swing.JLabel();
        btnVehiculos = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtCosto = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        btnRegresar = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Bienvenido a Talleres Osman!");
        setBackground(new java.awt.Color(248, 242, 206));
        setMinimumSize(new java.awt.Dimension(1530, 850));
        setResizable(false);
        setSize(new java.awt.Dimension(1530, 850));

        jPanel1.setBackground(new java.awt.Color(248, 242, 206));
        jPanel1.setMaximumSize(new java.awt.Dimension(1530, 800));
        jPanel1.setMinimumSize(new java.awt.Dimension(1530, 800));
        jPanel1.setPreferredSize(new java.awt.Dimension(1530, 800));
        jPanel1.setRequestFocusEnabled(false);
        jPanel1.setLayout(null);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesCostos/Costos.png"))); // NOI18N
        jPanel1.add(jLabel1);
        jLabel1.setBounds(50, 24, 510, 44);

        btnVehiculos.setBackground(new java.awt.Color(248, 242, 206));
        btnVehiculos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesCostos/Agregar.png"))); // NOI18N
        btnVehiculos.setBorder(null);
        btnVehiculos.setBorderPainted(false);
        btnVehiculos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnVehiculosMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnVehiculosMouseExited(evt);
            }
        });
        btnVehiculos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVehiculosActionPerformed(evt);
            }
        });
        jPanel1.add(btnVehiculos);
        btnVehiculos.setBounds(1204, 721, 282, 70);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesCostos/RegistrarCostos.png"))); // NOI18N
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel2);
        jLabel2.setBounds(850, 24, 548, 70);

        txtCosto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCostoActionPerformed(evt);
            }
        });
        jPanel1.add(txtCosto);
        txtCosto.setBounds(786, 230, 340, 39);

        tblClientes.setBackground(new java.awt.Color(216, 217, 137));
        tblClientes.setForeground(new java.awt.Color(73, 61, 63));
        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Servicio", "Costo"
            }
        ));
        tblClientes.setFillsViewportHeight(true);
        jScrollPane1.setViewportView(tblClientes);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(50, 124, 452, 667);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesCostos/NombreDelServicio.png"))); // NOI18N
        jPanel1.add(jLabel3);
        jLabel3.setBounds(786, 169, 350, 31);

        txtNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreActionPerformed(evt);
            }
        });
        jPanel1.add(txtNombre);
        txtNombre.setBounds(786, 124, 700, 39);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesCostos/Costo.png"))); // NOI18N
        jPanel1.add(jLabel4);
        jLabel4.setBounds(786, 275, 100, 31);

        btnRegresar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesCliente/Regresar.png"))); // NOI18N
        btnRegresar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRegresarMouseClicked(evt);
            }
        });
        jPanel1.add(btnRegresar);
        btnRegresar.setBounds(1416, 24, 70, 70);

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesCliente/fondo.png"))); // NOI18N
        jPanel1.add(jLabel11);
        jLabel11.setBounds(360, 70, 740, 720);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreActionPerformed

    private void txtCostoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCostoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoActionPerformed

    private void btnVehiculosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVehiculosActionPerformed
        // TODO add your handling code here:
        this.registrarServicio();
    }//GEN-LAST:event_btnVehiculosActionPerformed

    private void btnVehiculosMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVehiculosMouseExited

    }//GEN-LAST:event_btnVehiculosMouseExited

    private void btnVehiculosMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVehiculosMouseEntered

    }//GEN-LAST:event_btnVehiculosMouseEntered

    private void btnRegresarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegresarMouseClicked
        MenuVista clienteVista = new MenuVista();
        clienteVista.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnRegresarMouseClicked

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        txtNombre.setVisible(true);
        txtCosto.setVisible(true);

        jLabel3.setVisible(true);
        jLabel4.setVisible(true);

    }//GEN-LAST:event_jLabel2MouseClicked

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
            java.util.logging.Logger.getLogger(CostosVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CostosVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CostosVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CostosVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CostosVista().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnRegresar;
    private javax.swing.JButton btnVehiculos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblClientes;
    private javax.swing.JTextField txtCosto;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
