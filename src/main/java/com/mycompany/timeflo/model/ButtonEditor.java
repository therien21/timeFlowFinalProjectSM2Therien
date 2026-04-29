package com.mycompany.timeflo.model;

import java.awt.Component;
import java.awt.event.ActionListener;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;

public class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private int clickedRow;
    private ActionListener listener;

    public ButtonEditor(ActionListener listener) {
        super(new JCheckBox());
        this.listener = listener;
        button = new JButton("AI ✨");
        button.setOpaque(true);
        button.addActionListener(e -> {
            fireEditingStopped();
            listener.actionPerformed(
                new java.awt.event.ActionEvent(button,
                    java.awt.event.ActionEvent.ACTION_PERFORMED,
                    String.valueOf(clickedRow))
            );
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        clickedRow = row;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return "AI ✨";
    }
}