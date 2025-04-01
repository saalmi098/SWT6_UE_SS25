namespace IntroEF.Utils;

using Microsoft.EntityFrameworkCore;

public static class DatabaseUtil
{
  public static async Task<bool> CreateDatabaseAsync(DbContext db, bool recreate = false)
  {
    if (recreate)
    {
      await db.Database.EnsureDeletedAsync();
    }

    bool wasCreated = await db.Database.EnsureCreatedAsync();
    if (wasCreated)
    {
      Console.WriteLine($"Created database {db.Database.GetDbConnection().Database}");
    }
    else
    {
      Console.WriteLine($"Database {db.Database.GetDbConnection().Database} already exists");
    }

    return wasCreated;
  }
}
