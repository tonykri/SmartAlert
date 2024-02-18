namespace app.Utils;

public class DistanceManager : IDistanceManager
{
    private readonly double EarthRadiusKm = 6371.0;

    private double DegreeToRadians(double degree)
    {
        return degree * (Math.PI / 180.0);
    }

    public double CalculateDistance(double lat1, double lon1, double lat2, double lon2)
    {
        var dLat = DegreeToRadians(lat2 - lat1);
        var dLon = DegreeToRadians(lon2 - lon1);

        var a = Math.Sin(dLat / 2) * Math.Sin(dLat / 2) +
                Math.Cos(DegreeToRadians(lat1)) * Math.Cos(DegreeToRadians(lat2)) *
                Math.Sin(dLon / 2) * Math.Sin(dLon / 2);

        var c = 2 * Math.Atan2(Math.Sqrt(a), Math.Sqrt(1 - a));

        return EarthRadiusKm * c;
    }

    public double CalculateAverage(double oldAverage, double newValue, int numberOfValues)
    {
        return (oldAverage * numberOfValues + newValue) / (numberOfValues + 1);
    }
}