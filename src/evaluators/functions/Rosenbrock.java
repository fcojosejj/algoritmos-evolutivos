package evaluators.functions;

import evaluators.BaseEvaluator;

public class Rosenbrock extends BaseEvaluator {
    public Rosenbrock(int dimensions) {
        super(dimensions, "rosenbrock");
    }

    @Override
    public double evaluate(double[] values) {
        double sum = 0;

        for (int i = 0; i < this.getDimensions() - 1; i++) {
            sum += 100 * (values[i + 1] - Math.pow(values[i], 2)) + Math.pow((values[i] - 1), 2);
        }

        return sum;
    }
}
