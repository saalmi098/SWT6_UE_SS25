namespace IntroEF.Domain;

//[Table("TBL_CUSTOMER")] // Attribute spielen in EF eher eine untergeordnete Rolle (daher wieder auskommentiert) -> Fluent-API
public class Customer(Guid id, string name, Rating rating)
{
    // Unterschied zu JPA: Kein leerer Konstruktor notwendig

    public Customer(string name, Rating rating) : this(Guid.NewGuid(), name, rating)
    {
    }

    //[Key]
    public Guid Id { get; set; } = id;

    //[Required]
    //[Column("COL_NAME")]
    //[Required, Column("COL_NAME")]
    public string Name { get; set; } = name;
    public Rating Rating { get; set; } = rating;

    //[Column(TypeName = "decimal(18, 2)")]
    public decimal? TotalRevenue { get; set; }

    public Address? Address { get; set; }

    public IList<Order> Orders { get; set; } = []; // 1:n-Beziehung

    public override string ToString() => $"Customer {{ Id: {Id}, Name: {Name}, " +
                                         $"TotalRevenue: {TotalRevenue} }}";
}
