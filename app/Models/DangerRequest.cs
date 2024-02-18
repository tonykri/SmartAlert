using System.ComponentModel.DataAnnotations;

namespace app.Models;

public class DangerRequest
{
    [Key]
    public Guid Id { get; set; } = Guid.NewGuid();
    [Required]
    public string CategoryName { get; set; } = null!;
    [Required]
    public Category Category { get; set; } = null!;
    [Required]
    public string UserEmail { get; set; } = null!;
    [Required]
    public User User { get; set; } = null!;
    [Required]
    public DateTime CreatedAt { get; set; } = DateTime.Now;
    [Required]
    public string Message { get; set; } = null!;
    [Required]
    public double Latitude { get; set; } 
    [Required]
    public double Longitude { get; set; } 
    [Required]
    public bool Approved { get; set; } = false;
}