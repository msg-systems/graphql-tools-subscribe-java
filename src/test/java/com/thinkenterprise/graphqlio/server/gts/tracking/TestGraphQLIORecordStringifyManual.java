package com.thinkenterprise.graphqlio.server.gts.tracking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.thinkenterprise.graphqlio.server.gts.tracking.GtsRecord;
import com.thinkenterprise.graphqlio.server.gts.tracking.GtsRecord.GtsArityType;
import com.thinkenterprise.graphqlio.server.gts.tracking.GtsRecord.GtsOperationType;
import com.thinkenterprise.graphqlio.server.gts.tracking.GtsRecord.GtsRecordBuilder;

@Tag("annotations")
@Tag("junit5")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestGraphQLIORecordStringifyManual {

	final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger();

	
//	// optional parameters 
//	private String srcType = null; 
//	private String srcId = null; 
//	private String srcAttr = null; 
//
//	/// required Parameters
//	private String op = ""; 	
//	private String arity = "";
//	private String dstType = "";
//	private String dstIds[] = null;
//	private String dstAttrs[] = null;
	
	@Test
	public void testStringify() {

		GtsRecordBuilder trb1 = new GtsRecordBuilder();
		
		GtsRecord record = GtsRecord.builder()
				.op(GtsOperationType.READ)
				.arity(GtsArityType.ONE)
				.dstType("dstType")
				.dstIds(new String[] {"dstId1", "dstId2"})
				.dstAttrs(new String[] {"dstAttr1", "dstAttr2"})
				.build();
		
		String strRecord = record.stringify();
//		log.info("strRecord =" + strRecord);
//		
//		boolean bMatchesRegExp = GraphQLIORecord.matchesPredefinedPattern(strRecord);
//		Assert.assertTrue(bMatchesRegExp);
		
		GtsRecord recordFromStringManual = GtsRecord.builder().stringified(strRecord).build();
		
		Assertions.assertTrue(recordFromStringManual != null && record != null && recordFromStringManual.equals(record));

	}	
}
