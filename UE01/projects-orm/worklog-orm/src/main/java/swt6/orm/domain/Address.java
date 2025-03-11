package swt6.orm.domain;

import java.io.Serial;
import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// V2 (OneToOne)
//@Entity

// V3 (Embedded)
@Embeddable

@Setter
@Getter
@ToString
@NoArgsConstructor
public class Address implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  // V2 (OneToOne)
//  @Id @GeneratedValue
//  private Long     id;

  // V3 (Embedded): Keine ID Property!

  private String   zipCode;
  private String   city;
  private String   street;

  public Address(String zipCode, String city, String street) {
    this.zipCode = zipCode;
    this.city = city;
    this.street = street;
  }
}
