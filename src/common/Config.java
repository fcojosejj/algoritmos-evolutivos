package common;

import common.exceptions.UnknownPropertyException;
import evaluators.BaseEvaluator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Clase de configuración. Ofrece servicio a las demás clases para obtener ajustes.
 */
public class Config {
    private final static String CONFIG_FILE = "global_config.properties";
    private final static String LOG_FILE = "output.log";

    private static Config instance = null;
    private final Logger logger;
    private Properties properties = null;

    /**
     * Constructor privado. Genera la primera instancia para el singleton.
     */
    private Config() {
        // Setting up logger
        logger = Logger.getLogger("PR1");
        logger.setLevel(Level.ALL);
        try {
            FileHandler fileHandler = new FileHandler(LOG_FILE, true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "unable to configure logger", e);
        }

        // Setting up the properties
        try (InputStream inputStream = Files.newInputStream(Paths.get(CONFIG_FILE))) {
            properties = new Properties();
            properties.load(inputStream);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "unable to load the glboal properties file", e);
        }
    }

    /**
     * Devuelve la instancia del singleton, y si no hubiera ninguna, la genera
     * @return instancia del singleton
     */
    private static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    /**
     * Permite obtener los valores de los ajustes del archivo de configuración global.
     * @param key clave del ajuste
     * @return valor del ajuste
     * @throws UnknownPropertyException si la clave no está definida
     */
    public static String getSetting(String key) throws UnknownPropertyException {
        String property = getInstance().properties.getProperty(key);
        if (property == null) {
            throw new UnknownPropertyException("property " + key + " does not exist.");
        }
        return property;
    }

    /**
     * Obtiene el nivel de logging del fichero de configuración global
     * @return el nivel del logger
     */
    private static String getLoggerLevel() {
        return getInstance().properties.getProperty("global.loggerLevel");
    }

    /**
     * Obtiene el logger global de la aplicación
     * @return instancia del logger global del programa
     */
    public static Logger getLogger() {
        return getInstance().logger;
    }

    /**
     * Genera una nueva instancia de logger para una ruta específica.
     * @param filePath ruta donde se almacenará el fichero del logger
     * @param append true si se quiere añadir contenido a lo que hubiera en el fichero, false si se descarta el contenido previo
     * @return el nuevo logger
     */
    public static Logger getCustomLogger(String filePath, boolean append) {
        Logger customLogger = Logger.getAnonymousLogger();
        customLogger.setLevel(Level.parse(Config.getLoggerLevel()));
        try {
            FileHandler fileHandler = new FileHandler(filePath, append);
            fileHandler.setFormatter(new SimpleFormatter());
            customLogger.addHandler(fileHandler);
        } catch (IOException e) {
            Config.getLogger().log(Level.SEVERE, "unable to configure logger", e);
        }
        return customLogger;
    }

    /**
     * Devuelve una instancia de una función de evaluación.
     * @param evaluatorName nombre de la función de evaluación
     * @return la función de evaluación
     */
    public static BaseEvaluator getEvaluationFunction(String evaluatorName) throws InvocationTargetException, InstantiationException, IllegalAccessException, UnknownPropertyException, ClassNotFoundException, NoSuchMethodException {
        BaseEvaluator optimizationEvaluationFunction;

        String optimizationFunctionClassName = Utils.capitalizeFirstLetter(evaluatorName);
        int dimensions = Integer.parseInt(Config.getSetting("global.dimensions"));
        if (evaluatorName.equalsIgnoreCase("mape") || evaluatorName.equalsIgnoreCase("rmse")) {
            dimensions = Integer.parseInt(Config.getSetting("global.realDatasetDimensions"));
        }

        Class<?> otf = Class.forName("evaluators.functions." + optimizationFunctionClassName);
        Constructor<?> constructor = otf.getConstructor(int.class);
        optimizationEvaluationFunction = (BaseEvaluator) constructor.newInstance(dimensions);
        return optimizationEvaluationFunction;
    }

    /**
     * Método que permite parsear un doble a partir de un string, reconociendo el número pi
     * @param value cadena de texto que es un número doble
     * @return el valor convertido a tipo doble
     */
    public static double parseDouble(String value) {
        double parsedVal;
        if (value.equalsIgnoreCase("pi")) {
            parsedVal = Math.PI;
        } else if (value.equalsIgnoreCase("2pi")) {
            parsedVal = 2 * Math.PI;
        } else if (value.equalsIgnoreCase("3pi")) {
            parsedVal = 3 * Math.PI;
        } else {
            parsedVal = Double.parseDouble(value);
        }
        return parsedVal;
    }
}
