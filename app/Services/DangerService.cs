using app.Dto;
using app.Models;
using app.Utils;
using Microsoft.EntityFrameworkCore;

namespace app.Services;

public class DangerService : IDangerService
{
    private readonly DataContext dataContext;
    private readonly IDistanceManager distanceManager;

    public DangerService(DataContext dataContext, IDistanceManager distanceManager)
    {
        this.dataContext = dataContext;
        this.distanceManager = distanceManager;
    }

    public async Task<ApiResponse<int, Exception>> CreateUpdateDanger(double latitude, double longitude, string category)
    {
        var dangers = await dataContext.Dangers
        .Include(d => d.Category)
            .Where(d => d.CreatedAt.AddDays(2) > DateTime.Now &&
                d.CategoryName.Equals(category))
            .ToListAsync();
        
        Console.WriteLine(dangers.Count());
        Console.WriteLine(dangers.Count());
        Console.WriteLine(dangers.Count());
        Console.WriteLine(dangers.Count());

        var data = new List<Danger>();
        foreach (var d in dangers)
        {
            if (d.Category.DangerRay > distanceManager.CalculateDistance(latitude, longitude, d.Latitude, d.Longitude))
                data.Add(d);
            Console.WriteLine(data.Count());
        }
        

        if (data.Any())
        {
            foreach (var danger in data)
            {
                var newLatitude = distanceManager.CalculateAverage(danger.Latitude, latitude, danger.NoOfRequests);
                var newLongitude = distanceManager.CalculateAverage(danger.Longitude, longitude, danger.NoOfRequests);
                danger.NoOfRequests++;
                danger.Latitude = newLatitude;
                danger.Longitude = newLongitude;
            }
        }
        else
        {
            var cat = dataContext.Categories.FirstOrDefault(c => c.Name.Equals(category));
            if (cat is null)
                return new ApiResponse<int, Exception>(new Exception(ExceptionMessages.CATEGORY_NOT_FOUND));

            var danger = new Danger()
            {
                CategoryName = category,
                Category = cat,
                NoOfRequests = 1,
                Latitude = latitude,
                Longitude = longitude
            };
            await dataContext.Dangers.AddAsync(danger);
        }
        await dataContext.SaveChangesAsync();
        return new ApiResponse<int, Exception>(0);
    }
}
