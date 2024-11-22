package banquemisr.challenge05.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "LO_TASK_STATUS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoTaskStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STATUS_ID")
    private Integer statusId;
    @Column(name = "STATUS_NAME")
    private String status;
}
