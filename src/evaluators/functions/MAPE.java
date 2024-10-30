package evaluators.functions;

import common.DatasetItem;
import common.DatasetLoader;
import evaluators.BaseEvaluator;

import java.util.ArrayList;

public class MAPE extends BaseEvaluator {
    private final ArrayList<DatasetItem> dataset;
    private final double[] realValues;

    public MAPE(int dimensions) {
        super(dimensions, "mape");
        this.dataset = DatasetLoader.getDataset();

        realValues = new double[dataset.size()];
        for (int i = 0; i < realValues.length; i++) {
            realValues[i] = dataset.get(i).getPower();
        }
    }

    private double[] calculateNewValues(double[] values) {
        double[] newValues = new double[realValues.length];
        for (int i = 0; i < newValues.length; i++) {
            newValues[i] = dataset.get(i).getDNI() * (
                    values[0]
                    + (values[1] * dataset.get(i).getDNI())
                    + (values[2] * dataset.get(i).getAmbientTemperature())
                    + (values[3] * dataset.get(i).getWindSpeed())
                    + (values[4] * dataset.get(i).getSMR())
            );
        }

        return newValues;
    }

    @Override
    public double evaluate(double[] values) {
        double[] predicted = calculateNewValues(values);
        double errorSum = 0;
        for (int i = 0; i < realValues.length; i++) {
            double error = Math.abs((realValues[i] - predicted[i]) / realValues[i]);
            errorSum += error;
        }
        return errorSum / realValues.length;
    }
}
