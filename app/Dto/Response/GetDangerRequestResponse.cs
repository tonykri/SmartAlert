namespace app.Dto.Response;

public class GetDangerRequestResponse
{
    public Guid Id { get; set; } 
    public string CategoryName { get; set; } = null!;
    public DateTime CreatedAt { get; set; } 
    public string Message { get; set; } = null!;
    public double Latitude { get; set; } 
    public double Longitude { get; set; } 
    public bool Approved { get; set; }
    public int similarRequests { get; set; } = 0;
}