package info.elexis.server.core.product.web.test;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import io.restassured.http.Cookie;
import io.restassured.response.Response;

public class RootResourceTest extends AbstractRestTest {

	final String AUTH_BASE = BASE;

	@Test
	public void testLoginAvailable() {
		given().when().get(AUTH_BASE + "/login.jsp").then().statusCode(200);
	}

	@Test
	public void testLoginFails() {
		assertIdentity(null, ANONYMOUS);
		Response response = given().formParam("username", "esadmin").queryParam("password", "passr0d").when()
				.post(AUTH_BASE + "/login.jsp");
		Cookie jSessionId = response.getDetailedCookie("JSESSIONID");
		assertNull(jSessionId);
		// not valid, session is created on first access!
		
		assertIdentity(null, ANONYMOUS);
	}

	@Test
	public void testLoginLogout() {
		Response response = given().formParam("username", "esadmin").queryParam("password", "password").when()
				.post(AUTH_BASE + "/login.jsp");
		response.then().log().all().statusCode(302);
		Cookie jSessionId = response.getDetailedCookie("JSESSIONID");
		assertNotNull(jSessionId);
		assertEquals("/", jSessionId.getPath());

		assertIdentity(jSessionId, "esadmin");

		given().cookie(jSessionId).when().get(AUTH_BASE + "/logout.jsp").then().statusCode(200);

		assertIdentity(null, ANONYMOUS);
	}

}
