package org.zxp.funk.hopper.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;  
  

/**
 * Security的主要配置
 * @author 朱新培 zxp@wisoft.com.cn
 *
 */
@Configuration  
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {  
  
	@Value("${sys.login.username}")
	String username;
	@Value("${sys.login.password}")
	String password;
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
		    .antMatchers("/assets/**","/hopper-res/**","/LayR/**","/plugins/**","/version.json").permitAll()
		    .anyRequest().authenticated()
		    .and()
		    	.addFilterAt(myUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class).exceptionHandling()
		    	.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/hopper_login.html"))
            .and()
	            // 指定登录页面的请求路径
	            .formLogin().loginPage("/hopper_login.html")
	            // 登陆处理路径
	            .loginProcessingUrl("/login").permitAll()
	        .and()
	            // 退出请求的默认路径为logout，下面改为signout，
	            // 成功退出登录后的url可以用logoutSuccessUrl设置
	            .logout().logoutSuccessUrl("/hopper_login.html").permitAll()
            .and()               
            // 关闭csrf
            	.csrf().disable()
            	.headers()
            		.frameOptions().sameOrigin()//iframe同源允许
            		.httpStrictTransportSecurity().disable()
            .and()
				.sessionManagement().sessionFixation().changeSessionId()  
				.maximumSessions(1).expiredUrl("/hopper.html");  
	 }

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
	   /* auth
	        .inMemoryAuthentication()
	            .withUser("user").password("password").roles("USER");*/
	    
	    auth.userDetailsService(new UserDetailsService(){
	    	
	    	MyUserDetails user1 = new MyUserDetails(username, "系统管理员", "001", password);

			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				if("superadmin".equals(username))
					return user1;
				else
					throw new UsernameNotFoundException(username+"不存在！");
			}
	    	
	    });
	}
	
	
	@Bean
    public UsernamePasswordAuthenticationFilter myUsernamePasswordAuthenticationFilter() throws Exception {
        UsernamePasswordAuthenticationFilter myFilter = new UsernamePasswordAuthenticationFilter();
        myFilter.setAuthenticationManager(authenticationManagerBean());
        myFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        myFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
        //myFilter.setRememberMeServices(tokenBasedRememberMeServices());
        return myFilter;
    }
    
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new SimpleUrlAuthenticationSuccessHandler() {
			
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
			   request.getSession().setMaxInactiveInterval(100*60);//设置session过期时间
			   //String url = super.determineTargetUrl(request, response);
			   String url ="/hopper.html";
			   response.setCharacterEncoding("UTF-8");  
		       response.setContentType("application/json");  
		       response.getWriter().println("{\"success\":true,\"msg\":\"登录成功\",\"retObj\":\""+url+"\"}");  
			}
		};
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new SimpleUrlAuthenticationFailureHandler(){
        	
        	@Override
        	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        			AuthenticationException exception) throws IOException, ServletException {
        		response.setCharacterEncoding("UTF-8");  
     	       response.setContentType("application/json");  
     	       response.getWriter().println("{\"success\":false,\"msg\":\""+exception.getLocalizedMessage()+"\"}");
        	}
        	
        };
    }
   
}  
