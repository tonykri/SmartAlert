using app.Dto;
using app.Dto.Request;
using app.Dto.Response;
using app.Models;
using app.Utils;
using Microsoft.EntityFrameworkCore;

namespace app.Repositories;

public class DangerRequestRepo : IDangerRequestRepo {
    private readonly DataContext dataContext;
    private readonly IDistanceManager distanceManager;
    private readonly ITokenDecoder tokenDecoder;
    public DangerRequestRepo(DataContext dataContext, IDistanceManager distanceManager, ITokenDecoder tokenDecoder)
    {
        this.dataContext = dataContext;
        this.distanceManager = distanceManager;
        this.tokenDecoder = tokenDecoder;
    }

    private async Task<ICollection<GetDangerRequestResponse>> GetRequests(string? userEmail = null)
    {

        var requests = new List<GetDangerRequestResponse>();
        var savedRequests = new List<DangerRequest>();
        if (userEmail is null)
        {
            savedRequests = await dataContext.DangerRequests
                .Include(r => r.Category)
                .Where(r => r.CreatedAt.AddDays(2) > DateTime.Now && r.Approved == false)
                .ToListAsync();
        }
        else
        {
            savedRequests = await dataContext.DangerRequests
                .Include(r => r.Category)
                .Where(r => r.UserEmail.Equals(userEmail))
                .ToListAsync();
        }


        foreach (var request in savedRequests)
        {
            var temp = new GetDangerRequestResponse()
            {
                Id = request.Id,
                CategoryName = request.CategoryName,
                CreatedAt = request.CreatedAt,
                Message = request.Message,
                Latitude = request.Latitude,
                Longitude = request.Longitude,
                Approved = request.Approved,
                similarRequests = -1
            };
            foreach (var similarRequest in savedRequests)
            {
                var distance = distanceManager.CalculateDistance(request.Latitude, request.Longitude, similarRequest.Latitude, similarRequest.Longitude);
                if (request.Category.DangerRay > distance)
                    temp.similarRequests++;
            }
            requests.Add(temp);
        }

        if (userEmail is null)
            return requests.OrderByDescending(r => r.similarRequests).ToList();
        else
            return requests.OrderByDescending(r => r.CreatedAt).ToList();
    }

    public async Task<ApiResponse<ICollection<GetDangerRequestResponse>, Exception>> GetUserRequests()
    {
        string userEmail = tokenDecoder.GetUserEmail();
        var requests = await GetRequests(userEmail);

        return new ApiResponse<ICollection<GetDangerRequestResponse>, Exception>(requests);
    }

    public async Task<ApiResponse<ICollection<GetDangerRequestResponse>, Exception>> GetAdminRequests()
    {
        var requests = await GetRequests();
        return new ApiResponse<ICollection<GetDangerRequestResponse>, Exception>(requests);
    }

    public async Task<ApiResponse<object, Exception>> GetUserApprovedRequests()
    {
        string userEmail = tokenDecoder.GetUserEmail();

        var savedRequests = await dataContext.DangerRequests
            .Where(r => r.UserEmail.Equals(userEmail))
            .ToListAsync();

        int approved = 0;
        foreach (var request in savedRequests)
        {
            if (request.Approved)
                approved++;
        }

        var result = new
        {
            Total = savedRequests.Count,
            Approved = approved
        };

        return new ApiResponse<object, Exception>(result);
    }

}