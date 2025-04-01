namespace IntroEF.Dal; 

using IntroEF.Domain;
using Microsoft.EntityFrameworkCore;

public static class Commands
{
  public static async Task AddCustomersAsync()
  {
    var customer1 = new Customer("Mayr Immobilien", Rating.B);
    var customer2 = new Customer("Software Solutions", Rating.A);

    await Task.CompletedTask;
  }

  public static async Task ListCustomersAsync()
  {
    await Task.CompletedTask;
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
