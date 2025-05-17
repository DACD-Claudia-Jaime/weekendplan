package com.proyecto1.businessunit;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BusinessUnitGUI gui = new BusinessUnitGUI();
            gui.setVisible(true);
        });
    }
}