package algorithms;

import common.Config;
import common.Individual;
import common.exceptions.UnknownPropertyException;
import evaluators.BaseEvaluator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AlgDE_Clase03_Grupo04 extends BaseAlgorithm {
    protected final double recombinationFactor;

    /**
     * Crea una nueva instancia del algoritmo evolutivo diferencial.
     *
     * @param seed                La semilla para inicializar el generador de números aleatorios.
     * @param evaluator           La función de evaluación a utilizar.
     * @param dimensions          El número de dimensiones de los individuos en la población.
     * @param searchIntervalStart El límite inferior del intervalo de búsqueda.
     * @param searchIntervalEnd   El límite superior del intervalo de búsqueda.
     * @throws UnknownPropertyException Si se encuentra una propiedad desconocida en el archivo de configuración.
     */
    public AlgDE_Clase03_Grupo04(long seed, BaseEvaluator evaluator, int dimensions, double searchIntervalStart, double searchIntervalEnd) throws UnknownPropertyException {
        super(seed, evaluator, dimensions, searchIntervalStart, searchIntervalEnd);
        this.recombinationFactor = Double.parseDouble(Config.getSetting("ed.recombinationFactor"));
    }

    /**
     * Configura y devuelve un objeto logger para registrar información del algoritmo.
     *
     * @return Un objeto logger configurado.
     */
    @Override
    protected Logger getConfiguredLogger() {
        return Config.getCustomLogger("logs/ed/" + evaluator.getClass().getSimpleName() + "_population" + populationSize + "_d" + dimensions + "_s" + seed + ".txt", false);
    }

    /**
     * Ejecuta el algoritmo evolutivo diferencial y devuelve la mejor solución encontrada.
     *
     * @return La mejor solución encontrada por el algoritmo.
     */
    @Override
    protected double[] execute() {
        int evaluations = 0, generations = 0, posObj = 0;
        initializePopulation();
        evaluatePopulation();
        do {
            generations++;

            this.logger.info("Start of generation: " + generations + " - Number of evaluations: " + evaluations);
            this.logger.info("Best individual in current population: " + Arrays.toString(population.get(findBestIndividualIndex()).getValues()) + " evaluated with: " + Math.abs(evaluator.getGlobalOptimum() - evaluator.evaluate(population.get(findBestIndividualIndex()).getValues())));

            newPopulation = new ArrayList<>();
            for (int i = 0; i < this.populationSize; i++) {
                int[] randoms;
                do {
                    randoms = generateIndex();
                } while (i == randoms[0] || i == randoms[1]);
                do {
                    posObj = tournamentK3();
                } while (posObj == randoms[0] || posObj == randoms[1] || posObj == i);
                double[] newIndividualValues = newIndividual(randoms, posObj, i);
                replacement(newIndividualValues, i);
                evaluations++;
            }
            population = newPopulation;
        } while (evaluations < maxEvaluations);
        this.logger.info("Evaluations > " + maxEvaluations + " - Algorithm stopped. Best individual values: " + Arrays.toString(population.get(findBestIndividualIndex()).getValues()));
        return population.get(findBestIndividualIndex()).getValues();
    }

    /**
     * Realiza un torneo K3 para seleccionar el mejor individuo entre tres individuos aleatorios de la población.
     *
     * @return La posición del mejor individuo seleccionado en la población.
     */
    protected int tournamentK3() {
        int[] pairIndex = generateIndex();
        int index3 = 0;
        do {
            index3 = random.nextInt(populationSize);
        } while (index3 == pairIndex[0] || index3 == pairIndex[1]);

        if (population.get(pairIndex[0]).getEvaluation() < population.get(pairIndex[1]).getEvaluation()) {
            if (population.get(pairIndex[0]).getEvaluation() < population.get(index3).getEvaluation()) {
                return pairIndex[0];
            } else {
                return index3;
            }
        } else {
            if (population.get(pairIndex[1]).getEvaluation() < population.get(index3).getEvaluation()) {
                return pairIndex[1];
            } else {
                return index3;
            }
        }
    }

    /**
     * Genera dos índices aleatorios para seleccionar dos individuos de la población.
     *
     * @return Un array con los índices de los dos individuos seleccionados.
     */
    protected int[] generateIndex() {
        int index1, index2;
        index1 = random.nextInt(this.populationSize);
        do {
            index2 = random.nextInt(this.populationSize);
        } while (index1 == index2);
        return new int[]{index1, index2};
    }

    /**
     * Genera un nuevo individuo a partir de un padre y dos individuos seleccionados aleatoriamente.
     *
     * @param randoms   Los índices de los dos individuos seleccionados aleatoriamente.
     * @param posObj    La posición del individuo objetivo en la población.
     * @param posFather La posición del padre en la población.
     * @return Un array que representa al nuevo individuo generado.
     */
    protected double[] newIndividual(int[] randoms, int posObj, int posFather) {
        double[] newIndividualValues = new double[this.dimensions];
        for (int j = 0; j < newIndividualValues.length; j++) {
            if (random.nextDouble() <= this.recombinationFactor) {
                newIndividualValues[j] = population.get(posFather).getValues()[j] + random.nextDouble() * (population.get(randoms[0]).getValues()[j] - population.get(randoms[1]).getValues()[j]);
            } else {
                newIndividualValues[j] = population.get(posObj).getValues()[j];
            }
        }
        this.logger.log(Level.FINE, "A new individual was generated with values: " + Arrays.toString(newIndividualValues));
        return newIndividualValues;
    }

    /**
     * Reemplaza al individuo en una posición específica de la población por el nuevo individuo generado, si es mejor.
     *
     * @param newIndividualValues Los valores del nuevo individuo generado.
     * @param i                   La posición del individuo a reemplazar en la población.
     */
    protected void replacement(double[] newIndividualValues, int i) {
        if (Math.abs(evaluator.getGlobalOptimum() - evaluator.evaluate(newIndividualValues)) < Math.abs(evaluator.getGlobalOptimum() - evaluator.evaluate(population.get(i).getValues()))) {
            newPopulation.add(new Individual(newIndividualValues, Math.abs(evaluator.getGlobalOptimum() - evaluator.evaluate(newIndividualValues)), false, i, -1));
            this.logger.log(Level.FINE, "Replacing individual at index " + i + " with new individual (values: " + Arrays.toString(newIndividualValues));
        } else {
            this.logger.log(Level.FINE, "The individual at index " + i + " was NOT replaced.");
            newPopulation.add(population.get(i));
        }
    }
}