using System.ComponentModel.DataAnnotations;

namespace app.Models;

public class Category
{
    [Key]
    public string Name { get; set; } = null!;
    [Required]
    public string ProtectionEn { get; set; } = null!;
    [Required]
    public string ProtectionEl { get; set; } = null!;
    [Required]
    public int DangerRay { get; set; }
}