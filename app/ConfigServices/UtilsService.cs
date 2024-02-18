using app.Utils;

public static class UtilsService
{
    public static void UtilsConfig(this IServiceCollection services)
    {
        services.AddSingleton<ITokenDecoder, TokenDecoder>();
        services.AddSingleton<IJwtToken, JwtToken>();
        services.AddSingleton<IPasswordManager, PasswordManager>();
        services.AddSingleton<IDistanceManager, DistanceManager>();
    }
}