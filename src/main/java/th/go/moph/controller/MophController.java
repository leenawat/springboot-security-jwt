package th.go.moph.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.svlada.security.auth.jwt.extractor.TokenExtractor;
import com.svlada.security.config.JwtSettings;
import com.svlada.security.config.WebSecurityConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

@RestController
public class MophController {

	@Autowired
	private TokenExtractor tokenExtractor;

	@Autowired
	private JwtSettings jwtSettings;

	@GetMapping("/api/moph/admino_nly/00")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String api_adminonly() {
		// เพิ่ม ลบ แก้ไข user ได้ทุกคน
		return "this is api admino_nly 00";
	}

	@GetMapping("/api/moph/preminum_member_only/00")
	@PreAuthorize("hasAuthority('ROLE_PREMIUM_MEMBER')")
	public String api_preminum_member_only() {
		// เข้าถึงบางอย่างที่พิเศษกว่า member ทั่วไป
		return "this is api api_preminum_member_only 00";
	}

	@GetMapping("/api/moph/member_or_premium_member/00")
	@PreAuthorize("hasAuthority('ROLE_PREMIUM_MEMBER') or hasAuthority('ROLE_MEMBER')")
	public String api_member_or_premium_member() {
		// แก้ไขข้อมูลส่วนตัว รูปภาพส่วนตัว
		// แก้ไข user ของตัวเองเท่านั้น
		return "this is api member_or_premium_member 00";
	}

	@GetMapping("/api/moph/api_admin_or_member_or_preminum_member/00")
	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_PREMIUM_MEMBER') or hasAuthority('ROLE_MEMBER')")
	public String api_admin_or_member_or_preminum_member() {
		// ตัวอย่างเช่น reset password
		return "this is api api_admin_or_member_or_preminum_member";
	}

	@GetMapping("/api/moph/test/getheader/00")
	public String api_public_00(@RequestHeader(value = WebSecurityConfig.JWT_TOKEN_HEADER_PARAM) String tokenPayload) {
		String result = "";
		String token = tokenExtractor.extract(tokenPayload);
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(jwtSettings.getTokenSigningKey()).parseClaimsJws(token);
			Objects.toString(claims.getBody().get("sub"));
			result = Objects.toString(claims.getBody().get("sub"));
		} catch (Exception ex) {
			ex.printStackTrace();
			result = "fail";
		}
		return result;
	}

	@PostMapping("/api/moph/test/checkjwt_with_mail")
	public String api_public_00(@RequestHeader(value = WebSecurityConfig.JWT_TOKEN_HEADER_PARAM) String tokenPayload,
			@RequestBody UserForUpdate user) {
		String result = "";
		String token = tokenExtractor.extract(tokenPayload);
		try {
			Claims claims = Jwts.parser().setSigningKey(jwtSettings.getTokenSigningKey()).parseClaimsJws(token).getBody();
			Objects.toString(claims.get("sub"));
			if (user.username.equals(Objects.toString(claims.get("sub")))) {
				result = "change password success";
			} else {
				result = "email not match";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			result = "email not match or not premit";
		}
		return result;
	}

}

class UserForUpdate {
	String username;
	String oldPassword;
	String newPassword;

	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}
