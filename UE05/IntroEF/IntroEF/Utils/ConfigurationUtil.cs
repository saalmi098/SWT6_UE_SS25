namespace IntroEF.Utils; 

using Microsoft.Extensions.Configuration;

public static class ConfigurationUtil
{
  private static IConfiguration? configuration = null;

  public static IConfiguration GetConfiguration() =>
    configuration ??= new ConfigurationBuilder()
      .AddJsonFile("appsettings.json", optional: false)
      .Build();

  public static string GetConnectionString(string connectionStringName)
  {
    var connectionString = GetConfiguration().GetSection("ConnectionStrings")[connectionStringName];
    if (connectionString is null)
    {
      throw new ArgumentException($"No connection string found for {connectionStringName}");
    }

    return connectionString;
  }
}
