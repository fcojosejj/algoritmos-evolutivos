package evaluators.functions;

import evaluators.BaseEvaluator;
import evaluators.OptimizationEvaluationFunction;

public class Michalewicz extends BaseEvaluator {

    private final int m = 10;

    public Michalewicz(int dimensions) {
        super(dimensions, "michalewicz");
    }

    @Override
    public double evaluate(double[] values) {
        double sum = 0;

        for (int i = 0; i < this.getDimensions(); i++) {
            sum += Math.sin(values[i]) * Math.pow(Math.sin(i * Math.pow(values[i],2)/Math.PI), 2 * this.m);
        }

        return -sum;
    }
}
