package ru.skillbox.socnetwork.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.skillbox.socnetwork.exception.CustomAuthenticationEntryPoint;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Getter
    @Value("${skillbox.app.server}")
    private String server;
    @Getter
    @Value("${skillbox.app.server.sexy}")
    private String serverSexy;
    @Getter
    @Value("${skillbox.app.server.mcsoft}")
    private String serverMcsoft;
    @Getter
    @Value("${skillbox.app.localhost}")
    private String localhost;


    private final JwtCsrfFilter jwtCsrfFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .configurationSource(corsConfigurationSource())
                .and()
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/auth/**", "/api/v1/account/register").permitAll()
                .antMatchers("/static/**", "/api/v1/platform/**").permitAll()
                .antMatchers("/api/v1/account/password/**", "/actuator/prometheus").permitAll()
                .antMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                .antMatchers("/ws/**").permitAll()
                .antMatchers("/app/**").permitAll()
                .antMatchers("/topic/**", "/queue/**", "/user/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
                .and()
                .addFilterBefore(jwtCsrfFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.applyPermitDefaultValues();
        configuration.setAllowedOrigins(getHosts());
        configuration.setAllowedMethods(List.of("OPTIONS", "DELETE", "POST", "GET", "PATCH", "PUT"));
        configuration.setExposedHeaders(List.of("Content-Type", "X-Requested-With", "accept", "Origin",
                "Access-Control-Request-Method", "Access-Control-Request-Headers", "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private List<String> getHosts() {
        List<String> links = new ArrayList<>();
        String http = "http://";
        String[] hosts = new String[]{getLocalhost(), getServer(), getServerSexy(), getServerMcsoft()};
        String[] ports = new String[]{":8080", ":8086", "", ":9091"};
        for (String host : hosts) {
            for (String port : ports) {
                links.add(http + host + port);
            }
        }
        return links;
    }
}
