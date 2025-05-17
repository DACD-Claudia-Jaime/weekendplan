package com.proyecto1.businessunit;

public class DataImporterRunner {
    public static void main(String[] args) {
        try {
            DatamartManager dm = new DatamartManager();
            EventImporter importer = new EventImporter(dm);
            importer.importSocialEvents();
            importer.importFlights();
            dm.close();
            System.out.println("Importación completada.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}