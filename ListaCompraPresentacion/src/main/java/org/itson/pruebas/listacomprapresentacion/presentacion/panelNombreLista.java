/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package org.itson.pruebas.listacomprapresentacion.presentacion;

import DTOs.ClienteDTO;
import DTOs.CompraDTO;
import com.mycompany.listacompragestorclientes.GestorClientes;
import com.mycompany.listacompragestorcompras.GestorCompras;

/**
 *
 * @author victo
 */
public class panelNombreLista extends javax.swing.JPanel {

    private frmMenuInicio menuInicio;
    private ClienteDTO cliente;
    private GestorCompras gestorCompras;
    
    /**
     * Creates new form panelNombreLista
     */
    public panelNombreLista(frmMenuInicio menuInicio, ClienteDTO cliente) {
        this.menuInicio = menuInicio;
        this.gestorCompras = new GestorCompras();
        this.cliente=cliente;
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtUsuario = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btnContinuar = new javax.swing.JButton();
        txtNombreLista = new javax.swing.JTextField();

        txtUsuario.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 14)); // NOI18N
        txtUsuario.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        setBackground(new java.awt.Color(255, 255, 185));
        setMaximumSize(new java.awt.Dimension(1024, 597));

        jLabel1.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 36)); // NOI18N
        jLabel1.setText("Nombre de la lista");

        btnContinuar.setBackground(new java.awt.Color(254, 194, 58));
        btnContinuar.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
        btnContinuar.setText("Continuar");
        btnContinuar.setFocusable(false);
        btnContinuar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnContinuarActionPerformed(evt);
            }
        });

        txtNombreLista.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 24)); // NOI18N
        txtNombreLista.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(338, 338, 338)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtNombreLista))
                .addContainerGap(339, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnContinuar, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(191, 191, 191)
                .addComponent(jLabel1)
                .addGap(50, 50, 50)
                .addComponent(txtNombreLista, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 172, Short.MAX_VALUE)
                .addComponent(btnContinuar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnContinuarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContinuarActionPerformed
        
        String compraS = txtNombreLista.getText();
        CompraDTO compra = new CompraDTO(compraS, cliente);
        gestorCompras.agregarCompra(compra);
        panelListaProductos agregarProducto = new panelListaProductos(menuInicio, compra);
        menuInicio.mostrarPanel(agregarProducto);
        
    }//GEN-LAST:event_btnContinuarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnContinuar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField txtNombreLista;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
