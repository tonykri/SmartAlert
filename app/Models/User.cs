using System.ComponentModel.DataAnnotations;
using System.Diagnostics.CodeAnalysis;

namespace app.Models;

public class User 
{
    [Key, EmailAddress]
    public string Email { get; set; } = null!;
    [Required]
    public string Firstname { get; set; } = null!;
    [Required]
    public string Lastname { get; set; } = null!;
    [Required]
    public byte[] PasswordHash { get; set; } = null!;
    [Required]
    public byte[] PasswordSalt { get; set; } = null!;    
    [Required]
    public DateOnly BirthDate { get; set; }
    [Required]
    public string Role { get; set; } = "User";
    [AllowNull]
    public string? RefreshToken { get; set; }
    [AllowNull]
    public DateTime? ExpiresAt { get; set; }

    public ICollection<DangerRequest> DangerRequests {get; set; } = new List<DangerRequest>();
}