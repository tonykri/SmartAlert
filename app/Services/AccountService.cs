using System.Security.Cryptography;
using app.Dto;
using app.Dto.Request;
using app.Dto.Response;
using app.Models;
using app.Utils;
using Microsoft.EntityFrameworkCore;

namespace app.Services;

public class AccountService : IAccountService
{
    private readonly DataContext dataContext;
    private readonly IPasswordManager passwordManager;
    private readonly IJwtToken jwtToken;
    public AccountService(DataContext dataContext, IPasswordManager passwordManager, IJwtToken jwtToken)
    {
        this.dataContext = dataContext;
        this.passwordManager = passwordManager;
        this.jwtToken = jwtToken;
    }

    public async Task<ApiResponse<LoginResponse, Exception>> Register(RegisterRequest data)
    {
        if (await dataContext.Users.AnyAsync(u => u.Email.Equals(data.Email)))
            return new ApiResponse<LoginResponse, Exception>(new Exception(ExceptionMessages.USER_EXISTS));
        
        byte[] PasswordHash, PasswordSalt;
        passwordManager.CreatePasswordHash(data.Password, out PasswordHash, out PasswordSalt);
        var user = new User()
        {
            Firstname = data.Firstname,
            Lastname = data.Lastname,
            Email = data.Email,
            BirthDate = data.BirthDate,
            PasswordHash = PasswordHash,
            PasswordSalt = PasswordSalt
        };
        await dataContext.Users.AddAsync(user);
        await dataContext.SaveChangesAsync();

        return await Login(new LoginRequest(){
            Email = data.Email,
            Password = data.Password
        });
    }

    public async Task<ApiResponse<LoginResponse, Exception>> Login(LoginRequest data)
    {
        var user = await dataContext.Users.FirstOrDefaultAsync(u => u.Email.Equals(data.Email));
        if (user is null)
            return new ApiResponse<LoginResponse, Exception>(new Exception(ExceptionMessages.USER_NOT_FOUND));
        
        if (!passwordManager.VerifyPasswordHash(data.Password, user.PasswordHash, user.PasswordSalt))
            return new ApiResponse<LoginResponse, Exception>(new Exception(ExceptionMessages.WRONG_CREDENTIALS));
        
        var AccessToken = jwtToken.CreateLoginToken(user);
        var RefreshToken = Convert.ToBase64String(RandomNumberGenerator.GetBytes(64));

        user.RefreshToken = RefreshToken;
        user.ExpiresAt = DateTime.Now.AddDays(7);
        await dataContext.SaveChangesAsync();

        var response = new LoginResponse()
        {
            Firstname = user.Firstname,
            Lastname = user.Lastname,
            Email = user.Email,
            Role = user.Role,
            AccessToken = AccessToken,
            RefreshToken = RefreshToken
        };
        return new ApiResponse<LoginResponse, Exception>(response);
    }

    public async Task<ApiResponse<LoginResponse, Exception>> RefreshToken(string token)
    {
        var user = await dataContext.Users.FirstOrDefaultAsync(u => u.RefreshToken != null && u.RefreshToken.Equals(token));
        if (user is null)
            return new ApiResponse<LoginResponse, Exception>(new Exception(ExceptionMessages.USER_NOT_FOUND));
        else if (user.ExpiresAt < DateTime.Now)
            return new ApiResponse<LoginResponse, Exception>(new Exception(ExceptionMessages.EXPIRED_TOKEN));
        
        var AccessToken = jwtToken.CreateLoginToken(user);
        var RefreshToken = Convert.ToBase64String(RandomNumberGenerator.GetBytes(64));

        user.RefreshToken = RefreshToken;
        user.ExpiresAt = DateTime.Now.AddDays(7);
        dataContext.SaveChanges();

        var response = new LoginResponse()
        {
            Firstname = user.Firstname,
            Lastname = user.Lastname,
            Email = user.Email,
            Role = user.Role,
            AccessToken = AccessToken,
            RefreshToken = RefreshToken
        };
        return new ApiResponse<LoginResponse, Exception>(response);
    }
}