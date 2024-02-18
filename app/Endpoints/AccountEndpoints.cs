using System.Net;
using app.Dto;
using app.Dto.Request;
using app.Dto.Response;
using app.Services;
using Config.Stracture;
using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.AspNetCore.Mvc;

namespace app.Endpoints;

public class AccountEndpoints : IEndpointDefinition
{
    public void DefineEndpoints(WebApplication app)
    {
        app.MapPost("account/register", Register);
        app.MapPost("account/login", Login);
        app.MapGet("account/refreshtoken", RefreshToken);
    }

    public void DefineServices(IServiceCollection services)
    {
        services.AddScoped<IAccountService, AccountService>();
    }

    private async Task<IResult> Register([FromServices] IAccountService accountService, [FromBody] RegisterRequest data)
    {
        var result = await accountService.Register(data);
        return result.Match<IResult>(
            data => Results.Ok(result.Data),
            exception => Results.BadRequest(result.Exception?.Message)
        );
    }

    private async Task<IResult> Login([FromServices] IAccountService accountService, [FromBody] LoginRequest data)
    {
        var result = await accountService.Login(data);
        return result.Match<IResult>(
            data => Results.Ok(result.Data),
            exception => Results.BadRequest(result.Exception?.Message)
        );
    }

    private async Task<IResult> RefreshToken([FromServices] IAccountService accountService, [FromQuery] string token)
    {   
        var result = await accountService.RefreshToken(token);
        return result.Match<IResult>(
            data => Results.Ok(result.Data),
            exception => Results.BadRequest(result.Exception?.Message)
        );
    }
}