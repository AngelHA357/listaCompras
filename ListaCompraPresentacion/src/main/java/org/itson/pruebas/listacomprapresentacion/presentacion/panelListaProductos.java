package org.itson.pruebas.listacomprapresentacion.presentacion;

import DTOs.CompraDTO;
import DTOs.ProductoDTO;
import Subsistemas.IFiltroPorCategoria;
import Subsistemas.FiltroPorCategoria;
import Subsistemas.IFiltroPorCompra;
import Subsistemas.FiltroPorCompra;
import Subsistemas.IGestorProductos;
import Subsistemas.GestorProductos;
import Subsistemas.IGestorCompras;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Panel que permite mostrar las lista de productos contenidos en una lista de
 * compras.
 *
 * @author Víctor Encinas - 244821 , José Armenta - 247641 , José Huerta -
 * 245345.
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
     * Método constructor que nos permite crear el frame y además recibe el
     * valor del frame menu inicio y la compra. Además inicializa el valor del
     * frame principal y la compra. También crea la instancia GestorProductos,
     * FiltroPorCompra y FiltroPorCategoría.
     *
     * @param menuInicio Frame de menu inicio donde se pintará este panel.
     * @param compra Compra actual.
     */
    public panelListaProductos(frmMenuInicio menuInicio, CompraDTO compra) {
        this.menuInicio = menuInicio;
        this.compra = compra;
        gestorProductos = new GestorProductos();
        filtroCompra = new FiltroPorCompra();
        filtroCategoria = new FiltroPorCategoria();
        initComponents();
        decorarTabla();
        mostrarListaProductos();
        actualizarProducto();
    }

    /**
     * Permite agregarle detalles estéticos a la tabla donde se muestran los
     * productos.
     */
    private void decorarTabla() {
        tblListaProductos.getTableHeader().setFont(new Font("MS Reference Sans Serif", Font.BOLD, 14));
        tblListaProductos.getTableHeader().setReorderingAllowed(false);
        tblListaProductos.getTableHeader().setBackground(new Color(255, 255, 185));
        JScrollPane scrollPane = (JScrollPane) tblListaProductos.getParent().getParent();
        scrollPane.getViewport().setBackground(new Color(255, 255, 185));
    }

    /**
     * Permite llenar la tabla con las listas de productos que tiene una compra.
     */
    private void mostrarListaProductos() {
        tblListaProductos.setModel(modelo);
        modelo.setRowCount(0);
        List<ProductoDTO> listaProductoCliente = filtroCompra.obtenerProductosFiltrarPorCompra(compra.getId());
        if (listaProductoCliente != null) {
            listaProductoCliente.forEach(p -> modelo.addRow(new Object[]{p.getNombre(), p.getCantidad(), p.getCategoria(), p.isComprado()}));
        }
        realizarAccionCheckbox();
        aplicarColorFilas();
    }

    /**
     * Permite que el checkBox cambie el estado de producto como comprado.
     */
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
                }
            }
        });
    }

    /**
     * Permite ya que el producto haya sido marcado como comprado que se cambie
     * el color de la fila por verde para indicar que ya fue comprado.
     */
    private void aplicarColorFilas() {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (column != 3) {
                    Object compradoObj = table.getValueAt(row, 3);
                    if (compradoObj instanceof Boolean && (Boolean) compradoObj) {
                        cell.setBackground(Color.GREEN);
                    } else {
                        cell.setBackground(Color.WHITE);
                    }
                }

                if (isSelected) {
                    cell.setBackground(table.getSelectionBackground());
                    cell.setForeground(table.getSelectionForeground());
                } else {
                    cell.setForeground(Color.BLACK);
                }

                return cell;
            }
        };

        for (int i = 0; i < tblListaProductos.getColumnCount(); i++) {
            if (i != 3) {
                tblListaProductos.getColumnModel().getColumn(i).setCellRenderer(renderer);
            }
        }
    }

    /**
     * Permite que todas las columnas no sean editables excepto la que contiene
     * el checkbox.
     */
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

    /**
     * Método que permite seleccionar un producto para que se pueda actualizar.
     * Funciona al hacer doble clic en la fila.
     */
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
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 185));

        tblListaProductos.setAutoCreateRowSorter(true);
        tblListaProductos.setBackground(new java.awt.Color(255, 255, 220));
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
        tblListaProductos.setAlignmentX(1.0F);
        tblListaProductos.setAlignmentY(1.0F);
        tblListaProductos.setGridColor(new java.awt.Color(255, 255, 185));
        tblListaProductos.setRowHeight(25);
        tblListaProductos.setSelectionBackground(new java.awt.Color(255, 255, 185));
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

        jLabel3.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 12)); // NOI18N
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

        jLabel4.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 12)); // NOI18N
        jLabel4.setText("Para editar un producto haga doble clic en la fila");

        jLabel5.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 12)); // NOI18N
        jLabel5.setText("Para eliminar seleccione un producto y luego presione el botón");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(btnMostrarTodo)
                        .addGap(89, 89, 89)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 134, Short.MAX_VALUE)
                        .addComponent(txtCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnFiltrarCategoria)
                .addGap(28, 28, 28))
            .addGroup(layout.createSequentialGroup()
                .addGap(95, 95, 95)
                .addComponent(btnEliminarProducto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAgregarProducto)
                .addGap(90, 90, 90))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 731, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(146, 146, 146))
            .addGroup(layout.createSequentialGroup()
                .addGap(371, 371, 371)
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addContainerGap(41, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Método que permite mostrar el panel para poder agregar un producto.
     *
     * @param evt Evento al hacer clic en el botón.
     */
    private void btnAgregarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarProductoActionPerformed
        panelDatosProducto agregarDatosProducto = new panelDatosProducto(menuInicio, compra);
        menuInicio.mostrarPanel(agregarDatosProducto);
    }//GEN-LAST:event_btnAgregarProductoActionPerformed

    /**
     * Método que permite borrar un producto de la lista de compras. Se obtiene
     * el producto al seleccionar la fila y hacerle clic a este botón.
     *
     * @param evt Evento al hacer clic en el botón.
     */
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
    /**
     * Método que permite filtrar la lista de productos según el texto
     * ingresado.
     *
     * @param evt Evento al hacer clic en el botón.
     */
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

    /**
     * Método que permite ver todos los productos de una compra.
     *
     * @param evt Evento al hacer clic en el botón.
     */
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
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblListaProductos;
    private javax.swing.JTextField txtCategoria;
    // End of variables declaration//GEN-END:variables
}
