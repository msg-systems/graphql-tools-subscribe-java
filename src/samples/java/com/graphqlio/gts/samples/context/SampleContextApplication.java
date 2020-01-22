package com.graphqlio.gts.samples.context;

import com.graphqlio.gts.tracking.GtsRecord;
import com.graphqlio.gts.tracking.GtsRecord.GtsArityType;
import com.graphqlio.gts.tracking.GtsRecord.GtsOperationType;

public class SampleContextApplication {

	public static void main(String[] args) {
		GtsRecord record = GtsRecord.builder().op(GtsOperationType.READ).arity(GtsArityType.ONE).dstType("dstType")
				.dstIds(new String[] { "dstId1", "dstId2" }).dstAttrs(new String[] { "dstAttr1", "dstAttr2" }).build();

		String strRecord = record.stringify();

		GtsRecord recordFromString = GtsRecord.builder().stringified(strRecord).build();

		System.out.println("record           = " + record);
		System.out.println("strRecord        = " + strRecord);
		System.out.println("recordFromString = " + recordFromString);
	}

}
