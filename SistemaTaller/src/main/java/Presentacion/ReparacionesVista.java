/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Presentacion;

import Dominio.Cliente;
import Dominio.Reparacion;
import Dominio.ReparacionServicio;
import Dominio.Servicio;
import Dominio.Vehiculo;
import Negocio.ControlCliente;
import Negocio.ControlReparacion;
import Negocio.ControlReparacionServicio;
import Negocio.ControlServicio;
import Persistencia.Conexion;
import java.awt.event.MouseAdapter;
import Negocio.ControlVehiculo;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 *
 * @author hoshi
 */
public class ReparacionesVista extends javax.swing.JFrame {

    Connection conexion;
    private ControlServicio controlServicio = new ControlServicio();
    private ControlVehiculo controlVehiculo = new ControlVehiculo();
    private ControlCliente controlCliente = new ControlCliente();
    private ControlReparacionServicio controlReparacionServicio = new ControlReparacionServicio();
    private ControlReparacion controlReparacion = new ControlReparacion();
    private List<Servicio> serviciosReparacion = new ArrayList<>();

    /**
     * Creates new form VehiculoVista
     */
    public ReparacionesVista() {
        initComponents();
        setLocationRelativeTo(null);

        // Abre la conexión aquí
        this.conexion = Conexion.getConnection();
        cargarReparaciones();
        cargarListas();
        tblReparaciones.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                tablaVehiculosMouseClicked(evt);
            }

        });

        txtEmpleado.setVisible(false);
        txtServicios.setVisible(false);
        cmbServicios.setVisible(false);
        cmbPlaca.setVisible(false);

        jLabel2.setVisible(false);
        jLabel4.setVisible(false);
        jLabel6.setVisible(false);

        btnAgregarServicio.setVisible(false);
    }

    private void cargarListas() {
        List<Servicio> listarServicios = controlServicio.listarServicios();
        cmbServicios.removeAllItems();

        for (Servicio servicio : listarServicios) {
            cmbServicios.addItem(servicio);
        }

        List<Vehiculo> listarPlacas = controlVehiculo.obtenerTodosLosVehiculos();
        cmbPlaca.removeAllItems();

        for (Vehiculo vehiculo : listarPlacas) {
            if (!vehiculo.getEliminada()) {
                cmbPlaca.addItem(vehiculo);
            }
        }
    }

    private void tablaVehiculosMouseClicked(MouseEvent evt) {

        int filaSeleccionada = tblReparaciones.getSelectedRow();
        if (filaSeleccionada >= 0) {
            String placa = tblReparaciones.getValueAt(filaSeleccionada, 0).toString(); // Asumiendo que el RFC está en la segunda columna
            Vehiculo vehiculo = controlVehiculo.obtenerVehiculoPorPlaca(placa); // Método que debes implementar

            if (vehiculo != null) {
                cmbServicios.setSelectedItem(vehiculo.getCliente().getRfc());
                txtServicios.setText(vehiculo.getModelo());
                txtEmpleado.setText(vehiculo.getColor());

                txtEmpleado.setVisible(true);
                txtServicios.setVisible(true);
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

    private void cargarReparaciones() {
        // Crear el modelo de la tabla con las columnas adecuadas
        DefaultTableModel modeloTabla = new DefaultTableModel(new Object[]{"Id", "Placa", "Descripción"}, 0);
        String descripcion = "";

        try {
            // Consulta SQL para obtener las reparaciones activas
            String consultaSQL = "SELECT id, placa_vehiculo FROM reparaciones";
            PreparedStatement ps = this.conexion.prepareStatement(consultaSQL);
            ResultSet rs = ps.executeQuery();

            // Recorrer los resultados y agregarlos al modelo de la tabla
            while (rs.next()) {
                int id = rs.getInt("id");
                String placa = rs.getString("placa_vehiculo");

                for (ReparacionServicio reparacionServicio : controlReparacionServicio.obtenerTodasLasReparacionesServicios()) {
                    if (id == reparacionServicio.getReparacion().getId()) {
                        Servicio temp = controlServicio.obtenerServicioPorId(reparacionServicio.getServicio().getId_servicio());
                        if (!descripcion.equals("")) {
                            descripcion = descripcion + ", " + temp.getDescripcion();
                        } else {
                            descripcion = temp.getDescripcion();
                        }
                    }
                }

                // Agregar la fila al modelo
                modeloTabla.addRow(new Object[]{id, placa, descripcion});
                descripcion = "";
            }

            // Cerrar recursos
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al cargar los datos de reparaciones");
        }

        // Asignar el modelo a la tabla
        tblReparaciones.setModel(modeloTabla);

        TableColumnModel columnModel = tblReparaciones.getColumnModel();

        for (int col = 0; col < tblReparaciones.getColumnCount(); col++) {
            int maxWidth = 0;

            // Obtener ancho del encabezado
            TableColumn column = columnModel.getColumn(col);
            TableCellRenderer headerRenderer = tblReparaciones.getTableHeader().getDefaultRenderer();
            Component headerComp = headerRenderer.getTableCellRendererComponent(
                    tblReparaciones, column.getHeaderValue(), false, false, 0, col);
            maxWidth = headerComp.getPreferredSize().width;

            // Obtener ancho del contenido de las celdas
            for (int row = 0; row < tblReparaciones.getRowCount(); row++) {
                TableCellRenderer cellRenderer = tblReparaciones.getCellRenderer(row, col);
                Component comp = tblReparaciones.prepareRenderer(cellRenderer, row, col);
                maxWidth = Math.max(comp.getPreferredSize().width, maxWidth);
            }

            // Ajustar el ancho de la columna
            column.setPreferredWidth(maxWidth + 10); // Agregar un margen
        }
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
        txtEmpleado.setText("");
        cargarListas();
    }

    private void agregarServiciosLista() {
        Servicio servicio = (Servicio) cmbServicios.getSelectedItem();
        if (txtServicios.getText().equals("")) {
            txtServicios.setText(servicio.getDescripcion());
            serviciosReparacion.add(servicio);
        } else {
            txtServicios.setText(txtServicios.getText() + ", " + servicio.getDescripcion());
            serviciosReparacion.add(servicio);
        }
        cmbServicios.removeItem(servicio);
    }

    private void agregarReparacion() {
        int nuevoId = -1;
        Reparacion reparacion = new Reparacion(txtEmpleado.getText(), (Vehiculo) cmbPlaca.getSelectedItem());
        try {
            String sql = "INSERT INTO reparaciones (nombre_empleado, placa_vehiculo) VALUES (?, ?)";
            PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, reparacion.getNombre_empleado());
            stmt.setString(2, reparacion.getVehiculo().getPlaca());
            stmt.executeUpdate();

            // Obtener el ID generado
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                nuevoId = rs.getInt(1); // El ID generado
            }

            if (nuevoId <= 0) {
                System.err.println("Error al agregar la reparación.");
            }

            // Actualizar el objeto Reparación con el nuevo ID
            reparacion.setId(nuevoId);

            // Agregar servicios relacionados con la reparación
            for (Servicio servicio : serviciosReparacion) {
                controlReparacionServicio.agregarReparacionServicio(new ReparacionServicio(reparacion, servicio));
            }
            System.out.println("Reparación agregada con ID: " + nuevoId);
        } catch (SQLException e) {
            e.printStackTrace();
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
        lblTitulo = new javax.swing.JLabel();
        btnRegresar1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblReparaciones = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        txtServicios = new javax.swing.JTextField();
        txtEmpleado = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnAgregarServicio = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        cmbServicios = new javax.swing.JComboBox<>();
        btnAgregar = new javax.swing.JButton();
        cmbPlaca = new javax.swing.JComboBox<>();
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

        tblReparaciones.setBackground(new java.awt.Color(216, 217, 137));
        tblReparaciones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Id", "Placa", "Descripción"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblReparaciones.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane1.setViewportView(tblReparaciones);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(50, 124, 520, 500);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesReparaciones/Servicios.png"))); // NOI18N
        jPanel1.add(jLabel2);
        jLabel2.setBounds(640, 180, 160, 31);

        txtServicios.setEnabled(false);
        txtServicios.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtServiciosKeyTyped(evt);
            }
        });
        jPanel1.add(txtServicios);
        txtServicios.setBounds(640, 240, 700, 48);
        jPanel1.add(txtEmpleado);
        txtEmpleado.setBounds(640, 320, 700, 48);

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesReparaciones/Empleado.png"))); // NOI18N
        jPanel1.add(jLabel6);
        jLabel6.setBounds(640, 380, 160, 31);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesReparaciones/Placa.png"))); // NOI18N
        jPanel1.add(jLabel4);
        jLabel4.setBounds(640, 480, 94, 31);

        btnAgregarServicio.setBackground(new java.awt.Color(248, 242, 206));
        btnAgregarServicio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesReparaciones/Agregar1.png"))); // NOI18N
        btnAgregarServicio.setBorderPainted(false);
        btnAgregarServicio.setContentAreaFilled(false);
        btnAgregarServicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarServicioActionPerformed(evt);
            }
        });
        jPanel1.add(btnAgregarServicio);
        btnAgregarServicio.setBounds(1040, 120, 300, 69);

        jLabel1.setBackground(new java.awt.Color(248, 242, 206));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesReparaciones/NuevaReparacion.png"))); // NOI18N
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel1);
        jLabel1.setBounds(710, 20, 548, 70);

        jPanel1.add(cmbServicios);
        cmbServicios.setBounds(640, 120, 340, 40);

        btnAgregar.setBackground(new java.awt.Color(248, 242, 206));
        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesReparaciones/Agregar2.png"))); // NOI18N
        btnAgregar.setBorderPainted(false);
        btnAgregar.setContentAreaFilled(false);
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });
        jPanel1.add(btnAgregar);
        btnAgregar.setBounds(1060, 540, 280, 69);

        jPanel1.add(cmbPlaca);
        cmbPlaca.setBounds(640, 430, 340, 40);

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

    private void btnAgregarServicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarServicioActionPerformed
        agregarServiciosLista();
    }//GEN-LAST:event_btnAgregarServicioActionPerformed

    private void txtServiciosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtServiciosKeyTyped

    }//GEN-LAST:event_txtServiciosKeyTyped

    private void btnRegresar1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegresar1MouseClicked
        MenuVista clienteVista = new MenuVista();
        clienteVista.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnRegresar1MouseClicked

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        txtEmpleado.setVisible(true);
        txtServicios.setVisible(true);
        cmbServicios.setVisible(true);
        cmbPlaca.setVisible(true);

        jLabel2.setVisible(true);
        jLabel4.setVisible(true);
        jLabel6.setVisible(true);

        btnAgregarServicio.setVisible(true);
    }//GEN-LAST:event_jLabel1MouseClicked

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        agregarReparacion();
        serviciosReparacion.removeAll(serviciosReparacion);
        limpiarCampos();
    }//GEN-LAST:event_btnAgregarActionPerformed

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
    private javax.swing.JButton btnAgregarServicio;
    private javax.swing.JLabel btnRegresar1;
    private javax.swing.JComboBox<Vehiculo> cmbPlaca;
    private javax.swing.JComboBox<Servicio> cmbServicios;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTable tblReparaciones;
    private javax.swing.JTextField txtEmpleado;
    private javax.swing.JTextField txtServicios;
    // End of variables declaration//GEN-END:variables

}
