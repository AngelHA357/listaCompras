/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.pruebas.listacomprapresentacion.modelotabla;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CustomTableModel extends DefaultTableModel {

    public CustomTableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if (column == 3) {
            Boolean value = (Boolean) getValueAt(row, column);
            return !Boolean.TRUE.equals(value);
        }
        return super.isCellEditable(row, column);
    }
}
