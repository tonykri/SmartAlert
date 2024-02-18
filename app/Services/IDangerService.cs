using app.Dto;
using app.Models;

namespace app.Services;

public interface IDangerService
{
    public Task<ApiResponse<int, Exception>> CreateUpdateDanger(double latitude, double longitude, string category);
}