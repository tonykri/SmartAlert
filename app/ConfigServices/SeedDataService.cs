using app.Models;
using app.Utils;

public static class SeedDataService
{
    public static void InitCategories(DataContext context)
    {
        // Ensure the database is created
        context.Database.EnsureCreated();

        // Check and add records
        if (!context.Categories.Any(c => c.Name.Equals("Flood")))
        {
            context.Categories.Add(new Category()
            {
                Name = "Flood",
                DangerRay = 10,
                ProtectionEl = "Πηγαίνετε σε όσο το δυνατόν μεγαλύτερο υψόμετρο",
                ProtectionEn = "Go as high as possible"
            });
        }

        if (!context.Categories.Any(c => c.Name.Equals("Fire")))
        {
            context.Categories.Add(new Category()
            {
                Name = "Fire",
                DangerRay = 15,
                ProtectionEl = "Απομακρυνθείτε από μέρη με πλούσια βλάστηση",
                ProtectionEn = "Move away from places with lush vegetation"
            });
        }

        if (!context.Categories.Any(c => c.Name.Equals("Earthquake")))
        {
            context.Categories.Add(new Category()
            {
                Name = "Earthquake",
                DangerRay = 15,
                ProtectionEl = "Απομακρυνθείτε από ψηλά κτήρια",
                ProtectionEn = "Move away from high buildings"
            });
        }

        // Save changes to the database
        context.SaveChanges();
    }

    public static void InitAdmin(DataContext context, IPasswordManager passwordManager)
    {
        if (context.Users.Any(u => u.Role.Equals("Admin")))
            return;
        
        byte[] PasswordHash, PasswordSalt;
        passwordManager.CreatePasswordHash("password", out PasswordHash, out PasswordSalt);
        var user = new User()
        {
            Firstname = "Euthimios",
            Lastname = "Alepis",
            Email = "talepis@unipi.gr",
            BirthDate = new DateOnly(1973, 03, 01),
            Role = "Admin",
            PasswordHash = PasswordHash,
            PasswordSalt = PasswordSalt
        };
        context.Users.Add(user);
        context.SaveChanges();
    }
}
