package com.lab.lib.enumerated;

public enum SystemRole {
	ADMIN,
	MANAGER,
	EMPLOYEE;
	public static SystemRole fromStringToEnum(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Input không được null");
        }
        for (SystemRole role : SystemRole.values()) {
            if (role.name().equalsIgnoreCase(input)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy enum phù hợp với: " + input);
    }
}
