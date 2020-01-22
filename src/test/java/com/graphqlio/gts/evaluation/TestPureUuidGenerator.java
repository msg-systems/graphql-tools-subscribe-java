package com.graphqlio.gts.evaluation;

import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.graphqlio.gts.tracking.GtsScope;
import com.graphqlio.gts.tracking.GtsScopeState;

@Tag("annotations")
@Tag("junit5")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestPureUuidGenerator {

	public TestPureUuidGenerator() {
		// TODO Auto-generated constructor stub
	}
	
	/// helper function
	private UUID isValidUUID(String uuidString) {
		UUID resultUUID = null;
		try {
			resultUUID = UUID.fromString(uuidString);   //throws exception if string does not represent a valid UUID 
		}
		catch( IllegalArgumentException e) {
			resultUUID = null;
		}						
		return resultUUID;	
	}
	

	final String strRecordQuerySid1 = "read(many)->item#{id1,id2,id3,id4,id5}.{id,name,address,email}";
	
	
	
	@Test
	public void testCreateScopeandScopeId() {
	
		GtsScope scope =
				GtsScope.builder().withQuery(strRecordQuerySid1).build();
		
		String uuid = scope.getScopeId();
		
		Assertions.assertTrue(isValidUUID(uuid) != null);		
		
	}
	
	
}
