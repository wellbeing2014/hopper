package org.zxp.funk.hopper.config;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class MyUserDetails implements UserDetails{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2213332179171208972L;
	private String username;
	private String realname;
	private String userid;
	private String password;
	private Collection<? extends GrantedAuthority> authorities;
	
	public MyUserDetails(String username,String realname,String userid,String password){
		super();
		this.userid = userid;  
        this.username = username;  
        this.password = password; 
        this.realname = realname;
	}
	
	public MyUserDetails(String username,String realname,String userid,String password, 
			Collection<? extends GrantedAuthority> authorities){
		super();
		this.userid = userid;  
        this.username = username;  
        this.password = password; 
        this.realname = realname;
        this.authorities = authorities;
	}
	
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public String getRealname(){
		return realname;
	}
	
	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override  
    public String toString() {  
        return "MyUserDetails [userId=" + userid + ", username=" + username  
                + ", password=" + password + ", authorities=" + authorities + "]";  
    }  
}
