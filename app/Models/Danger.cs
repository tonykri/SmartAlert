using System.ComponentModel.DataAnnotations;

namespace app.Models;

public class Danger
{
    [Key]
    public Guid Id { get; set; } = Guid.NewGuid();
    [Required]
    public string CategoryName { get; set; } = null!;
    [Required]
    public Category Category { get; set; } = null!;
    [Required]
    public DateTime CreatedAt { get; set; } = DateTime.Now;
    [Required]
    public int NoOfRequests { get; set; } 
    [Required]
    public double Latitude { get; set; } 
    [Required]
    public double Longitude { get; set; } 
}