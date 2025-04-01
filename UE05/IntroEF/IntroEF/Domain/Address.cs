using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace IntroEF.Domain;

public class Address(int zipCode, string city, string? street = null)
{
  public int Id { get; set; }
  public int ZipCode { get; set; } = zipCode;
  public string City { get; set; } = city;
  public string? Street { get; set; } = street;

  public override string ToString() => $"Address {{ ZipCode: {ZipCode}, City: {City}, Street: {Street ?? ""} }}";
}
