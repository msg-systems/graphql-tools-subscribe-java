/**
 * *****************************************************************************
 *
 * <p>Design and Development by msg Applied Technology Research Copyright (c) 2019-2020 msg systems
 * ag (http://www.msg-systems.com/) All Rights Reserved.
 *
 * <p>Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * <p>The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * <p>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * <p>****************************************************************************
 */
package com.graphqlio.gts.evaluation;

import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.graphqlio.gts.tracking.GtsScope;

/**
 * class for testing pure uuid generator
 *
 * @author Michael Schäfer
 * @author Dr. Edgar Müller
 */
public class PureUuidGeneratorTest {

  // helper function
  private UUID isValidUUID(String uuidString) {
    UUID resultUUID = null;
    try {
      resultUUID =
          UUID.fromString(uuidString); // throws exception if string does not represent a valid UUID
    } catch (IllegalArgumentException e) {
      resultUUID = null;
    }
    return resultUUID;
  }

  final String strRecordQuerySid1 =
      "read(many)->item#{id1,id2,id3,id4,id5}.{id,name,address,email}";

  @Test
  public void whenGetScopeIdIsCalledThenOutputIsCorrect() {

    GtsScope scope = GtsScope.builder().withQuery(strRecordQuerySid1).build();

    String uuid = scope.getScopeId();

    Assertions.assertTrue(isValidUUID(uuid) != null);
  }
}
