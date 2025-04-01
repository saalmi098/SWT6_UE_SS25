﻿namespace IntroEF.Domain;

public class Order(Guid id, string article, DateTimeOffset orderDate, decimal totalPrice)
{
  public Order(string article, DateTimeOffset orderDate, decimal totalPrice) :
    this(Guid.NewGuid(), article, orderDate, totalPrice)
  {
  }

  public Guid Id { get; set; } = id;

  public string Article { get; set; } = article;

  public DateTimeOffset OrderDate { get; set; } = orderDate;

  public decimal TotalPrice { get; set; } = totalPrice;

  public override string ToString() => $"Order {{ Id: {Id}, Article: {Article}, OrderDate: {OrderDate:d}, TotalPrice: {TotalPrice:F2} }}";
}
