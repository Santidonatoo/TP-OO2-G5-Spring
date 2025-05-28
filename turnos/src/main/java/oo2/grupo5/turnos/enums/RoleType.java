package oo2.grupo5.turnos.enums;

public enum RoleType {
	
	ADMIN,
	EMPLOYEE,
	CLIENT;
	
	public String getPrefixedName() {
		return "ROLE_" + this.name();
	}
}
