package org.itson.pruebas.listacomprapresentacion.presentacion;

import DTOs.CompraDTO;
import DTOs.ProductoDTO;
import Exceptions.NegocioException;
import Subsistemas.IFiltroPorCompra;
import Subsistemas.FiltroPorCompra;
import Subsistemas.IGestorCompras;
import Subsistemas.GestorCompras;
import Subsistemas.IGestorProductos;
import Subsistemas.GestorProductos;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.itson.pruebas.listacomprapresentacion.validadores.Validadores;

/**
 * Panel que permite agregar un producto a una lista de compras.
 *
 * @author Víctor Encinas - 244821 , José Armenta - 247641 , José Huerta -
 * 245345.
 */
public class panelDatosProducto extends javax.swing.JPanel {

    private frmMenuInicio menuInicio;
    private CompraDTO compra;
    private ProductoDTO producto;
    private IGestorProductos gestorProductos;
    private IGestorCompras gestorCompras;
    private IFiltroPorCompra filtroCompra;
    private Boolean isUpdating;

    /**
     * Método constructor que nos permite crear el frame y además recibe el
     * valor del frame menu inicio y la compra. Además inicializa el valor del
     * frame principal y el cliente. También crea la instancia GestorProductos,
     * GestorCompras y FiltroPorCompra.
     *
     * @param menuInicio Frame de menu inicio donde se pintará este panel.
     * @param compra Compra actual.
     */
    public panelDatosProducto(frmMenuInicio menuInicio, CompraDTO compra) {
        this.menuInicio = menuInicio;
        this.compra = compra;
        this.isUpdating = false;
        gestorProductos = new GestorProductos();
        gestorCompras = new GestorCompras();
        filtroCompra = new FiltroPorCompra();
        initComponents();
    }

    /**
     * Método constructor que nos permite crear el frame cuando se quiere
     * actualizar un producto y además recibe el valor del frame menu inicio, la
     * compra, el producto a actualizar y un booleano que permite saber si se
     * desea actualizar. Además inicializa el valor del frame principal y el
     * cliente. También crea la instancia GestorProductos, GestorCompras y
     * FiltroPorCompra.
     *
     * @param menuInicio Frame de menu inicio donde se pintará este panel.
     * @param compra Compra actual.
     * @param producto
     * @param isUpdating
     */
    public panelDatosProducto(frmMenuInicio menuInicio, CompraDTO compra, ProductoDTO producto, Boolean isUpdating) {
        this.menuInicio = menuInicio;
        this.compra = compra;
        this.producto = producto;
        this.isUpdating = isUpdating;
        gestorProductos = new GestorProductos();
        gestorCompras = new GestorCompras();
        filtroCompra = new FiltroPorCompra();
        initComponents();
        cargarDatos();
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
        jLabel2 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtCategoría = new javax.swing.JTextField();
        btnAgregarProducto = new javax.swing.JButton();
        btnRegresar = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 185));

        jPanel1.setBackground(new java.awt.Color(54, 7, 69));

        jLabel1.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Datos del producto");

        jLabel2.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Cantidad:");

        txtNombre.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 18)); // NOI18N

        jLabel3.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Nombre:");

        txtCantidad.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 18)); // NOI18N

        jLabel4.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Categoría:");

        txtCategoría.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 18)); // NOI18N

        btnAgregarProducto.setBackground(new java.awt.Color(254, 194, 58));
        btnAgregarProducto.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
        btnAgregarProducto.setText("Aceptar");
        btnAgregarProducto.setFocusable(false);
        btnAgregarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarProductoActionPerformed(evt);
            }
        });

        btnRegresar.setBackground(new java.awt.Color(254, 194, 58));
        btnRegresar.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
        btnRegresar.setText("Regresar");
        btnRegresar.setFocusable(false);
        btnRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegresarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(236, 236, 236)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(77, 77, 77)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 461, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(btnRegresar))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnAgregarProducto)
                                    .addComponent(txtCategoría))))))
                .addContainerGap(80, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel1)
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 74, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCategoría, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(51, 51, 51))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(156, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(153, 153, 153))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(105, 105, 105)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(107, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Método que nos permite agregar o actualizar un producto de la lista de
     * compras.
     *
     * @param evt Evento al hacer clic en el botón.
     */
    private void btnAgregarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarProductoActionPerformed
        Validadores validador = new Validadores();
        if (validarCamposLlenos()) {
            if (validador.validarCantidad(txtCantidad.getText())) {

                try {
                    if (isUpdating) {
                        try {
                            actualizarDatos();
                        } catch (NegocioException ex) {
                            Logger.getLogger(panelDatosProducto.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        try {
                            guardarProducto();
                        } catch (NegocioException ex) {
                            Logger.getLogger(panelDatosProducto.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                    panelListaProductos agregarProducto = new panelListaProductos(menuInicio, compra);
                    menuInicio.mostrarPanel(agregarProducto);
                } catch (NegocioException ex) {
                    Logger.getLogger(panelDatosProducto.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Ingrese un valor numérico válido en la cantidad.", "Cantidad inválida", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnAgregarProductoActionPerformed

    /**
     * Método que nos permite regresarnos al panel anterior de lista de
     * productos.
     *
     * @param evt Evento al hacer clic en el botón.
     */
    private void btnRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresarActionPerformed
        try {
            panelListaProductos agregarProducto = new panelListaProductos(menuInicio, compra);
            menuInicio.mostrarPanel(agregarProducto);
        } catch (NegocioException ex) {
            Logger.getLogger(panelDatosProducto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnRegresarActionPerformed

    /**
     * Permite agregar un producto a una lista de compras
     *
     * @throws NegocioException Se lanza en caso de que no pueda ser agregado.
     */
    private void guardarProducto() throws NegocioException {
        String nombre = txtNombre.getText();
        Double cantidad = Double.valueOf(txtCantidad.getText());
        String categoria = txtCategoría.getText();

        if (existeProductoEnCompra(nombre)) {
            JOptionPane.showMessageDialog(this, "Ya existe un producto con ese nombre en la compra.", "Producto existente", JOptionPane.WARNING_MESSAGE);
        } else {
            ProductoDTO producto = new ProductoDTO(nombre, categoria, false, compra, cantidad);
            gestorProductos.agregarProducto(producto);
        }

    }

    /**
     * Permite actualizar un producto de una lista de compras.
     */
    private void actualizarDatos() throws NegocioException {
        String nombre = txtNombre.getText();
        Double cantidad = Double.valueOf(txtCantidad.getText());
        String categoria = txtCategoría.getText();
        if (nombre.equals(producto.getNombre())) {
            ProductoDTO productoAct = new ProductoDTO(nombre, categoria, producto.isComprado(), compra, cantidad);
            productoAct.setId(producto.getId());
            gestorProductos.actualizarProducto(productoAct);
        } else if (existeProductoEnCompra(nombre)) {
            JOptionPane.showMessageDialog(this, "Ya existe un producto con ese nombre en la compra.", "Producto existente", JOptionPane.WARNING_MESSAGE);
        } else {
            ProductoDTO productoAct = new ProductoDTO(nombre, categoria, producto.isComprado(), compra, cantidad);
            productoAct.setId(producto.getId());
            gestorProductos.actualizarProducto(productoAct);
        }
    }

    /**
     * Permite cargar los datos del producto para que puedan ser mostrados.
     */
    private void cargarDatos() {
        if (isUpdating) {
            String nombre = producto.getNombre();
            txtNombre.setText(nombre);
            String cantidad = producto.getCantidad().toString();
            txtCantidad.setText(cantidad);
            String categoria = producto.getCategoria();
            txtCategoría.setText(categoria);
        }
    }

    /**
     * Permite validar si los espacios donde se agregan los productos no están
     * vacíos.
     *
     * @return False si esta vacío, true en caso contrario.
     */
    private boolean validarCamposLlenos() {
        if (txtNombre.getText().isBlank() || txtCantidad.getText().isBlank() || txtCategoría.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Permite validar que el ombre del prodcuto no este repetido en la lista de
     * compras.
     *
     * @param nombre NOmbre del producto.
     * @return True en caso de que si aparezca, false en caso contrario.
     */
    public boolean existeProductoEnCompra(String nombre) throws NegocioException {
        List<ProductoDTO> productos = filtroCompra.obtenerProductosFiltrarPorCompra(this.compra.getId());
        for (ProductoDTO producto : productos) {
            if (producto.getNombre().equalsIgnoreCase(nombre)) {
                return true;
            }
        }
        return false;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarProducto;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtCategoría;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
