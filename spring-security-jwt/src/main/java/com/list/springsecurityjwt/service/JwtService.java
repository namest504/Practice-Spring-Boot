package com.list.springsecurityjwt.service;

import com.list.springsecurityjwt.entity.Member;
import com.list.springsecurityjwt.exception.CustomException;
import com.list.springsecurityjwt.repository.MemberRepository;
import com.list.springsecurityjwt.security.jwt.MemberPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

	private final Key secretKey;
	private MemberRepository memberRepository;

	private final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);

	private static final Long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60L * 30L;           // n(30)분
	private static final Long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60L * 60L * 24L;	 // n(1)일

	public static final String BEARER_PREFIX = "Bearer ";

//	Long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60L * 5L; // 5분
//	Long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60L * 60L * 24L; // 24시간

	public JwtService(@Value("${jwt.secret}") String secretKey, MemberRepository memberRepository) {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.secretKey = Keys.hmacShaKeyFor(keyBytes);
		this.memberRepository = memberRepository;
	}

	public String createAccessToken(Long uid) {
		Claims claims = Jwts.claims().setSubject("access_token");
		claims.put("uid", uid);
		Date currentTime = new Date();

		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(currentTime)
				.setExpiration(new Date(currentTime.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
				.signWith(secretKey, SignatureAlgorithm.HS256)
				.compact();
	}

	public String createRefreshToken(Long uid) {
		Claims claims = Jwts.claims().setSubject("refresh_token");
		claims.put("uid", uid);
		Date currentTime = new Date();

		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(currentTime)
				.setExpiration(new Date(currentTime.getTime() + REFRESH_TOKEN_EXPIRE_TIME))
				.signWith(secretKey, SignatureAlgorithm.HS256)
				.compact();
	}

	public UsernamePasswordAuthenticationToken getAuthentication(String token) {
		LOGGER.info("[JwtService] getAuthentication 시작");
//		Long uid = getUidFromToken(sliceToken(token));
		Long uid = getUidFromToken(token);
		LOGGER.info("[JwtService] getUidFromToken 의 결과 : {}", uid);
		Member member = memberRepository.findById(uid)
				.orElseThrow(() -> new RuntimeException("Member 를 찾지 못했습니다."));
		MemberPrincipal memberPrincipal = new MemberPrincipal(member);
		return new UsernamePasswordAuthenticationToken(memberPrincipal, token,
				member.getAuthorities());
	}

//	public String sliceToken(String token) {
//		LOGGER.info("[JwtService] sliceToken 시작");
//		String slicedToken = null;
//		if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
//			slicedToken = token.substring(7);
//		} else {
//			throw new CustomException(HttpStatus.BAD_REQUEST, "토큰 값이 잘못 전달되었습니다.");
//		}
//		LOGGER.info("[JwtService] sliceToken 의 결과 : {}", slicedToken);
//		return slicedToken;
//	}

	public Long getUidFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody()
				.get("uid",
						Long.class);
	}

	public Boolean validateAccessToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(secretKey).build()
					.parseClaimsJws(token).getBody();

			return true;
		} catch (Exception e) {
			throw new CustomException(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다.");
		}
	}

	public Claims decodeJwtToken(String token) {
		try {
			Claims claim = Jwts.parserBuilder().setSigningKey(secretKey).build()
					.parseClaimsJws(token).getBody();
			return claim;
		} catch (SecurityException | MalformedJwtException | ExpiredJwtException
				| UnsupportedJwtException
				| IllegalArgumentException | SignatureException e) {
			throw new CustomException(HttpStatus.UNAUTHORIZED, "잘못된 접근입니다.");
		}
	}
}
