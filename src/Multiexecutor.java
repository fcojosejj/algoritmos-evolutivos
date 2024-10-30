import algorithms.AlgDE_Clase03_Grupo04;
import algorithms.AlgEvBLX_Clase03_Grupo04;
import algorithms.AlgEvM_Clase03_Grupo04;
import common.Config;
import common.Utils;
import evaluators.BaseEvaluator;
import algorithms.BaseAlgorithm;

import java.lang.reflect.Constructor;
import java.util.logging.Level;

public class Multiexecutor {
    private final long[] seeds;
    private final String[] evaluationFunctions;
    private final String alg;

    public Multiexecutor(long[] seeds, String[] evaluationFunctions, String alg) {
        this.seeds = seeds;
        this.evaluationFunctions = evaluationFunctions;
        this.alg = alg;
    }

    public void execute() {
        for (String evaluationFunction : evaluationFunctions) {
            System.out.println("\n" + Utils.capitalizeFirstLetter(evaluationFunction));
            System.out.println("Evaluation\t\t\tTime (ms)");
            for (Long seed : seeds) {
                try {
                    BaseEvaluator evaluator = Config.getEvaluationFunction(evaluationFunction);

                    long startTime = System.nanoTime();
                    Class<?> algorithm = Class.forName("algorithms." + alg);
                    Constructor<?> constructor = algorithm.getConstructor(long.class, BaseEvaluator.class, int.class, double.class, double.class);
                    BaseAlgorithm instance = (BaseAlgorithm) constructor.newInstance(seed, evaluator, evaluator.getDimensions(), evaluator.getInterval()[0], evaluator.getInterval()[1]);
                    long stopTime = System.nanoTime();

                    System.out.println(evaluator.evaluate(instance.getBestSolution()) + "\t" + (stopTime - startTime) / 1e6);
                } catch (Exception e) {
                    Config.getLogger().log(Level.SEVERE, "Ocurrió un error inesperado.", e);
                }
            }
        }
        System.out.println("\n");
    }
}
