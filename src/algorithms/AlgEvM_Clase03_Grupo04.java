package algorithms;

import common.Config;
import common.Individual;
import common.exceptions.UnknownPropertyException;
import evaluators.BaseEvaluator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

public class AlgEvM_Clase03_Grupo04 extends BaseAlgorithm {
    protected final double crossingProb;
    protected final double mutationProb;

    /**
     * Crea una nueva instancia del algoritmo EvM.
     *
     * @param seed                semilla para el generador de números aleatorios utilizado por el algoritmo.
     * @param evaluator           evaluador que se usará para evaluar la función de optimización.
     * @param dimensions          número de dimensiones de la función de optimización.
     * @param searchIntervalStart límite inferior del intervalo de búsqueda.
     * @param searchIntervalEnd   límite superior del intervalo de búsqueda.
     * @throws UnknownPropertyException si se produce un error al cargar las propiedades del algoritmo.
     */
    public AlgEvM_Clase03_Grupo04(long seed, BaseEvaluator evaluator, int dimensions, double searchIntervalStart, double searchIntervalEnd) throws UnknownPropertyException {
        super(seed, evaluator, dimensions, searchIntervalStart, searchIntervalEnd);
        this.crossingProb = Double.parseDouble(Config.getSetting("evm.crossingProb"));
        this.mutationProb = Double.parseDouble(Config.getSetting("evm.mutationProb"));
    }

    @Override
    protected Logger getConfiguredLogger() {
        return Config.getCustomLogger("logs/evm/" + evaluator.getClass().getSimpleName() + "_population" + populationSize + "_d" + dimensions + "_s" + seed + ".txt", false);
    }

    /**
     * Ejecuta el algoritmo EvM.
     *
     * @return la mejor solución encontrada.
     */
    @Override
    protected double[] execute() {
        int evaluations = 0, generations = 0;
        int eliteIndex;

        initializePopulation();
        evaluatePopulation();
        do {
            generations++;

            this.logger.info("Start of generation: " + generations + " - Number of evaluations: " + evaluations);

            newPopulation = new ArrayList<>();
            eliteIndex = findBestIndividualIndex();

            this.logger.info("Best individual in current population: " + Arrays.toString(population.get(eliteIndex).getValues()) + " evaluated with: " + Math.abs(evaluator.getGlobalOptimum() - evaluator.evaluate(population.get(eliteIndex).getValues())));
            for (int i = 0; i < this.populationSize; i++) {
                crossbreed();
                mutation(newPopulation.get(i));
                evaluateInidividual(newPopulation.get(i));
                evaluations++;
            }

            replacement(eliteIndex);
        } while (evaluations < maxEvaluations);
        this.logger.info("Evaluations > " + maxEvaluations + " - Algorithm stopped. Best individual values: " + Arrays.toString(population.get(findBestIndividualIndex()).getValues()));
        return population.get(findBestIndividualIndex()).getValues();
    }

    /**
     * Evalúa el individuo especificado.
     *
     * @param individual el individuo a evaluar
     */
    protected void evaluateInidividual(Individual individual) {
        individual.setEvaluation(Math.abs(evaluator.getGlobalOptimum() - evaluator.evaluate(individual.getValues())));
    }

    /**
     * Devuelve el índice del individuo mejor o peor de una población en función de su evaluación.
     *
     * @param index1   El índice del primer individuo a comparar.
     * @param index2   El índice del segundo individuo a comparar.
     * @param findBest Si es true, la función devolverá el índice del individuo con la mejor evaluación.
     *                 Si es false, la función devolverá el índice del individuo con la peor evaluación.
     * @return El índice del individuo mejor (si findBest es true) o peor (si findBest es false) en la población.
     */
    protected int tournamentK2(int index1, int index2, boolean findBest) {
        if (findBest) {
            // find the best individual
            if (population.get(index1).getEvaluation() < population.get(index2).getEvaluation()) {
                return index1;
            } else {
                return index2;
            }
        } else {
            // find the worst individual
            if (population.get(index1).getEvaluation() > population.get(index2).getEvaluation()) {
                return index1;
            } else {
                return index2;
            }
        }
    }

    /**
     * Realiza un torneo K2 entre cuatro individuos de la población y devuelve el índice del peor.
     *
     * @return El índice del peor individuo del torneo K2.
     */
    protected int tournamentK4() {
        int[] pairIndex1 = generateIndex();
        int[] pairIndex2;
        do {
            pairIndex2 = generateIndex();
        } while (pairIndex1 == pairIndex2);
        int worstIndex1 = tournamentK2(pairIndex1[0], pairIndex1[1], false);
        int worstIndex2 = tournamentK2(pairIndex2[0], pairIndex2[1], false);
        return tournamentK2(worstIndex1, worstIndex2, false);
    }

    /**
     * Cruza dos individuos de la población y agrega el hijo resultante a la nueva población.
     * Si no se produce el cruce, se agrega el mejor individuo de los dos padres a la nueva población.
     */
    protected void crossbreed() {
        int[] parent1Indexes = generateIndex();
        int indexf1 = tournamentK2(parent1Indexes[0], parent1Indexes[1], true);

        int[] parent2Indexes = generateIndex();
        int indexf2 = tournamentK2(parent2Indexes[0], parent2Indexes[1], true);

        double[] son = new double[this.dimensions];
        for (int i = 0; i < this.dimensions; i++) {
            son[i] = (population.get(indexf1).getValues()[i] + population.get(indexf2).getValues()[i]) / 2;
        }

        if (random.nextDouble() < this.crossingProb) {
            newPopulation.add(new Individual(son, Math.abs(evaluator.getGlobalOptimum() - evaluator.evaluate(son)), false, indexf1, indexf2));
            this.logger.info("Crossbreed was executed. Adding new son to the population.");
        } else {
            this.logger.info("Crossbreed was NOT executed. Adding father1 to the population.");
            newPopulation.add(population.get(indexf1));
        }
    }

    /**
     * Realiza la mutación de un individuo, si se cumple una probabilidad dada.
     *
     * @param son El individuo que se va a mutar.
     */
    protected void mutation(Individual son) {
        this.logger.info("Mutating individual with values: " + Arrays.toString(son.getValues()));
        for (int i = 0; i < son.getValues().length; i++) {
            if (random.nextDouble() <= this.mutationProb) {
                son.getValues()[i] = this.searchIntervalStart + (this.searchIntervalEnd - this.searchIntervalStart) * random.nextDouble();
            }
        }

        this.logger.info("Values after mutation: " + Arrays.toString(son.getValues()));
    }

    /**
     * Genera dos índices aleatorios para seleccionar dos individuos de la población.
     *
     * @return un array de dos enteros con los índices de los individuos seleccionados.
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
     * Verifica si el individuo élite de la población anterior sobrevivió a la selección.
     *
     * @param pos La posición del individuo élite en la población anterior.
     * @return True si el individuo élite sobrevivió a la selección, false en caso contrario.
     */
    protected boolean eliteSurvived(int pos) {
        for (int i = 0; i < populationSize; i++) {
            if (population.get(pos) == newPopulation.get(i)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Realiza el reemplazo de la población actual con la nueva población generada,
     * agregando el individuo élite de la población anterior si no sobrevivió a la selección.
     *
     * @param posElite La posición del individuo élite en la población anterior.
     */
    protected void replacement(int posElite) {
        this.logger.info("Performing population replacement.");
        if (!eliteSurvived(posElite)) {
            this.logger.info("Elite did not survive. Performing K4 tournament and removing the worst.");
            int worstIndividualIndex = tournamentK4();
            newPopulation.remove(worstIndividualIndex);
            newPopulation.add(population.get(posElite));
        }
        population = newPopulation;
    }
}
