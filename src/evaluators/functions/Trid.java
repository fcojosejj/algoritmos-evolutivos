package evaluators.functions;

import evaluators.BaseEvaluator;
import evaluators.OptimizationEvaluationFunction;

public class Trid extends BaseEvaluator {
    public Trid(int dimensions) {
        super(dimensions, "trid");
    }

    @Override
    public double evaluate(double[] values) {
        double sum1 = 0;
        double sum2 = 0;

        for (int i = 0; i < this.getDimensions(); i++) {
            sum1 += Math.pow((values[i] - 1), 2);
        }

        for (int i = 1; i < this.getDimensions(); i++) {
            sum2 += values[i] * values[i - 1];
        }

        return sum1 - sum2;
    }

    @Override
    public double getGlobalOptimum() {
        return -200;
    }

}
