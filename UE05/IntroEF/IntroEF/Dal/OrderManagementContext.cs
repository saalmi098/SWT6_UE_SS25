using IntroEF.Domain;
using IntroEF.Utils;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;

namespace IntroEF.Dal;

internal class OrderManagementContext : DbContext // DbContext entspricht EntityManager in JPA
{
    public DbSet<Customer> Customers => Set<Customer>();

    protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
    {
        optionsBuilder
            .UseSqlServer(ConfigurationUtil.GetConnectionString("OrderDbConnection"))
            .EnableSensitiveDataLogging()
            .LogTo(Console.WriteLine, LogLevel.Information);
    }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.Entity<Customer>()
            .ToTable("TBL_CUSTOMER")
            .HasKey(c => c.Id);

        modelBuilder.Entity<Customer>()
            .Property(c => c.Name)
            .HasColumnName("COL_NAME")
            .IsRequired();
    }
}
