using app.Models;

namespace app.Utils;

public interface IJwtToken
{
    public string CreateLoginToken(User user);
}