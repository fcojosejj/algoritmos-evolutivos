package evaluators;

public interface OptimizationEvaluationFunction {

    /**
     * Evalúa la función de optimización en el punto especificado.
     *
     * @param values valores para cada una de las dimensiones del punto a evaluar.
     * @return el resultado de evaluar la función en el punto especificado.
     */
    double evaluate(double[] values);

    /**
     * Obtiene el óptimo global de la función de optimización.
     *
     * @return el óptimo global
     */
    double getGlobalOptimum();

    /**
     * Obtiene el intervalo de valores válidos para cada una de las dimensiones de la función de optimización.
     *
     * @return el intervalo de valores válidos
     */
    double[] getInterval();
}
