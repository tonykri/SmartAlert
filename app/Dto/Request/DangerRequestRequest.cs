namespace app.Dto.Request;

public class DangerRequestRequest
{
    public required string CategoryName { get; set; }
    public required string Message { get; set; }
    public required double Latitude { get; set; }
    public required double Longitude { get; set; }
}