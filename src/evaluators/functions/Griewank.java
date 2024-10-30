package evaluators.functions;

import common.Config;
import evaluators.BaseEvaluator;
import evaluators.OptimizationEvaluationFunction;

import java.util.Arrays;

public class Griewank extends BaseEvaluator {

    public Griewank(int dimensions) {
        super(dimensions, "griewank");
    }

    @Override
    public double evaluate(double[] values) {
        double sum = 0;
        double prod = 0;

        for (int i = 0; i < this.getDimensions(); i++) {
            sum += Math.pow(values[i], 2) / 4000;
            prod *= Math.cos(values[i] / Math.sqrt(i + 1));
        }

        return sum - prod + 1;
    }
}
