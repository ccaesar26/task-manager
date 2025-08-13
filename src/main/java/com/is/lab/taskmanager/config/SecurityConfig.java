package com.is.lab.taskmanager.config;

import com.is.lab.taskmanager.service.JpaUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JpaUserDetailsService jpaUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Permitem accesul public la resursele statice (CSS, JS) și la pagina de login
                        .requestMatchers("/css/**", "/js/**", "/login").permitAll()
                        // Orice altă cerere necesită autentificare
                        .anyRequest().authenticated()
                )
                // Configurăm formularul de login
                .formLogin(form -> form
                        .loginPage("/login") // URL-ul paginii de login
                        .loginProcessingUrl("/login") // URL-ul unde se trimite formularul pentru procesare
                        .defaultSuccessUrl("/projects", true) // Pagina de destinație după un login reușit
                        .permitAll() // Toată lumea are voie să vadă pagina de login
                )
                // Configurăm funcționalitatea de logout
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout") // URL-ul unde ajunge utilizatorul după logout
                        .permitAll()
                )
                .userDetailsService(jpaUserDetailsService); // Specificăm serviciul nostru pentru încărcarea utilizatorilor

        return http.build();
    }
}
