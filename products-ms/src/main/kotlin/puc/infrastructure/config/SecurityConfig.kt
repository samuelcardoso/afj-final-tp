package puc.infrastructure.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import puc.infrastructure.filters.AuthenticationFilter


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
class SecurityConfig() {

    @Bean
    fun secureFilterChain(http: HttpSecurity, authenticationFilter: AuthenticationFilter): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { it.anyRequest().authenticated() }
            .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}