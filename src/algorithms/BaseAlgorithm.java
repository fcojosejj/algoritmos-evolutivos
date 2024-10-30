package algorithms;

import common.Config;
import common.Individual;
import common.exceptions.UnknownPropertyException;
import evaluators.BaseEvaluator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class BaseAlgorithm {
    protected final long seed;
    protected final int populationSize;
    protected final BaseEvaluator evaluator;
    protected final int dimensions;
    protected final double searchIntervalStart;
    protected final double searchIntervalEnd;
    protected final int maxEvaluations;

    // Utility variables
    protected final Random random;
    protected final Logger logger;

    // Algorithm variables
    protected double[] bestSolution = null;
    protected ArrayList<Individual> population;
    protected ArrayList<Individual> newPopulation;

    protected BaseAlgorithm(long seed, BaseEvaluator evaluator, int dimensions, double searchIntervalStart, double searchIntervalEnd) throws UnknownPropertyException {
        this.seed = seed;
        this.populationSize = Integer.parseInt(Config.getSetting("global.populationSize"));
        this.evaluator = evaluator;
        this.dimensions = dimensions;
        this.searchIntervalStart = searchIntervalStart;
        this.searchIntervalEnd = searchIntervalEnd;
        this.maxEvaluations = Integer.parseInt(Config.getSetting("global.maxEvaluations"));
        this.random = new Random(seed);

        this.population = new ArrayList<>();
        this.newPopulation = new ArrayList<>();

        this.logger = getConfiguredLogger();
    }

    protected abstract Logger getConfiguredLogger();

    protected abstract double[] execute();

    /**
     * Devuelve la mejor solución. La calcula si no se había calculado previamente o devuelve la almacenada.
     *
     * @return la mejor solución encontrada
     */
    public double[] getBestSolution() {
        if (bestSolution == null) {
            this.bestSolution = this.execute();
            for (Handler h : logger.getHandlers()) {
                h.close();
            }
        }
        return bestSolution;
    }

    /**
     * Inicializa la población del algoritmo.
     */
    protected void initializePopulation() {
        this.logger.info("Initializing population...");
        for (int i = 0; i < this.populationSize; i++) {
            double[] newIndividualValues = new double[this.dimensions];
            for (int j = 0; j < newIndividualValues.length; j++) {
                newIndividualValues[j] = this.searchIntervalStart + (this.searchIntervalEnd - this.searchIntervalStart) * random.nextDouble();
            }
            this.logger.log(Level.FINE, "New individual generated with values: " + Arrays.toString(newIndividualValues));
            this.population.add(new Individual(newIndividualValues, 0, false, -1, -1));
        }
    }

    /**
     * Evalúa la población actual del algoritmo.
     */
    protected void evaluatePopulation() {
        this.logger.info("Evaluating population...");
        for (int i = 0; i < this.populationSize; i++) {
            population.get(i).setEvaluation(Math.abs(evaluator.getGlobalOptimum() - evaluator.evaluate(population.get(i).getValues())));
        }
    }

    /**
     * Devuelve la posición del individuo con mejor evaluación en la población y lo marca como élite.
     *
     * @return posición del individuo
     */
    protected int findBestIndividualIndex() {
        int bestIndex = 0;
        for (int i = 0; i < population.size(); i++) {
            if (population.get(i).getEvaluation() < population.get(bestIndex).getEvaluation()) {
                bestIndex = i;
            }
        }
        this.population.get(bestIndex).setElite(true);
        return bestIndex;
    }
}
