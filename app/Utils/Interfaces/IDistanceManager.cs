namespace app.Utils;

public interface IDistanceManager
{
    public double CalculateDistance(double lat1, double lon1, double lat2, double lon2);
    public double CalculateAverage(double oldAverage, double newValue, int numberOfValues);
}