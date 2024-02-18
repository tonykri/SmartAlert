using app.Models;
using app.Utils;
using Config.Stracture;
using Microsoft.EntityFrameworkCore;

var builder = WebApplication.CreateBuilder(args);
builder.Services.AddHttpContextAccessor();

builder.Services.AddEndpointDefinitions(typeof(IEndpointDefinition));
builder.Services.UtilsConfig();

builder.Services.AddEndpointsApiExplorer();
builder.Services.SwaggerConfig();
builder.Services.CorsConfig();

// Connect to database
string? ConnectionString = builder.Configuration.GetConnectionString("DefaultConnection");
builder.Services.AddDbContext<DataContext>(
    opt => opt.UseSqlServer(ConnectionString)
);

// Configure JWT tokens
string? AppToken = builder.Configuration.GetSection("AppSettings:Token").Value;
builder.Services.AuthConfig(AppToken);

var app = builder.Build();

// Initialize the data
using (var scope = app.Services.CreateScope())
{
    var services = scope.ServiceProvider;

    try
    {
        var context = services.GetRequiredService<DataContext>();
        var passwordManager = services.GetRequiredService<IPasswordManager>();
        SeedDataService.InitCategories(context);
        SeedDataService.InitAdmin(context, passwordManager);
    }
    catch (Exception ex)
    {
        Console.WriteLine($"An error occurred while initializing data: {ex.Message}");
    }
}


app.UseSwagger();
app.UseSwaggerUI();

app.UseCors("AllowAllOrigins");

app.UseEndpointDefinitions();

app.UseAuthentication();
app.UseAuthorization();

app.Run();
