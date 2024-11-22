package banquemisr.challenge05.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "USER")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    Long userId;
    @Column(name = "NAME")
    String name;
    @Column(name = "EMAIL")
    String email;
    @Column(name = "PASSWORD")
    String password;
    @Column(name = "MOBILE_NUMBER")
    String mobileNumber;
    @Column(name = "CREATION_DATE", updatable = false, insertable = false)
    LocalDate creationDate;
    @OneToMany
    @JoinColumn(name = "USER_ID")
    List<Task> userTasks;
    @ManyToMany
    @JoinTable(name = "USER_ROLE",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    List<Role> roles;

}
