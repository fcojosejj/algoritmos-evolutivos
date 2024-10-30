package evaluators.functions;

import evaluators.BaseEvaluator;
import evaluators.OptimizationEvaluationFunction;

public class Perm0db extends BaseEvaluator {

    private final double beta = 10;

    public Perm0db(int dimensions) {
        super(dimensions, "perm0db");
    }

    @Override
    public double evaluate(double[] values) {
        double sum1 = 0, sum2 = 0;

        for (int i = 0; i < this.getDimensions(); i++) {
            for (int j = 0; j < this.getDimensions(); j++) {
                sum2 += (j + this.beta) * (Math.pow(values[j], i) - ( 1.0/Math.pow(j+1, i) )); //j+1 porque si no cuando i>0 y j=0, dividimos 1.0/0 y da infinito.
            }
            sum1 += Math.pow(sum2, 2);
            sum2 = 0;
        }

        return sum1;
    }
}
