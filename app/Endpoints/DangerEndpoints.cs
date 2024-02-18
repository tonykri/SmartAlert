using app.Models;
using app.Repositories;
using app.Services;
using Config.Stracture;
using Microsoft.AspNetCore.Mvc;

public class DangerEndpoints : IEndpointDefinition
{
    public void DefineEndpoints(WebApplication app)
    {
        app.MapGet("dangers", GetCloseDangers)
            .RequireAuthorization();
    }

    public void DefineServices(IServiceCollection services)
    {
        services.AddScoped<IDangerRepo, DangerRepo>();
        services.AddScoped<IDangerService, DangerService>();
    }

    private async Task<IResult> GetCloseDangers([FromServices] IDangerRepo dangerRepo, [FromQuery] double latitude, [FromQuery] double longitude)
    {
        var result = await dangerRepo.GetCloseDangers(latitude, longitude);
        return result.Match<IResult>(
            data => Results.Ok(result.Data),
            exception => Results.BadRequest(result.Exception?.Message)
        );
    }
}