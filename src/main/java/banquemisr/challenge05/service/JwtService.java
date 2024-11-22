package banquemisr.challenge05.service;


import banquemisr.challenge05.constants.Constant;
import banquemisr.challenge05.dto.JwtTokenDto;
import banquemisr.challenge05.entity.JwtToken;
import banquemisr.challenge05.entity.Role;
import banquemisr.challenge05.entity.User;
import banquemisr.challenge05.repo.JwtTokenRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final ModelMapper modelMapper;
    private final JwtTokenRepo jwtTokenRepo;

    private static final String SECRET_KEY =
            "lkjsdfjsaljfkYsldkaafsjlkjfsksdhklsdfhlksdhfalkfhasjkfhalskhalskfhaskflaklfashkljhsakjhfkashfkahfjksadf";

    @Value("${jwt.access.token.validity}")
    private long accessTokenValidityInSeconds; // 1 hrs

    @Value("${jwt.refresh.token.validity}")
    private long refreshTokenValidityInSeconds; // 8 hrs

    JwtTokenDto generateTokensByUser(User user) {

        Map<String, Object> data = new HashMap<>();
        data.put(Constant.USER_ID, user.getUserId());
        data.put(Constant.NAME, user.getName());
        data.put(Constant.MOBILE_NUMBER, user.getMobileNumber());
        data.put(Constant.EMAIL, user.getEmail());
        data.put(Constant.ROLES, user.getRoles().stream().map(Role::getName).toList());

        return generateTokens(data);
    }

    JwtTokenDto generateTokens(Map<String, Object> data) {
        JwtToken token;

        if (data.get(Constant.USER_ID) == null)
            return null;

        String accessToken = Jwts.builder()
                .setClaims(data)
                .setSubject(data.get(Constant.USER_ID).toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidityInSeconds * 1000))
                .signWith(getSecretKey()).compact();

        String refreshToken = Jwts.builder()
                .claim(Constant.USER_ID, data.get(Constant.USER_ID).toString())
                .setSubject(data.get(Constant.USER_ID).toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidityInSeconds * 1000))
                .signWith(getSecretKey()).compact();

        List<JwtToken> jwtTokenList = jwtTokenRepo.findByUserId(Long.parseLong(data.get(Constant.USER_ID).toString()));
        if (jwtTokenList != null && !jwtTokenList.isEmpty()) {
            token = jwtTokenList.get(0);
            token.setAccessToken(accessToken);
            token.setRefreshToken(refreshToken);
            token.setIsValid(true);
            token.setUpdatedDate(new Date());
            jwtTokenRepo.save(token);
        } else {
            token = new JwtToken(accessToken, refreshToken, Long.parseLong(data.get(Constant.USER_ID).toString()));
            jwtTokenRepo.save(token);
        }

        return modelMapper.map(token, JwtTokenDto.class);
    }

    public boolean isTokenExpired(String token) {
        try {
            extractAllClaims(token);
            return false;
        } catch (ExpiredJwtException e) {
            // Token is expired, but we can still extract the claims from the exception
            return true;
        }
    }

    public boolean isTokenValid(String userId, String accessToken) {
        if (userId == null || userId.isEmpty() || accessToken == null || accessToken.isEmpty())
            return false;

        List<JwtToken> tokens = jwtTokenRepo.findByUserId(Long.parseLong(userId));

        return tokens != null && !tokens.isEmpty() && accessToken.equals(tokens.get(0).getAccessToken()) && tokens.get(0).getIsValid();
    }

    SecretKey getSecretKey() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token).getBody();
    }

    public Claims extractAllClaimsExpired(String token) {
        try {
            return extractAllClaims(token);
        } catch (ExpiredJwtException e) {
            // Token is expired, but we can still extract the claims from the exception
            return e.getClaims();
        }
    }

}
