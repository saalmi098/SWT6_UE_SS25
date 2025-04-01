namespace IntroEF.Dal;

using IntroEF.Domain;
using Microsoft.EntityFrameworkCore;

public static class Commands
{
    public static async Task AddCustomersAsync()
    {
        var customer1 = new Customer("Mayr Immobilien", Rating.B);
        customer1.Address = new Address(4020, "Linz", "Wiener Strasse 10");
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
            //.Include(c => c.Address) // Address mitladen = Eager Loading (Hinweis: ist hier aber nicht notwendig, da eingebettet)
            .Include(c => c.Orders)
            .ToListAsync();

        foreach (var customer in customers)
        {
            Console.WriteLine(customer);
            if (customer.Address is not null)
            {
                Console.WriteLine($"  Address: {customer.Address}");
            }

            if (customer.Orders.Any())
            {
                Console.WriteLine("  Orders:");
                foreach (var order in customer.Orders)
                {
                    Console.WriteLine($"    {order}"); // erfordert Include(c => c.Orders) in Query
                }
            }
        }
    }

    public static async Task AddOrdersAsync()
    {
        await using var db = new OrderManagementContext();
        var customer = await db.Customers.FirstAsync();

        if (customer is null)
        {
            return;
        }

        var order1 = new Order("Surface Book 3", new DateTime(2022, 1, 1), 2850m) { Customer = customer };
        var order2 = new Order("Dell Monitor", new DateTime(2022, 2, 2), 250m) { Customer = customer };

        await db.Orders.AddRangeAsync(order1, order2);
    }

    public static async Task UpdateTotalRevenuesAsync()
    {
        await Task.CompletedTask;
    }
}
