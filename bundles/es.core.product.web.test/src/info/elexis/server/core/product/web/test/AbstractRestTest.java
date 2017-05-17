package info.elexis.server.core.product.web.test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.specification.ResponseSpecification;

public abstract class AbstractRestTest {

	public static final String BASE = "http://localhost:8380";
	public static final String ANONYMOUS = "anonymous";

	public static final ResponseSpecification LOGIN_PAGE_SPEC;

	static {
		ResponseSpecBuilder builder = new ResponseSpecBuilder();
		builder.expectStatusCode(200);
		builder.expectContentType(ContentType.HTML);
		builder.expectBody(containsString("948b1b1e-109e-11e7-93ae-92361f002671"));
		// TODO use url in addition
		LOGIN_PAGE_SPEC = builder.build();
	}

	public void assertIdentity(Cookie cookie, String userId) {
		if (cookie != null) {
			given().cookie(cookie).when().get(BASE + "/whoami.jsp").then().statusCode(200).contentType(ContentType.HTML)
					.body(is(userId));
		} else {
			given().when().get(BASE + "/whoami.jsp").then().statusCode(200).contentType(ContentType.HTML)
					.body(is(userId));
		}
	}

}
