/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package org.itson.pruebas.listacomprapresentacion.presentacion;

import DTOs.CompraDTO;
import DTOs.ProductoDTO;
import com.mycompany.listacomprafiltroporcategoria.FiltroPorCategoria;
import com.mycompany.listacomprafiltroporcategoria.IFiltroPorCategoria;
import com.mycompany.listacomprafiltroporcompra.FiltroPorCompra;
import com.mycompany.listacomprafiltroporcompra.IFiltroPorCompra;
import com.mycompany.listacompragestorcompras.IGestorCompras;
import com.mycompany.listacompragestorproductos.GestorProductos;
import com.mycompany.listacompragestorproductos.IGestorProductos;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author victo
 */
public class panelListaProductos extends javax.swing.JPanel {

    private frmMenuInicio menuInicio;
    private CompraDTO compra;
    private IGestorProductos gestorProductos;
    private IGestorCompras gestorCompras;
    private IFiltroPorCompra filtroCompra;
    private IFiltroPorCategoria filtroCategoria;
    private final Object[] columnNames = {"Nombre", "Cantidad", "Categoria", "Comprado"};

    /**
     * Creates new form panelAgregarProducto
     */
    public panelListaProductos(frmMenuInicio menuInicio, CompraDTO compra) {
        this.menuInicio = menuInicio;
        this.compra = compra;
        gestorProductos = new GestorProductos();
        filtroCompra = new FiltroPorCompra();
        filtroCategoria = new FiltroPorCategoria();
        initComponents();
        tblListaProductos.getTableHeader().setFont(new Font("MS Reference Sans Serif", Font.BOLD, 14));
        mostrarListaProductos();
        actualizarProducto();
    }

    private void mostrarListaProductos() {
        tblListaProductos.setModel(modelo);
        modelo.setRowCount(0);
        List<ProductoDTO> listaProductoCliente = filtroCompra.obtenerProductosFiltrarPorCompra(compra.getId());
        if (listaProductoCliente != null) {
            listaProductoCliente.forEach(p -> modelo.addRow(new Object[]{p.getNombre(), p.getCantidad(), p.getCategoria(), p.isComprado()}));
        }
        realizarAccionCheckbox();

    }

    private void realizarAccionCheckbox() {
        modelo.addTableModelListener((TableModelEvent e) -> {
            if (e.getColumn() == 3) {
                int row = e.getFirstRow();
                boolean comprado = (Boolean) modelo.getValueAt(row, 3);
                if (comprado) {
                    ProductoDTO productoBuscar = new ProductoDTO();
                    productoBuscar.setNombre(modelo.getValueAt(row, 0).toString());
                    productoBuscar.setCantidad(Double.valueOf(modelo.getValueAt(row, 1).toString()));
                    productoBuscar.setCategoria(modelo.getValueAt(row, 2).toString());
                    productoBuscar.setComprado(false);

                    ProductoDTO productoSelec = gestorProductos.obtenerProductoPorCaracteristicas(productoBuscar.getNombre(), productoBuscar.getCategoria(), productoBuscar.isComprado(), productoBuscar.getCantidad(), compra.getId());

                    productoSelec.setComprado(comprado);
                    productoSelec.setCompra(compra);
                    gestorProductos.actualizarProducto(productoSelec);
                    tblListaProductos.isCellEditable(row, 3);
                }
            }
        });

    }

    DefaultTableModel modelo = new DefaultTableModel(new Object[0][0], columnNames) {
        @Override
        public boolean isCellEditable(int row, int column) {
            if (column >= 0 && column <= 2) {
                return false;
            }
            if (column == 3) {
                Boolean value = (Boolean) getValueAt(row, column);
                return Boolean.FALSE.equals(value);
            }
            return super.isCellEditable(row, column);
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 3) {
                return Boolean.class;
            }
            return super.getColumnClass(columnIndex);
        }
    };

    private void actualizarProducto() {

        tblListaProductos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {

                    int filaSeleccionada = tblListaProductos.getSelectedRow();

                    if (filaSeleccionada != -1) {
                        Object[] datosFila = new Object[tblListaProductos.getColumnCount()];

                        for (int i = 0; i < tblListaProductos.getColumnCount(); i++) {
                            datosFila[i] = tblListaProductos.getValueAt(filaSeleccionada, i);
                        }

                        ProductoDTO productoBuscar = new ProductoDTO();
                        productoBuscar.setNombre(datosFila[0].toString());
                        productoBuscar.setCantidad(Double.valueOf(datosFila[1].toString()));
                        productoBuscar.setCategoria(datosFila[2].toString());
                        productoBuscar.setComprado(Boolean.parseBoolean(datosFila[3].toString()));
                        ProductoDTO productoSelec = gestorProductos.obtenerProductoPorCaracteristicas(productoBuscar.getNombre(), productoBuscar.getCategoria(), productoBuscar.isComprado(), productoBuscar.getCantidad(), compra.getId());
                        productoSelec.setCompra(compra);

                        panelDatosProducto actualizarDatosProducto = new panelDatosProducto(menuInicio, compra, productoSelec, true);
                        menuInicio.mostrarPanel(actualizarDatosProducto);
                    }
                }
            }
        });

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblListaProductos = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btnAgregarProducto = new javax.swing.JButton();
        btnEliminarProducto = new javax.swing.JButton();
        btnFiltrarCategoria = new javax.swing.JButton();
        txtCategoria = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        btnMostrarTodo = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 185));

        tblListaProductos.setAutoCreateRowSorter(true);
        tblListaProductos.setBackground(new java.awt.Color(255, 255, 185));
        tblListaProductos.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 14)); // NOI18N
        tblListaProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Producto", "Cantidad", "Categoria", "Comprado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblListaProductos);

        jLabel1.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 28)); // NOI18N
        jLabel1.setText("Lista de productos");

        btnAgregarProducto.setBackground(new java.awt.Color(254, 194, 58));
        btnAgregarProducto.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
        btnAgregarProducto.setText("Agregar producto");
        btnAgregarProducto.setFocusable(false);
        btnAgregarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarProductoActionPerformed(evt);
            }
        });

        btnEliminarProducto.setBackground(new java.awt.Color(254, 194, 58));
        btnEliminarProducto.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
        btnEliminarProducto.setText("Eliminar producto");
        btnEliminarProducto.setFocusable(false);
        btnEliminarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarProductoActionPerformed(evt);
            }
        });

        btnFiltrarCategoria.setBackground(new java.awt.Color(254, 194, 58));
        btnFiltrarCategoria.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
        btnFiltrarCategoria.setText("Filtrar");
        btnFiltrarCategoria.setFocusable(false);
        btnFiltrarCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFiltrarCategoriaActionPerformed(evt);
            }
        });

        txtCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCategoriaActionPerformed(evt);
            }
        });

        jLabel3.setText("Ingrese una Categoría");

        btnMostrarTodo.setBackground(new java.awt.Color(254, 194, 58));
        btnMostrarTodo.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
        btnMostrarTodo.setText("Mostrar Todos los Productos");
        btnMostrarTodo.setFocusable(false);
        btnMostrarTodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarTodoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(95, 95, 95)
                .addComponent(btnEliminarProducto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAgregarProducto)
                .addGap(90, 90, 90))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 731, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(146, 146, 146))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(btnMostrarTodo)
                .addGap(89, 89, 89)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 134, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnFiltrarCategoria)))
                .addGap(28, 28, 28))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(btnFiltrarCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMostrarTodo, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(30, 30, 30)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(41, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarProductoActionPerformed
        panelDatosProducto agregarDatosProducto = new panelDatosProducto(menuInicio, compra);
        menuInicio.mostrarPanel(agregarDatosProducto);
    }//GEN-LAST:event_btnAgregarProductoActionPerformed

    private void btnEliminarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarProductoActionPerformed
        int filaSeleccionada = tblListaProductos.getSelectedRow();

        if (filaSeleccionada != -1) {
            Object[] datosFila = new Object[tblListaProductos.getColumnCount()];

            for (int i = 0; i < tblListaProductos.getColumnCount(); i++) {
                datosFila[i] = tblListaProductos.getValueAt(filaSeleccionada, i);
            }

            int respuesta = JOptionPane.showConfirmDialog(this, "¿Estás seguro de borrar este producto?", "Atención", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                ProductoDTO productoBuscar = new ProductoDTO();
                productoBuscar.setNombre(datosFila[0].toString());
                productoBuscar.setCantidad(Double.valueOf(datosFila[1].toString()));
                productoBuscar.setCategoria(datosFila[2].toString());
                productoBuscar.setComprado(Boolean.parseBoolean(datosFila[3].toString()));

                ProductoDTO productoSelec = gestorProductos.obtenerProductoPorCaracteristicas(productoBuscar.getNombre(), productoBuscar.getCategoria(), productoBuscar.isComprado(), productoBuscar.getCantidad(), compra.getId());
                gestorProductos.eliminarProducto(productoSelec.getId());
                mostrarListaProductos();
            }

        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un producto", "Atención", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnEliminarProductoActionPerformed

    private void btnFiltrarCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarCategoriaActionPerformed
        DefaultTableModel modelo = (DefaultTableModel) tblListaProductos.getModel();

        if (!txtCategoria.getText().isBlank() && txtCategoria.getText() != null) {
            modelo.setRowCount(0);
            List<ProductoDTO> listaProductoCliente = filtroCategoria.filtrarPorCategoriaYCompraId(txtCategoria.getText(), compra.getId());
            if (listaProductoCliente != null) {
                listaProductoCliente.forEach(p -> modelo.addRow(new Object[]{p.getNombre(), p.getCantidad(), p.getCategoria(), p.isComprado()}));
            }
        } else {
            JOptionPane.showMessageDialog(this, "Ingrese una Categoría", "Categoría vacía", JOptionPane.INFORMATION_MESSAGE);
        }


    }//GEN-LAST:event_btnFiltrarCategoriaActionPerformed

    private void txtCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCategoriaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCategoriaActionPerformed

    private void btnMostrarTodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarTodoActionPerformed
        mostrarListaProductos();

    }//GEN-LAST:event_btnMostrarTodoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarProducto;
    private javax.swing.JButton btnEliminarProducto;
    private javax.swing.JButton btnFiltrarCategoria;
    private javax.swing.JButton btnMostrarTodo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblListaProductos;
    private javax.swing.JTextField txtCategoria;
    // End of variables declaration//GEN-END:variables
}
