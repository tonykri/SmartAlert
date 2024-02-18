using app.Dto;
using app.Dto.Request;
using app.Dto.Response;

namespace app.Services;

public interface IAccountService
{
    public Task<ApiResponse<LoginResponse, Exception>> Register(RegisterRequest data);
    public Task<ApiResponse<LoginResponse, Exception>> Login(LoginRequest data);
    public Task<ApiResponse<LoginResponse, Exception>> RefreshToken(string token);
}