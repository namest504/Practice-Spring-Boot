package com.list.springsecurityjwt.service;

import com.list.springsecurityjwt.dto.MemberDto;

import com.list.springsecurityjwt.entity.RefreshToken;
import com.list.springsecurityjwt.repository.RefreshTokenRepository;
import com.list.springsecurityjwt.security.jwt.JwtAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.list.springsecurityjwt.entity.Member;
import com.list.springsecurityjwt.exception.CustomException;
import com.list.springsecurityjwt.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.list.springsecurityjwt.dto.MemberDto.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    private final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    public Member register(RegisterRequestDto registerRequestDto) {
        LOGGER.info("[AuthService] register 시작");
        if (memberRepository.existsByName(registerRequestDto.getName())) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "이미 가입되어있는 아이디 입니다.");
        }

        Member member = Member.builder()
                .name(registerRequestDto.getName())
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .isBan(false)
                .build();
        LOGGER.info("[AuthService] register 종료: member.getUsername() = {}", member.getUsername());
        LOGGER.info("[AuthService] register 종료: member.getName() = {}", member.getName());
        LOGGER.info("[AuthService] register 종료: member.getAuthorities() = {}", member.getAuthorities());
        return memberRepository.save(member);
    }

    public Long login(LoginDto login) {
        LOGGER.info("[AuthService] login 시작");
        Member member = memberRepository.findByName(login.getName())
                .orElseThrow(
                () -> new CustomException(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 일치하지 않습니다."));

        if (!passwordEncoder.matches(login.getPassword(), member.getPassword())) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        LOGGER.info("[AuthService] register 종료: member.getUsername() = {}", member.getUsername());
        LOGGER.info("[AuthService] register 종료: member.getName() = {}", member.getName());
        LOGGER.info("[AuthService] register 종료: member.getAuthorities() = {}", member.getAuthorities());

        return member.getId();
    }

    public Boolean saveRefreshToken(Long loginId, String refreshToken) {
        LOGGER.info("[AuthService] saveRefreshToken 시작");
        RefreshToken rt = RefreshToken.builder()
                .key(loginId.toString())
                .value(refreshToken)
                .build();
        refreshTokenRepository.save(rt);

        LOGGER.info("[AuthService] saveRefreshToken 종료: rt.getKey() = {}", rt.getKey());
        LOGGER.info("[AuthService] saveRefreshToken 종료: rt.getValue() = {}", rt.getValue());
        return true;
    }


    public Member inquire(Long memberId) {
        LOGGER.info("[AuthService] inquire 시작");
        Boolean aBoolean = memberRepository.existsById(memberId);
        LOGGER.info("[AuthService] memberRepository.existsById(memberId) 결과 = {}", aBoolean);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(
                        () -> new CustomException(HttpStatus.UNAUTHORIZED, "일치하는 유저가 없습니다."));
        LOGGER.info("[AuthService] inquire 종료: member.getUsername() = {}", member.getUsername());
        LOGGER.info("[AuthService] inquire 종료: member.getName() = {}", member.getName());
        LOGGER.info("[AuthService] inquire 종료: member.getAuthorities() = {}", member.getAuthorities());
        return member;
    }
}
