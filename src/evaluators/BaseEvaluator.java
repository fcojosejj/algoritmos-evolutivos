package evaluators;

import common.Config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;

public abstract class BaseEvaluator implements OptimizationEvaluationFunction {

    /**
     * Número de dimensiones de la función de evaluación.
     */
    protected final int dimensions;

    /**
     * Inicio del intervalo para cada una de las dimensiones de la función de evaluación.
     */
    protected final double intervalStart;

    /**
     * Fin del intervalo para cada una de las dimensiones de la función de evaluación.
     */
    protected final double intervalEnd;

    /**
     * Óptimo global de la función de evaluación.
     */
    protected final double globalOptimum;

    /**
     * Propiedades de la función de evaluación.
     */
    protected final Properties properties;

    /**
     * Crea una nueva instancia de la clase BaseEvaluator con el número de dimensiones y el nombre de configuración
     * especificados.
     *
     * @param dimensions el número de dimensiones de la función de evaluación.
     * @param configName el nombre de la configuración de la función de evaluación.
     */
    protected BaseEvaluator(int dimensions, String configName) {
        properties = getEvaluatorProperties(configName);

        this.dimensions = dimensions;
        this.intervalStart = Config.parseDouble(this.properties.getProperty("intervalStart"));
        this.intervalEnd = Config.parseDouble(this.properties.getProperty("intervalEnd"));
        this.globalOptimum = Config.parseDouble(this.properties.getProperty("globalOptimum"));
    }

    /**
     * Carga las propiedades de la función de evaluación especificada.
     *
     * @param configName el nombre de la configuración de la función de evaluación.
     * @return las propiedades de la función de evaluación especificada.
     */
    private Properties getEvaluatorProperties(String configName) {
        Properties evaluatorProperties = null;
        try (InputStream inputStream = Files.newInputStream(Paths.get("evaluator." + configName + ".properties"))) {
            evaluatorProperties = new Properties();
            evaluatorProperties.load(inputStream);
        } catch (IOException e) {
            Config.getLogger().log(Level.SEVERE, "unable to load the evaluator " + configName + " properties file", e);
        }
        return evaluatorProperties;
    }

    /**
     * Obtiene el intervalo de valores válidos para cada una de las dimensiones de la función de evaluación.
     *
     * @return el intervalo de valores válidos para cada una de las dimensiones de la función de evaluación.
     */
    public double[] getInterval() {
        return new double[]{intervalStart, intervalEnd};
    }

    /**
     * Obtiene el número de dimensiones de la función de evaluación.
     *
     * @return el número de dimensiones de la función de evaluación.
     */
    public int getDimensions() {
        return dimensions;
    }

    /**
     * Obtiene el óptimo global de la función de evaluación.
     *
     * @return el óptimo global de la función de evaluación.
     */
    @Override
    public double getGlobalOptimum() {
        return globalOptimum;
    }
}
