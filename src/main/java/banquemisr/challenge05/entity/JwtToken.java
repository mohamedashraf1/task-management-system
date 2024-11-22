package banquemisr.challenge05.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "jwt_token")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TOKEN_ID")
    private Long tokenId;
    @Column(name = "ACCESS_TOKEN")
    private String accessToken;
    @Column(name = "REFRESH_TOKEN")
    private String refreshToken;
    @Column(name = "USER_ID")
    private Long userId;
    @Column(name = "UPDATED_DATE")
    private Date updatedDate;
    @Column(name = "IS_VALID")
    private Boolean isValid = true;

    public JwtToken(String accessToken, String refreshToken, Long userId){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
    }
}
