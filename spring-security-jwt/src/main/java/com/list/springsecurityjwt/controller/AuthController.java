package com.list.springsecurityjwt.controller;

import com.list.springsecurityjwt.entity.Member;
import com.list.springsecurityjwt.security.jwt.MemberPrincipal;
import com.list.springsecurityjwt.service.AuthService;
import com.list.springsecurityjwt.service.JwtService;
import com.list.springsecurityjwt.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.list.springsecurityjwt.dto.MemberDto.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    private final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/register")
    public RegisterResponseDto register(@RequestBody @Valid RegisterRequestDto registerRequestDto) {
        Member member = authService.register(registerRequestDto);
        return new RegisterResponseDto(true, member.getName());
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody @Valid LoginDto loginDto) {
        Long loginId = authService.login(loginDto);
        String accessToken = jwtService.createAccessToken(loginId);
        String refreshToken = jwtService.createRefreshToken(loginId);
        authService.saveRefreshToken(loginId, refreshToken);
        return new LoginResponseDto(true, loginId,accessToken, refreshToken);
    }

    @GetMapping("/inquire/{memberId}")
    public MemberResponseDto inquire(@PathVariable @Valid Long memberId) {
        System.out.println("memberId = " + memberId);
        System.out.println("inquire [memberId] 대상으로 실행");
        return new MemberResponseDto(true, authService.inquire(memberId).getName());
    }


    @GetMapping("/inquire/me")
    public MemberResponseDto inquireMe(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        LOGGER.info("[AuthController] inquireMe 시작");
        LOGGER.info("[AuthController] userInfoPrincipal.getUsername() = {}", memberPrincipal.getUsername());
        LOGGER.info("[AuthController] userInfoPrincipal.getMember() = {}", memberPrincipal.getMember());
        LOGGER.info("[AuthController] userInfoPrincipal.getMember().getId() = {}", memberPrincipal.getMember().getId());

        System.out.println("inquire [본인] 대상으로 실행");
        Member authMember = authService.inquire(memberPrincipal.getMember().getId());
        UserDetails userDetails = userDetailsService.loadUserByUsername(memberPrincipal.getMember().getUsername());
        return new MemberResponseDto(true, userDetails.getUsername());
    }

    @GetMapping("/test/non")
    public TestResponseDto testAuthNon(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        return new TestResponseDto(true, memberPrincipal.getMember().getName());
    }

    @GetMapping("/test/user")
    @Secured("ROLE_USER")
    public TestResponseDto testAuthUser(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        return new TestResponseDto(true, memberPrincipal.getMember().getName());
    }

    @PutMapping("/password")
    public MemberResponseDto updatePassword(@AuthenticationPrincipal MemberPrincipal memberPrincipal,@RequestBody UpdatePasswordRequestDto updatePasswordRequestDto) {
        LOGGER.info("[AuthController] userInfoPrincipal.getMember().getId() = {}", memberPrincipal.getMember().getId());
        Member member = authService.updatePassword(memberPrincipal.getMember().getId(), updatePasswordRequestDto);
        return new MemberResponseDto(true, member.getName());
    }
}
