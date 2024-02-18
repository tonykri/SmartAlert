using Microsoft.EntityFrameworkCore;

namespace app.Models;

public class DataContext : DbContext
{
    public DataContext(DbContextOptions<DataContext> opt ) : base(opt)
    {
        
    }

    public DbSet<Category> Categories { get; set; }
    public DbSet<Danger> Dangers { get; set; }
    public DbSet<DangerRequest> DangerRequests { get; set; }
    public DbSet<User> Users { get; set; }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        
    }
}