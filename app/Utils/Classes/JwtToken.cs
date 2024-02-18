using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using app.Models;
using Microsoft.IdentityModel.Tokens;

namespace app.Utils;

public class JwtToken : IJwtToken
{
    private readonly IConfiguration configuration;

    public JwtToken(IConfiguration configuration)
    {
        this.configuration = configuration;
    }

    public string CreateLoginToken(User user)
    {
        List<Claim> claims = new List<Claim>
        {
            new Claim(ClaimTypes.Email, user.Email),
            new Claim(ClaimTypes.Role, user.Role),
            new Claim(ClaimTypes.Name, user.Firstname)
        };

        string? AppToken = configuration.GetSection("AppSettings:Token").Value;
        if(AppToken is null)
            throw new Exception("AppSettings:Token not found in configuration");
        var key = new SymmetricSecurityKey(System.Text.Encoding.UTF8.GetBytes(AppToken));

        var creds = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);

        var token = new JwtSecurityToken(
            claims: claims,
            expires: DateTime.Now.AddDays(1),
            signingCredentials: creds);

        var jwt = new JwtSecurityTokenHandler().WriteToken(token);

        return jwt;
    }
}