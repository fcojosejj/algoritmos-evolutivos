package evaluators.functions;

import evaluators.BaseEvaluator;
import evaluators.OptimizationEvaluationFunction;

public class Rastringin extends BaseEvaluator {

    public Rastringin(int dimensions) {
        super(dimensions, "rastringin");
    }

    @Override
    public double evaluate(double[] values) {
        double sum = 0;

        for (int i = 0; i < this.getDimensions(); i++) {
            sum += Math.pow(values[i], 2) - 10 * Math.cos(2 * Math.PI * values[i]);
        }

        return 10 * this.getDimensions() + sum;
    }
}
