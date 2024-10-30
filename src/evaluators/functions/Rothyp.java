package evaluators.functions;

import evaluators.BaseEvaluator;
import evaluators.OptimizationEvaluationFunction;

public class Rothyp extends BaseEvaluator {
    public Rothyp(int dimensions) {
        super(dimensions, "rothyp");
    }

    @Override
    public double evaluate(double[] values) {
        double sum = 0;

        for (int i = 0; i < this.getDimensions(); i++) {
            for (int j = 0; j < i; j++) {
                sum += Math.pow(values[j], 2);
            }
        }

        return sum;
    }
}
