package info.elexis.server.core.product.web.test;

import static io.restassured.RestAssured.given;

import org.junit.Test;

public class FhirResourceTest extends AbstractRestTest {
//	http://localhost:8380/fhir/Patient?name=TestPatient
	
	public static final String FHIR_BASE = BASE+"/fhir/";	
	
	@Test
	public void testUnauthenticatedBlock() {
		assertIdentity(null, ANONYMOUS);
		
		given().when().get(FHIR_BASE).then().spec(LOGIN_PAGE_SPEC);
	}
	
}
