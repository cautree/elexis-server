package info.elexis.server.core.product.web.test;

import static io.restassured.RestAssured.given;

import org.junit.Before;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Header;

public class ServicesServerPathTest extends AbstractRestTest {
	final String SERVICE_BASE = BASE + "/services/server/";

	private Header APIKEY_HEADER = new Header("X-Api-Key", "a81d86ba-802f-4f9c-a41d-e47b7aa11049");
	
	@Before
	public void before() {
		RestAssured.baseURI = BASE;
		RestAssured.basePath = "/services/server/";
	}

	@Test
	public void testUnauthenticatedStatus() {
		given().when().get("status").then().statusCode(403);
	}
	
	@Test
	public void testApiKeyAuthenticatedStatus() {
		given().when().header(APIKEY_HEADER).get("status").then().statusCode(200);
	}

	// @Test
	// public void testUptimeStatus() {
	// given().when().get(SERVICE_BASE).then().statusCode(200).contentType(ContentType.TEXT)
	// .body(startsWith("Uptime"));
	// }
	//
	// @Test
	// public void testUnauthorizedHalt() {
	// given().when().get(BASE + HALT).then().statusCode(403);
	// }
	//
	// @Test
	// public void testUnauthorizedRestart() {
	// given().when().get(BASE + RESTART).then().statusCode(403);
	// }
}
