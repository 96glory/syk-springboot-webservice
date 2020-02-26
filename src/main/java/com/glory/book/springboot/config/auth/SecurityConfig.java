package com.glory.book.springboot.config.auth;

import com.glory.book.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
                // h2-console 화면을 사용하기 위해 해당 옵션들을 disable
        http    .csrf().disable().headers().frameOptions().disable()

                .and()
                    // URL별 권한 관리를 설정하는 옵션의 시작점
                    .authorizeRequests()
                        // antMatchers : 권한 관리 대상을 지정하는 옵션
                        // permitAll : 모두가 조회할 수 있음.
                        .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll()
                        .antMatchers("/api/v1/**").hasRole(Role.USER.name())
                        // hasRole : 해당 권한을 가진 사람만 조회할 수 있음
                        // anyRequest : 설정된 값들 이외 나머지 URL을 나타냄
                        // authenticated : 나머지 URL을 모두 인증된 사용자들에게만 허용함.
                        .anyRequest().authenticated()
                .and()
                    .logout().logoutSuccessUrl("/")
                .and()
                    // userInfoEndpoint : 로그인 성공 이후 사용자 정보를 가져올 떄의 설정
                    // userService : 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록함
                    .oauth2Login().userInfoEndpoint().userService(customOAuth2UserService);
    }

}
