using app.Dto;
using app.Dto.Request;
using app.Dto.Response;
using app.Models;
using app.Utils;
using Microsoft.EntityFrameworkCore;

namespace app.Services;

public class DangerRequestService : IDangerRequestService
{
    private readonly DataContext dataContext;
    private readonly ITokenDecoder tokenDecoder;
    private readonly IDangerService dangerService;
    public DangerRequestService(DataContext dataContext, ITokenDecoder tokenDecoder, IDangerService dangerService)
    {
        this.dataContext = dataContext;
        this.tokenDecoder = tokenDecoder;
        this.dangerService = dangerService;
    }

    public async Task<ApiResponse<int, Exception>> UploadRequest(DangerRequestRequest requestDto)
    {
        string userEmail = tokenDecoder.GetUserEmail();
        var user = await dataContext.Users.FirstOrDefaultAsync(u => u.Email.Equals(userEmail));
        if (user is null)
            return new ApiResponse<int, Exception>(new Exception(ExceptionMessages.USER_NOT_FOUND));

        var category = dataContext.Categories.FirstOrDefault(c => c.Name.Equals(requestDto.CategoryName));
        if (category is null)
            return new ApiResponse<int, Exception>(new Exception(ExceptionMessages.CATEGORY_NOT_FOUND));

        var dangerRequest = new DangerRequest()
        {
            UserEmail = userEmail,
            User = user,
            Message = requestDto.Message,
            Latitude = requestDto.Latitude,
            Longitude = requestDto.Longitude,
            Category = category,
            CategoryName = requestDto.CategoryName
        };
        await dataContext.DangerRequests.AddAsync(dangerRequest);

        await dataContext.SaveChangesAsync();
        return new ApiResponse<int, Exception>(0);
    }

    public async Task<ApiResponse<int, Exception>> ApproveDangerRequest(Guid id)
    {
        var request = await dataContext.DangerRequests.FirstOrDefaultAsync(r => r.Id == id);
        if (request is null)
            return new ApiResponse<int, Exception>(new Exception(ExceptionMessages.REQUEST_NOT_FOUND));

        request.Approved = true;
        await dataContext.SaveChangesAsync();
        var result = await dangerService.CreateUpdateDanger(request.Latitude, request.Longitude, request.CategoryName);

        bool IsCompleted = result.Match<bool>(
            data => true,
            exception => false
        );
        if (!IsCompleted)
            return new ApiResponse<int, Exception>(new Exception(result.Exception?.Message));
        
        return new ApiResponse<int, Exception>(0);
    }
}
