using app.Dto;
using app.Dto.Response;

namespace app.Repositories;

public interface IDangerRequestRepo {
    public Task<ApiResponse<ICollection<GetDangerRequestResponse>, Exception>> GetUserRequests();
    public Task<ApiResponse<ICollection<GetDangerRequestResponse>, Exception>> GetAdminRequests();
    public Task<ApiResponse<object, Exception>> GetUserApprovedRequests();
}