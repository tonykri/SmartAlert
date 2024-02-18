using app.Dto;
using app.Dto.Request;
using app.Dto.Response;
using app.Repositories;
using app.Services;
using Config.Stracture;
using Microsoft.AspNetCore.Mvc;

namespace app.Endpoints;

public class DangerRequestsEndpoints : IEndpointDefinition
{
    public void DefineEndpoints(WebApplication app)
    {
        app.MapPost("user/dangerrequests", UploadRequest)
            .RequireAuthorization("user");
        app.MapGet("user/dangerrequests", GetUserRequests)
            .RequireAuthorization("user");
        app.MapGet("admin/dangerrequests", GetAdminRequests)
            .RequireAuthorization("admin");
        app.MapGet("user/dangerrequests/approved", GetUserApprovedRequests)
            .RequireAuthorization("user");
        app.MapGet("admin/dangerrequests/approve", ApproveDangerRequest)
            .RequireAuthorization("admin");
    }

    public void DefineServices(IServiceCollection services)
    {
        services.AddScoped<IDangerRequestService, DangerRequestService>();
        services.AddScoped<IDangerRequestRepo, DangerRequestRepo>();
    }

    private async Task<IResult> UploadRequest([FromServices] IDangerRequestService dangerRequestService, [FromBody] DangerRequestRequest requestDto)
    {
        var result = await dangerRequestService.UploadRequest(requestDto);
        return result.Match<IResult>(
            data => Results.Ok(result.Data),
            exception => Results.BadRequest(result.Exception?.Message)
        );
    }

    private async Task<IResult> GetUserRequests([FromServices] IDangerRequestRepo dangerRequestRepo)
    {
        var result = await dangerRequestRepo.GetUserRequests();
        return result.Match<IResult>(
            data => Results.Ok(result.Data),
            exception => Results.BadRequest(result.Exception?.Message)
        );
    }

    private async Task<IResult> GetAdminRequests([FromServices] IDangerRequestRepo dangerRequestRepo)
    {
        var result = await dangerRequestRepo.GetAdminRequests();
        return result.Match<IResult>(
            data => Results.Ok(result.Data),
            exception => Results.BadRequest(result.Exception?.Message)
        );
    }

    private async Task<IResult> GetUserApprovedRequests([FromServices] IDangerRequestRepo dangerRequestRepo)
    {
        var result = await dangerRequestRepo.GetUserApprovedRequests();
        return result.Match<IResult>(
            data => Results.Ok(result.Data),
            exception => Results.BadRequest(result.Exception?.Message)
        );
    }

    private async Task<IResult> ApproveDangerRequest([FromServices] IDangerRequestService dangerRequestService, [FromQuery] Guid requestId)
    {
        var result = await dangerRequestService.ApproveDangerRequest(requestId);
        return result.Match<IResult>(
            data => Results.Ok(result.Data),
            exception => Results.BadRequest(result.Exception?.Message)
        );
    }
}
