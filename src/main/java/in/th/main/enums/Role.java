package in.th.main.enums;

import java.util.Set;

public enum Role {

	ADMIN(Set.of(Permissions.DELETE,Permissions.WRITE,Permissions.READ)),
	USER(Set.of(Permissions.READ));
	
	private final Set<Permissions> authorities;

	Role(Set<Permissions> authorities) {
		this.authorities = authorities;
	}
	
	public Set<Permissions> getAuthorities(){
		return authorities;
	}
}
