using app.Dto;
using app.Dto.Request;

namespace app.Services;

public interface IDangerRequestService
{
    public Task<ApiResponse<int, Exception>> UploadRequest(DangerRequestRequest requestDto);

    public Task<ApiResponse<int, Exception>> ApproveDangerRequest(Guid id);
}