package swt6.orm.domain;

import java.io.Serial;
import java.time.LocalDate;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@DiscriminatorValue("P") // fuer Inheritance strategy = SINGLE_TABLE
@Getter 
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class PermanentEmployee extends Employee {
  @Serial
  private static final long serialVersionUID = 1L;

  private double salary;
  
  public PermanentEmployee(String firstName, String lastName, LocalDate dateOfBirth) {
    super(firstName, lastName, dateOfBirth);
  }
}
