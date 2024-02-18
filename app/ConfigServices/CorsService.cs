public static class CorsService
{
    public static void CorsConfig(this IServiceCollection services)
    {
        services.AddCors(options =>
                {
                    options.AddPolicy("AllowAllOrigins", builder =>
                    {
                        builder.AllowAnyOrigin()
                               .AllowAnyHeader()
                               .AllowAnyMethod();
                    });
                });
    }
}