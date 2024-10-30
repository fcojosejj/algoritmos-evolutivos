package common;

import java.util.Arrays;

/**
 * Clase que representa un individuo en un algoritmo genético.
 */
public class Individual {

    /**
     * Valores de cada uno de los genes de este individuo.
     */
    private final double[] values;
    /**
     * Índice del primer padre de este individuo.
     */
    private final int indexf1;
    /**
     * Índice del segundo padre de este individuo.
     */
    private final int indexf2;
    /**
     * Valor de la evaluación de este individuo.
     */
    private double evaluation;
    /**
     * Indica si este individuo es un "élite" en la población.
     */
    private boolean isElite;

    /**
     * Crea una nueva instancia de un individuo con los valores especificados.
     *
     * @param values     valores de los genes de este individuo.
     * @param evaluation valor de la evaluación de este individuo.
     * @param isElite    indica si este individuo es un "élite" en la población.
     * @param indexf1    índice del primer padre de este individuo.
     * @param indexf2    índice del segundo padre de este individuo.
     */
    public Individual(double[] values, double evaluation, boolean isElite, int indexf1, int indexf2) {
        this.values = values;
        this.evaluation = evaluation;
        this.isElite = isElite;
        this.indexf1 = indexf1;
        this.indexf2 = indexf2;
    }

    /**
     * Crea una nueva instancia de un individuo a partir de otro individuo existente.
     *
     * @param source el individuo existente desde el cual se creará la nueva instancia.
     */
    public Individual(Individual source) {
        this.values = new double[source.values.length];
        System.arraycopy(source.values, 0, this.values, 0, source.values.length);
        this.evaluation = source.evaluation;
        this.isElite = source.isElite;
        this.indexf1 = source.indexf1;
        this.indexf2 = source.indexf2;
    }

    /**
     * Obtiene los valores de los genes de este individuo.
     *
     * @return los valores de los genes de este individuo.
     */
    public double[] getValues() {
        return values;
    }

    /**
     * Obtiene el valor de la evaluación de este individuo.
     *
     * @return el valor de la evaluación de este individiduo.
     */
    public double getEvaluation() {
        return evaluation;
    }

    /**
     * Establece el valor de la evaluación de este individuo.
     *
     * @param ev el nuevo valor de la evaluación de este individuo.
     */
    public void setEvaluation(double ev) {
        this.evaluation = ev;
    }

    /**
     * Indica si este individuo es un "élite" en la población.
     *
     * @return verdadero si este individuo es un "élite", falso en caso contrario.
     */
    public boolean isElite() {
        return isElite;
    }

    /**
     * Establece si este individuo es un "élite" en la población.
     *
     * @param elite el valor a establecer para la propiedad isElite.
     */
    public void setElite(boolean elite) {
        isElite = elite;
    }

    @Override
    public String toString() {
        return "Individual{" +
                "values=" + Arrays.toString(values) +
                ", indexf1=" + indexf1 +
                ", indexf2=" + indexf2 +
                ", evaluation=" + evaluation +
                ", isElite=" + isElite +
                '}';
    }
}
