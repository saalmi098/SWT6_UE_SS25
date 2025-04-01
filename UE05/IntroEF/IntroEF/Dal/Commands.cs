namespace IntroEF.Dal;

using IntroEF.Domain;
using Microsoft.EntityFrameworkCore;

public static class Commands
{
    public static async Task AddCustomersAsync()
    {
        var customer1 = new Customer("Mayr Immobilien", Rating.B);
        var customer2 = new Customer("Software Solutions", Rating.A);

        await using var db = new OrderManagementContext();
        await db.Customers.AddRangeAsync(customer1, customer2);
        await db.SaveChangesAsync();
    }

    public static async Task ListCustomersAsync()
    {
        await using var db = new OrderManagementContext();
        var customers = await db.Customers
            .AsNoTracking() // Kein Change-Tracking notwendig, da nur gelesen wird
            .ToListAsync();

        foreach (var customer in customers)
        {
            Console.WriteLine(customer);
        }
    }

    public static async Task AddOrdersAsync()
    {
        var order1 = new Order("Surface Book 3", new DateTime(2022, 1, 1), 2850m);
        var order2 = new Order("Dell Monitor", new DateTime(2022, 2, 2), 250m);

        await Task.CompletedTask;
    }

    public static async Task UpdateTotalRevenuesAsync()
    {
        await Task.CompletedTask;
    }
}
