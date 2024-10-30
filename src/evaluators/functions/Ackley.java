package evaluators.functions;

import evaluators.BaseEvaluator;

public class Ackley extends BaseEvaluator {
    private final double a = 20;
    private final double b = 0.2;
    private final double c = 2 * Math.PI;

    public Ackley(int dimensions) {
        super(dimensions, "ackley");
    }

    @Override
    public double evaluate(double[] values) {
        double score;
        double sum1 = 0.0;
        double sum2 = 0.0;

        for (int i = 0; i < this.getDimensions(); i++) {
            sum1 += Math.pow(values[i], 2);
            sum2 += Math.cos(this.c * values[i]);
        }

        score = -this.a * Math.exp(-this.b * Math.sqrt((1.0 / this.getDimensions()) * sum1)) -
                Math.exp((1.0 / this.getDimensions()) * sum2) + this.a + Math.exp(1);

        return score;
    }
}
