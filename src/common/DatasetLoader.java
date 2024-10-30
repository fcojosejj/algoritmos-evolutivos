package common;

import common.exceptions.UnknownPropertyException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Clase singleton que sirve para acceder al dataset desde cualquier parte de la aplicación, sin necesidad de volver
 * a cargar todos los datos.
 */
public class DatasetLoader {
    private static DatasetLoader instance;
    private final ArrayList<DatasetItem> dataset;

    /**
     * Constructor privado. Carga los datos que haya almacenados en el fichero del dataset indicado en los ajustes,
     * y los almacena en un ArrayList que contiene DatasetItem.
     */
    private DatasetLoader() {
        this.dataset = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(Config.getSetting("global.datasetFilePath")))) {
            br.readLine(); // To skip the first line
            String line;
            while ((line = br.readLine()) != null) {
                String[] arrayLine = line.split(",");
                this.dataset.add(new DatasetItem(Double.parseDouble(arrayLine[0]), Double.parseDouble(arrayLine[1]), Double.parseDouble(arrayLine[2]), Double.parseDouble(arrayLine[3]), Double.parseDouble(arrayLine[4])));
            }
        } catch (IOException | UnknownPropertyException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Devuelve los datos contenidos en el dataset.
     *
     * @return
     */
    public static ArrayList<DatasetItem> getDataset() {
        return getInstance().dataset;
    }

    /**
     * Genera o devuelve la instanca del singleton.
     *
     * @return
     */
    private static DatasetLoader getInstance() {
        if (instance == null) {
            instance = new DatasetLoader();
        }
        return instance;
    }
}
