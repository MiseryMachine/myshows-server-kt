package com.rjs.myshows.server.config.security

import org.apache.commons.lang3.StringUtils
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableWebSecurity
class WebSecurityConfig(private val userDetailsService: AppUserDetailsService): WebSecurityConfigurerAdapter() {
    override fun configure(web: WebSecurity?) {
        web?.ignoring()?.antMatchers("/resources/**")
    }

    @Bean
    fun encoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userDetailsService)
        authProvider.setPasswordEncoder(encoder())

        return authProvider
    }

    fun encode(value: String): String = if (StringUtils.isNotBlank(value)) encoder().encode(value) else ""

    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(authenticationProvider())
    }

    override fun configure(http: HttpSecurity?) {
        http
            ?.csrf()?.disable()
            ?.authorizeRequests()
                ?.antMatchers("/admin/**")?.hasRole("ADMIN")
//                ?.antMatchers("/ws/**")?.hasRole("REST_USER")
                ?.antMatchers("/shows/**")?.hasRole("USER")
                ?.antMatchers("/ws/**", "/", "/home", "/user/signup", "/user/registration", "/webjars/**", "/css/**", "/img/**", "/js/**", "/datatables/**")?.permitAll()
                ?.anyRequest()?.authenticated()
            ?.and()
            ?.formLogin()
                ?.loginPage("/user/login")?.permitAll()
            ?.and()
            ?.logout()
                ?.deleteCookies("JSESSIONID")
                ?.clearAuthentication(true)
                ?.invalidateHttpSession(true)?.permitAll()
                ?.logoutSuccessUrl("/home")
    }
}