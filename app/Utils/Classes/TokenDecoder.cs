using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;

namespace app.Utils;

public class TokenDecoder : ITokenDecoder
{
    private IHttpContextAccessor httpContextAccessor;
    public TokenDecoder(IHttpContextAccessor httpContextAccessor)
    {
        this.httpContextAccessor = httpContextAccessor;
    }

    private Dictionary<string, string> GetClaims(string jwtToken)
    {
        var tokenHandler = new JwtSecurityTokenHandler();
        var token = tokenHandler.ReadJwtToken(jwtToken);
        var claims = token.Claims;

        Dictionary<string, string> hashMap = new Dictionary<string, string>();

        foreach (var claim in claims)
        {
            hashMap[claim.Type] = claim.Value;
        }
        return hashMap;
    }

    public string GetUserEmail()
    {
        var givenToken = httpContextAccessor.HttpContext?.Request.Headers.Authorization.FirstOrDefault();
        if (givenToken is null)
            throw new InvalidDataException("Invalid token");
        givenToken = givenToken.Split(" ").Last();
        Dictionary<string, string> claims = GetClaims(givenToken);

        return claims[ClaimTypes.Email];
    }
}