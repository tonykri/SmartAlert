using app.Dto;
using app.Models;
using app.Utils;
using Microsoft.EntityFrameworkCore;

namespace app.Repositories;

public class DangerRepo : IDangerRepo
{
    private readonly DataContext dataContext;
    private readonly IDistanceManager distanceManager;
    public DangerRepo(DataContext dataContext, IDistanceManager distanceManager)
    {
        this.dataContext = dataContext;
        this.distanceManager = distanceManager;
    }

    public async Task<ApiResponse<ICollection<Danger>, Exception>> GetCloseDangers(double latitude, double longitude)
    {
        var data = await dataContext.Dangers
                            .Include(d => d.Category)
                            .OrderByDescending(d => d.NoOfRequests)
                            .ToListAsync();

        var result = new List<Danger>();
        foreach (var d in data)
        {
            if (d.Category.DangerRay > distanceManager.CalculateDistance(latitude, longitude, d.Latitude, d.Longitude))
                result.Add(d);
        }
        return new ApiResponse<ICollection<Danger>, Exception>(result);
    }
}