package com.proyecto1.businessunit;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        try {
            DatamartManager dm = new DatamartManager();
            SwingUtilities.invokeLater(() -> {
                BusinessUnitGUI gui = new BusinessUnitGUI(dm);
                gui.setVisible(true);
            });
            new Thread(() -> {
                try {
                    RealTimeEventListener listener = new RealTimeEventListener(dm);
                    listener.startListening();
                } catch (Exception e) {
                    System.err.println("Error al iniciar el listener en tiempo real:");
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            System.err.println("Error general al iniciar la aplicación:");
            e.printStackTrace();
        }
    }
}