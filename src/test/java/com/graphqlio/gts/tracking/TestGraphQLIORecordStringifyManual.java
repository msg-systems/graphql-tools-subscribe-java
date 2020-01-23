/*******************************************************************************
 * *
 * **  Design and Development by msg Applied Technology Research
 * **  Copyright (c) 2019-2020 msg systems ag (http://www.msg-systems.com/)
 * **  All Rights Reserved.
 * ** 
 * **  Permission is hereby granted, free of charge, to any person obtaining
 * **  a copy of this software and associated documentation files (the
 * **  "Software"), to deal in the Software without restriction, including
 * **  without limitation the rights to use, copy, modify, merge, publish,
 * **  distribute, sublicense, and/or sell copies of the Software, and to
 * **  permit persons to whom the Software is furnished to do so, subject to
 * **  the following conditions:
 * **
 * **  The above copyright notice and this permission notice shall be included
 * **  in all copies or substantial portions of the Software.
 * **
 * **  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * **  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * **  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * **  IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * **  CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * **  TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * **  SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * *
 ******************************************************************************/
package com.graphqlio.gts.tracking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.graphqlio.gts.tracking.GtsRecord.GtsArityType;
import com.graphqlio.gts.tracking.GtsRecord.GtsOperationType;
import com.graphqlio.gts.tracking.GtsRecord.GtsRecordBuilder;

/**
 * class for testing record stringify manual
 *
 * @author Michael Schäfer
 * @author Dr. Edgar Müller
 */

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

		GtsRecord record = GtsRecord.builder().op(GtsOperationType.READ).arity(GtsArityType.ONE).dstType("dstType")
				.dstIds(new String[] { "dstId1", "dstId2" }).dstAttrs(new String[] { "dstAttr1", "dstAttr2" }).build();

		String strRecord = record.stringify();
//		log.info("strRecord =" + strRecord);
//		
//		boolean bMatchesRegExp = GraphQLIORecord.matchesPredefinedPattern(strRecord);
//		Assert.assertTrue(bMatchesRegExp);

		GtsRecord recordFromStringManual = GtsRecord.builder().stringified(strRecord).build();

		Assertions
				.assertTrue(recordFromStringManual != null && record != null && recordFromStringManual.equals(record));

	}
}
