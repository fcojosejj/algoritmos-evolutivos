package evaluators.functions;

import evaluators.BaseEvaluator;
import evaluators.OptimizationEvaluationFunction;

public class Schewefel extends BaseEvaluator {
    public Schewefel(int dimensions) {
        super(dimensions, "schewefel");
    }

    @Override
    public double evaluate(double[] values) {
        double sum = 0;
        for (int i = 0; i < this.getDimensions(); i++) {
            sum += values[i] * Math.sin(Math.sqrt(Math.abs(values[i])));
        }

        return 418.9829 * this.getDimensions() - sum;
    }
}
