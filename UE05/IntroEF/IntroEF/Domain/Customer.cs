using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
namespace IntroEF.Domain;

public class Customer(Guid id, string name, Rating rating)
{
  public Customer(string name, Rating rating) : this(Guid.NewGuid(), name, rating)
  {
  }

  public Guid Id { get; set; } = id;
  public string Name { get; set; } = name;
  public Rating Rating { get; set; } = rating;
  public decimal? TotalRevenue { get; set; }

  public override string ToString() => $"Customer {{ Id: {Id}, Name: {Name}, " +
                                       $"TotalRevenue: {TotalRevenue} }}";
}
