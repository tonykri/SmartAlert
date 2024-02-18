using app.Dto;
using app.Models;

namespace app.Repositories;

public interface IDangerRepo {
    public Task<ApiResponse<ICollection<Danger>, Exception>> GetCloseDangers(double latitude, double longitude);
}