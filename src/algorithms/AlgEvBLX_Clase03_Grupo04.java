package algorithms;

import common.Config;
import common.Individual;
import common.exceptions.UnknownPropertyException;
import evaluators.BaseEvaluator;

import java.util.Arrays;
import java.util.logging.Logger;

public class AlgEvBLX_Clase03_Grupo04 extends AlgEvM_Clase03_Grupo04 {
    protected final double alpha;

    public AlgEvBLX_Clase03_Grupo04(long seed, BaseEvaluator evaluator, int dimensions, double searchIntervalStart, double searchIntervalEnd) throws UnknownPropertyException {
        super(seed, evaluator, dimensions, searchIntervalStart, searchIntervalEnd);
        this.alpha = Double.parseDouble(Config.getSetting("evblx.alpha"));
    }

    @Override
    protected Logger getConfiguredLogger() {
        return Config.getCustomLogger("logs/evblx/" + evaluator.getClass().getSimpleName() + "_population" + populationSize + "_d" + dimensions + "_s" + seed + ".txt", false);
    }

    /**
     * Realiza el cruce BLX-alfa entre dos individuos de la población y agrega el hijo resultante a la nueva población.
     * Si no se produce el cruce, se agrega el padre 1 a la nueva población.
     */
    @Override
    protected void crossbreed() {
        int indexf1 = 0, indexf2 = 0;
        int[] parent1Indexes = generateIndex();
        indexf1 = tournamentK2(parent1Indexes[0], parent1Indexes[1], true);
        int[] parent2Indexes = generateIndex();
        indexf2 = tournamentK2(parent2Indexes[0], parent2Indexes[1], true);

        double min = 0, max = 0;
        double[] son = new double[this.dimensions];
        for (int i = 0; i < this.dimensions; i++) {
            if (population.get(indexf1).getValues()[i] < population.get(indexf2).getValues()[i]) {
                min = population.get(indexf1).getValues()[i];
                max = population.get(indexf2).getValues()[i];
            } else {
                min = population.get(indexf2).getValues()[i];
                max = population.get(indexf1).getValues()[i];
            }
            son[i] = (min - (max - min) * this.alpha) + ((max + (max - min) * this.alpha) - (min - (max - min) * this.alpha)) * random.nextDouble();
        }

        if (random.nextDouble() < this.crossingProb) {
            newPopulation.add(new Individual(son, Math.abs(evaluator.getGlobalOptimum() - evaluator.evaluate(son)), false, indexf1, indexf2));
            this.logger.info("Crossbreed was executed. Adding new son to the population. Values: " + Arrays.toString(son));
        } else {
            this.logger.info("Crossbreed was NOT executed. Adding father1 to the population..");
            newPopulation.add(new Individual(population.get(indexf1)));
        }
    }

}
