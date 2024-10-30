package evaluators.functions;

import evaluators.BaseEvaluator;

import java.util.Arrays;

public class Dixonprice extends BaseEvaluator {

    public Dixonprice(int dimensions) {
        super(dimensions, "dixonprice");
    }

    @Override
    public double evaluate(double[] values) {
        double sum = 0;

        for (int i = 1; i < this.getDimensions(); i++) {
            sum += i * Math.pow((2 * Math.pow(values[i], 2) - values[i - 1]), 2);
        }

        return Math.pow((values[0] - 1), 2) + sum;
    }
}
