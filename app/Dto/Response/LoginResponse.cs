namespace app.Dto.Response;

public class LoginResponse
{
    public string Firstname { get; set; } = null!;
    public string Lastname { get; set; } = null!;
    public string Email { get; set; } = null!;
    public string Role { get; set; } = null!;
    public string RefreshToken { get; set; } = null!;
    public string AccessToken { get; set; } = null!;
}