package in.th.main.principals;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import in.th.main.entities.User;

public class UserPrincipal implements UserDetails{

	private static final long serialVersionUID = 1L;
	
	private User user;
	
	public UserPrincipal(User user) {
		this.user = user;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<SimpleGrantedAuthority> authority = new HashSet<>();
		authority.add(new SimpleGrantedAuthority("ROLE_"+user.getRole().name()));
		Set<SimpleGrantedAuthority> permissions = user
				.getRole()
				.getAuthorities()
				.stream()
				.map(permission -> new SimpleGrantedAuthority(permission.name()))
				.collect(Collectors.toSet());
		authority.addAll(permissions);
		return authority;
	}
	

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}
	public String getName() {
		return user.getName();
	}

}
