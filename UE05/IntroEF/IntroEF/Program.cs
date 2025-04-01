using IntroEF.Dal;
using IntroEF.Utils;
using static IntroEF.Utils.PrintUtil;

PrintTitle("Introduction to EntityFramework", character: '=');

PrintTitle("Creating database", '-');
await using (var db = new OrderManagementContext())
{
    await DatabaseUtil.CreateDatabaseAsync(db, recreate: true);
}

PrintTitle("AddCustomersAsync", '-');
await Commands.AddCustomersAsync();

PrintTitle("ListCustomersAsync", '-');
await Commands.ListCustomersAsync();

PrintTitle("AddOrdersAsync", '-');
await Commands.AddOrdersAsync();
