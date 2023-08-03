package sparta.blogfinal.user.entity;

public enum UserRoleEnum {
	USER(Authority.USER),  // 사용자 권한
	ADMIN(Authority.ADMIN);  // 관리자 권한

	private final String authority;

	UserRoleEnum(String authority) {
		this.authority = authority;
	}

	public String getAuthority() {
		return this.authority;
	}

	public static class Authority {
		// 권한 앞에 "ROLE_"을 붙이는 것이 규칙이다!
		public static final String USER = "ROLE_USER";
		public static final String ADMIN = "ROLE_ADMIN";
	}
}
