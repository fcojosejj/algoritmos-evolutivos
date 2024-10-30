package common;

/**
 * Clase usada para representar cada dato del dataset.
 */
public class DatasetItem {
    private final double DNI;
    private final double ambientTemperature;
    private final double windSpeed;
    private final double SMR;
    private final double power;

    public DatasetItem(double DNI, double ambientTemperature, double windSpeed, double SMR, double power) {
        this.DNI = DNI;
        this.ambientTemperature = ambientTemperature;
        this.windSpeed = windSpeed;
        this.SMR = SMR;
        this.power = power;
    }

    public double getDNI() {
        return DNI;
    }

    public double getAmbientTemperature() {
        return ambientTemperature;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getSMR() {
        return SMR;
    }

    public double getPower() {
        return power;
    }
}
