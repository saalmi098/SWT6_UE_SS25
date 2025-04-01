using IntroEF.Domain;
using IntroEF.Utils;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;

namespace IntroEF.Dal;

internal class OrderManagementContext : DbContext // DbContext entspricht EntityManager in JPA
{
    public DbSet<Customer> Customers => Set<Customer>();
    public DbSet<Order> Orders => Set<Order>();

    protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
    {
        optionsBuilder
            .UseSqlServer(ConfigurationUtil.GetConnectionString("OrderDbConnection"))
            .EnableSensitiveDataLogging()
            .LogTo(Console.WriteLine, LogLevel.Information);
    }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        //modelBuilder.Entity<Customer>()
        //    .ToTable("TBL_CUSTOMER")
        //    .HasKey(c => c.Id);

        //modelBuilder.Entity<Customer>()
        //    .Property(c => c.Name)
        //    .HasColumnName("COL_NAME")
        //    .IsRequired();

        modelBuilder.Entity<Customer>()
            .OwnsOne(c => c.Address, ca => // = Embedded in JPA -> ID-Property in Address muss entfernt werden
            {
                ca.Property(a => a.ZipCode).HasColumnName("ADDR_ZIPCODE");
                ca.Property(a => a.City).HasColumnName("ADDR_CITY");
                ca.Property(a => a.Street).HasColumnName("ADDR_STREET");
            });

        modelBuilder.Entity<Customer>()
            .HasMany(c => c.Orders)
            .WithOne(o => o.Customer)
            .IsRequired();
            //.OnDelete(DeleteBehavior.Cascade);
    }
}
