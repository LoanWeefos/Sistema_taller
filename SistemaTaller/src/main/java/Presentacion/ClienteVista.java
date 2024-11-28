/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Presentacion;

import Dominio.Cliente;
import Dominio.Domicilio;
import Persistencia.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;
import Negocio.ControlCliente;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author wikit
 */
public class ClienteVista extends javax.swing.JFrame {

    Connection conexion;
    private ControlCliente controlCliente = new ControlCliente();

    /**
     * Creates new form MenuView
     */
    public ClienteVista() {
        initComponents();
        //setLocationRelativeTo(null);
        // Abre la conexión aquí
        this.conexion = Conexion.getConnection();
        cargarDatosClientes();
        tblClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                tablaClientesMouseClicked(evt);
            }
        });
        
        txtCalle.setVisible(false);
        txtColonia.setVisible(false);
        txtCorreo.setVisible(false);
        txtFechaN.setVisible(false);
        txtNombre.setVisible(false);
        txtNumero.setVisible(false);
        txtRFC.setVisible(false);
        txtTelefono.setVisible(false);
        
        jLabel3.setVisible(false);
        jLabel4.setVisible(false);
        jLabel5.setVisible(false);
        jLabel6.setVisible(false);
        jLabel7.setVisible(false);
        jLabel8.setVisible(false);
        jLabel9.setVisible(false);
        jLabel10.setVisible(false);
        
    }

    private void cargarDatosClientes() {
        // Modelo de la tabla con columnas Nombre y RFC
        DefaultTableModel modeloTabla = new DefaultTableModel(new Object[]{"Nombre", "RFC"}, 0);

        try {
            // Usa la conexión existente
            String consultaSQL = "SELECT Nombre, RFC FROM clientes";
            PreparedStatement ps = this.conexion.prepareStatement(consultaSQL); // Usa la conexión de la clase
            ResultSet rs = ps.executeQuery();

            // Agrega cada fila de la base de datos al modelo de la tabla
            while (rs.next()) {
                String nombre = rs.getString("Nombre");
                String rfc = rs.getString("RFC");
                modeloTabla.addRow(new Object[]{nombre, rfc});
            }

            // Cierra el ResultSet y el PreparedStatement
            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al cargar los datos de clientes");
        }

        // Asigna el modelo a la tabla
        tblClientes.setModel(modeloTabla);
    }

    private void tablaClientesMouseClicked(MouseEvent evt) {
        int filaSeleccionada = tblClientes.getSelectedRow();
        if (filaSeleccionada >= 0) {
            String rfc = tblClientes.getValueAt(filaSeleccionada, 1).toString(); // Asumiendo que el RFC está en la segunda columna
            Cliente cliente = controlCliente.obtenerClientePorRfc(rfc); // Método que debes implementar

            if (cliente != null) {
                txtCalle.setVisible(true);
                txtColonia.setVisible(true);
                txtCorreo.setVisible(true);
                txtFechaN.setVisible(true);
                txtNombre.setVisible(true);
                txtNumero.setVisible(true);
                txtRFC.setVisible(true);
                txtTelefono.setVisible(true);

                jLabel3.setVisible(true);
                jLabel4.setVisible(true);
                jLabel5.setVisible(true);
                jLabel6.setVisible(true);
                jLabel7.setVisible(true);
                jLabel8.setVisible(true);
                jLabel9.setVisible(true);
                jLabel10.setVisible(true);
                // Cargar los datos del cliente en los campos de texto
                txtRFC.setText(cliente.getRfc());
                txtNombre.setText(cliente.getNombre());
                txtCorreo.setText(cliente.getCorreo());
                txtTelefono.setText(cliente.getTelefono());
                txtCalle.setText(cliente.getDomicilio().getCalle());
                txtColonia.setText(cliente.getDomicilio().getColonia());
                txtNumero.setText(cliente.getDomicilio().getNumero());
                txtFechaN.setDate(cliente.getFechaNacimiento());
            } else {
                System.out.println("Cliente no encontrado con RFC: " + rfc);
                limpiarCampos(); // Limpiar campos si no se encuentra cliente
            }
        }
    }

    private void registrarCliente() {
        String nombre = txtNombre.getText();
        String rfc = txtRFC.getText();
        String correo = txtCorreo.getText();
        java.util.Date fechaNac = txtFechaN.getDate();

        // Crear un objeto Domicilio embebido
        String colonia = txtColonia.getText();
        String numero = txtNumero.getText();
        String telefono = txtTelefono.getText();
        String calle = txtCalle.getText();
        Domicilio domicilio = new Domicilio(calle, colonia, numero);

        // Validación básica de campos vacíos
        if (nombre.isEmpty() || rfc.isEmpty() || correo.isEmpty() || colonia.isEmpty()
                || numero.isEmpty() || telefono.isEmpty() || calle.isEmpty() || fechaNac == null) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.");
            return;
        }

        // Verificar si el RFC ya está registrado
        if (existeClienteConRfc(rfc)) {
            JOptionPane.showMessageDialog(this, "Ya existe un cliente registrado con el mismo RFC.");
            return;
        }

        // Convertir la fecha a java.sql.Date
        java.sql.Date fechaSQL = new java.sql.Date(fechaNac.getTime());

        // Crear el objeto Cliente
        Cliente cliente = new Cliente(rfc, nombre, correo, fechaSQL, telefono, domicilio);

        // Llamar al método agregarCliente en ControlCliente
        boolean exito = controlCliente.agregarCliente(cliente);

        if (exito) {
            JOptionPane.showMessageDialog(this, "Cliente registrado exitosamente.");
            limpiarCampos(); // Limpia los campos después de la inserción exitosa
            cargarDatosClientes(); // Actualiza la tabla con los nuevos datos
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar el cliente");
        }
    }

    // Método auxiliar para verificar si existe un cliente con el mismo RFC
    private boolean existeClienteConRfc(String rfc) {
        Cliente cliente = controlCliente.obtenerClientePorRfc(rfc);
        return cliente != null; // Devuelve true si el cliente ya existe
    }

    private void editarCliente() {
        String rfc = txtRFC.getText();
        String nombre = txtNombre.getText();
        String correo = txtCorreo.getText();
        java.util.Date fechaNac = txtFechaN.getDate();
        String telefono = txtTelefono.getText();

        // Crear el objeto Cliente a editar
        Domicilio domicilio = new Domicilio(txtCalle.getText(), txtColonia.getText(), txtNumero.getText());
        Cliente cliente = new Cliente(rfc, nombre, correo, new java.sql.Date(fechaNac.getTime()), telefono, domicilio);

        // Llamar al método en ControlCliente para editar el cliente
        boolean exito = controlCliente.editarCliente(cliente);

        if (exito) {
            JOptionPane.showMessageDialog(this, "Cliente editado exitosamente.");
            cargarDatosClientes(); // Método para actualizar la tabla con los nuevos datos
            limpiarCampos(); // Limpia los campos después de la edición exitosa
        } else {
            JOptionPane.showMessageDialog(this, "Error al editar el cliente.");
        }
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
                cargarDatosClientes(); // Actualiza la tabla después de eliminar
                limpiarCampos(); // Limpia los campos después de la eliminación
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el cliente.");
            }
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtRFC.setText("");
        txtCorreo.setText("");
        txtColonia.setText("");
        txtNumero.setText("");
        txtTelefono.setText("");
        txtCalle.setText("");
        txtFechaN.setDate(null);
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
        txtRFC = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        txtFechaN = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtCorreo = new javax.swing.JTextField();
        txtColonia = new javax.swing.JTextField();
        txtNumero = new javax.swing.JTextField();
        txtTelefono = new javax.swing.JTextField();
        txtCalle = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        btnEditar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
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

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesCliente/TextoCliente.png"))); // NOI18N
        jPanel1.add(jLabel1);
        jLabel1.setBounds(50, 24, 510, 44);

        btnVehiculos.setBackground(new java.awt.Color(248, 242, 206));
        btnVehiculos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesCliente/Registrar.png"))); // NOI18N
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

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesCliente/RegistrarCliente.png"))); // NOI18N
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel2);
        jLabel2.setBounds(850, 24, 548, 70);

        txtRFC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRFCActionPerformed(evt);
            }
        });
        jPanel1.add(txtRFC);
        txtRFC.setBounds(786, 230, 340, 39);

        tblClientes.setBackground(new java.awt.Color(216, 217, 137));
        tblClientes.setForeground(new java.awt.Color(73, 61, 63));
        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Nombre", "RFC"
            }
        ));
        tblClientes.setFillsViewportHeight(true);
        jScrollPane1.setViewportView(tblClientes);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(50, 124, 452, 667);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesCliente/Nombre.png"))); // NOI18N
        jPanel1.add(jLabel3);
        jLabel3.setBounds(786, 169, 123, 31);

        txtNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreActionPerformed(evt);
            }
        });
        jPanel1.add(txtNombre);
        txtNombre.setBounds(786, 124, 700, 39);

        txtFechaN.setOpaque(false);
        jPanel1.add(txtFechaN);
        txtFechaN.setBounds(1146, 230, 340, 39);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesCliente/RFC.png"))); // NOI18N
        jPanel1.add(jLabel4);
        jLabel4.setBounds(786, 275, 55, 31);

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesCliente/FechaN.png"))); // NOI18N
        jPanel1.add(jLabel5);
        jLabel5.setBounds(1146, 275, 326, 31);

        txtCorreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCorreoActionPerformed(evt);
            }
        });
        jPanel1.add(txtCorreo);
        txtCorreo.setBounds(786, 336, 700, 39);

        txtColonia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtColoniaActionPerformed(evt);
            }
        });
        jPanel1.add(txtColonia);
        txtColonia.setBounds(1146, 556, 340, 38);

        txtNumero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroActionPerformed(evt);
            }
        });
        jPanel1.add(txtNumero);
        txtNumero.setBounds(786, 556, 340, 38);

        txtTelefono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelefonoActionPerformed(evt);
            }
        });
        txtTelefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelefonoKeyTyped(evt);
            }
        });
        jPanel1.add(txtTelefono);
        txtTelefono.setBounds(786, 442, 340, 38);

        txtCalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCalleActionPerformed(evt);
            }
        });
        jPanel1.add(txtCalle);
        txtCalle.setBounds(1146, 442, 340, 38);

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesCliente/Correo.png"))); // NOI18N
        jLabel6.setToolTipText("");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(786, 381, 119, 31);

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesCliente/Telefono.png"))); // NOI18N
        jPanel1.add(jLabel7);
        jLabel7.setBounds(786, 486, 141, 40);

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesCliente/Calle.png"))); // NOI18N
        jPanel1.add(jLabel8);
        jLabel8.setBounds(1146, 495, 87, 31);

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesCliente/NumCasa.png"))); // NOI18N
        jPanel1.add(jLabel9);
        jLabel9.setBounds(786, 600, 151, 31);

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesCliente/Colonia .png"))); // NOI18N
        jPanel1.add(jLabel10);
        jLabel10.setBounds(1146, 600, 128, 31);

        btnEditar.setBackground(new java.awt.Color(248, 242, 206));
        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesCliente/Editar.png"))); // NOI18N
        btnEditar.setBorder(null);
        btnEditar.setBorderPainted(false);
        btnEditar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEditarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEditarMouseExited(evt);
            }
        });
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });
        jPanel1.add(btnEditar);
        btnEditar.setBounds(924, 721, 250, 70);

        btnEliminar.setBackground(new java.awt.Color(248, 242, 206));
        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesCliente/Eliminar.png"))); // NOI18N
        btnEliminar.setBorder(null);
        btnEliminar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEliminarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEliminarMouseExited(evt);
            }
        });
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        jPanel1.add(btnEliminar);
        btnEliminar.setBounds(644, 721, 250, 70);

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

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        this.eliminarCliente();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnEliminarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEliminarMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEliminarMouseExited

    private void btnEliminarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEliminarMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEliminarMouseEntered

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        this.editarCliente();
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnEditarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditarMouseExited

    }//GEN-LAST:event_btnEditarMouseExited

    private void btnEditarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditarMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditarMouseEntered

    private void txtCalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCalleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCalleActionPerformed

    private void txtTelefonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefonoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoActionPerformed

    private void txtNumeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroActionPerformed

    private void txtColoniaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtColoniaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtColoniaActionPerformed

    private void txtCorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCorreoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreoActionPerformed

    private void txtNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreActionPerformed

    private void txtRFCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRFCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRFCActionPerformed

    private void btnVehiculosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVehiculosActionPerformed
        // TODO add your handling code here:
        this.registrarCliente();
    }//GEN-LAST:event_btnVehiculosActionPerformed

    private void btnVehiculosMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVehiculosMouseExited

    }//GEN-LAST:event_btnVehiculosMouseExited

    private void btnVehiculosMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVehiculosMouseEntered

    }//GEN-LAST:event_btnVehiculosMouseEntered

    private void txtTelefonoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoKeyTyped
        // TODO add your handling code here:
         // TODO add your handling code here:
         char c = evt.getKeyChar();

        if ((c < '0' || c > '9')) {
            evt.consume();
        }

        if (txtTelefono.getText().length() == 10) {
            evt.consume();
        }
    }//GEN-LAST:event_txtTelefonoKeyTyped

    private void btnRegresarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegresarMouseClicked
        MenuVista clienteVista = new MenuVista();
        clienteVista.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnRegresarMouseClicked

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        txtCalle.setVisible(true);
        txtColonia.setVisible(true);
        txtCorreo.setVisible(true);
        txtFechaN.setVisible(true);
        txtNombre.setVisible(true);
        txtNumero.setVisible(true);
        txtRFC.setVisible(true);
        txtTelefono.setVisible(true);
        
        jLabel3.setVisible(true);
        jLabel4.setVisible(true);
        jLabel5.setVisible(true);
        jLabel6.setVisible(true);
        jLabel7.setVisible(true);
        jLabel8.setVisible(true);
        jLabel9.setVisible(true);
        jLabel10.setVisible(true);
        
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
            java.util.logging.Logger.getLogger(ClienteVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClienteVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClienteVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClienteVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClienteVista().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JLabel btnRegresar;
    private javax.swing.JButton btnVehiculos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblClientes;
    private javax.swing.JTextField txtCalle;
    private javax.swing.JTextField txtColonia;
    private javax.swing.JTextField txtCorreo;
    private com.toedter.calendar.JDateChooser txtFechaN;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtNumero;
    private javax.swing.JTextField txtRFC;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
