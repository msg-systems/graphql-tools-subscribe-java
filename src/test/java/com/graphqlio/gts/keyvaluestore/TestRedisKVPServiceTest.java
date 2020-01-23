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
package com.graphqlio.gts.keyvaluestore;

import java.io.IOException;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Class used for testing the keyvaluestore
 *
 * @author Michael Schäfer
 * @author Dr. Edgar Müller
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestRedisKVPServiceTest {

	@Autowired
	GtsKeyValueStore kvp;

	@Test
	public void testKVP() throws IOException {

		Set<String> allKeys = null;

		kvp.start();

		/// reset kvp by deleting all keys
		kvp.getAllKeys().forEach((key) -> kvp.getRedisTemplate().delete(key));
		allKeys = kvp.getAllKeys();
		Assertions.assertEquals(0, allKeys.size());

		//// create Keys
		kvp.store("cid1", "sid1", "Value for <cid1,sid1>");
		allKeys = kvp.getAllKeys();
		kvp.store("cid1", "sid2", "Value for <cid1,sid2>");
		allKeys = kvp.getAllKeys();
		kvp.store("cid2", "sid3", "Value for <cid2,sid3>");
		allKeys = kvp.getAllKeys();
		kvp.store("cid2", "sid4", "Value for <cid2,sid4>");
		allKeys = kvp.getAllKeys();

		Assertions.assertTrue(kvp.hasKey("cid1", "sid1"));

		String value = kvp.get("cid1", "sid1");
		Assertions.assertTrue(value.equals("Value for <cid1,sid1>"));

		allKeys = kvp.getAllKeys();
		Assertions.assertEquals(4, allKeys.size());

		Set<String> allKeysCid = kvp.getAllKeysForConnection("cid1");
		Assertions.assertEquals(2, allKeysCid.size());

		Set<String> allKeysSid = kvp.getAllKeysForScope("sid1");
		Assertions.assertEquals(1, allKeysSid.size());

		kvp.delete("cid1", "sid1");
		kvp.delete("cid1", "sid2");
		kvp.delete("cid2", "sid3");
		kvp.delete("cid2", "sid4");

		kvp.stop();

	}

}
