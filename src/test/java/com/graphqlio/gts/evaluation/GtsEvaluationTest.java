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

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.graphqlio.gts.keyvaluestore.GtsKeyValueStore;
import com.graphqlio.gts.tracking.GtsRecord;
import com.graphqlio.gts.tracking.GtsScope;
import com.graphqlio.gts.tracking.GtsScopeState;

/**
 * class for testing evaluation
 *
 * @author Michael Schäfer
 * @author Dr. Edgar Müller
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GtsEvaluationTest {

  final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger();

  private GtsKeyValueStore keyval = new GtsKeyValueStore();
  private GtsEvaluation graphQLIOEvaluation = new GtsEvaluation(keyval);

  //// test data
  final String strRecordQuerySid1 =
      "read(many)->item#{id1,id2,id3,id4,id5}.{id,name,address,email}";
  final String strRecordQuerySid2 = "read(many)->item#{id4,id5,id6,id7}.{id,name}";
  final String strRecordQuerySid3 = "card#id1.items->read(one)->item#{id1}.{id,name}";

  final String strRecordMutationUpdateItemInQuerySid1 = "update(one)->item#{id3}.{email}";
  final String strRecordMutationDeleteItemInQuerySid1 = "delete(one)->item#{id2}.{*}";
  final String strRecordMutationUpdateOtherItem = "update(one)->item#{id10}.{email}";
  final String strRecordMutationUpdateOtherAttribute = "update(one)->item#{id3}.{telephone}";

  final String strRecordMutationUpdateCardItems = "update(one)->card#{id1}.{items}";

  final GtsRecord recodRecordQuerySid1 =
      GtsRecord.builder().stringified(strRecordQuerySid1).build();
  final GtsRecord recodRecordQuerySid2 =
      GtsRecord.builder().stringified(strRecordQuerySid2).build();
  final GtsRecord recodRecordQuerySid3 =
      GtsRecord.builder().stringified(strRecordQuerySid3).build();

  final GtsRecord recordMutationUpdateItemInQuerySid1 =
      GtsRecord.builder().stringified(strRecordMutationUpdateItemInQuerySid1).build();
  final GtsRecord recordMutationDeleteItemInQuerySid1 =
      GtsRecord.builder().stringified(strRecordMutationDeleteItemInQuerySid1).build();
  final GtsRecord recordMutationUpdateOtherItem =
      GtsRecord.builder().stringified(strRecordMutationUpdateOtherItem).build();
  final GtsRecord recordMutationUpdateOtherAttribute =
      GtsRecord.builder().stringified(strRecordMutationUpdateOtherAttribute).build();
  final GtsRecord recordMutationUpdateCardItems =
      GtsRecord.builder().stringified(strRecordMutationUpdateCardItems).build();

  GtsScope scopeSid1Cid1 =
      GtsScope.builder()
          .withScopeId("Sid1")
          .withConnectionId("Cid1")
          .withQuery(strRecordQuerySid1)
          .withState(GtsScopeState.SUBSCRIBED)
          .build();
  GtsScope scopeSid1Cid2 =
      GtsScope.builder()
          .withScopeId("Sid1")
          .withConnectionId("Cid2")
          .withQuery(strRecordQuerySid1)
          .withState(GtsScopeState.SUBSCRIBED)
          .build();
  GtsScope scopeSid2Cid1 =
      GtsScope.builder()
          .withScopeId("Sid2")
          .withConnectionId("Cid1")
          .withQuery(strRecordQuerySid2)
          .withState(GtsScopeState.SUBSCRIBED)
          .build();
  GtsScope scopeSid2Cid2 =
      GtsScope.builder()
          .withScopeId("Sid2")
          .withConnectionId("Cid2")
          .withQuery(strRecordQuerySid2)
          .withState(GtsScopeState.SUBSCRIBED)
          .build();
  GtsScope scopeSid3Cid1 =
      GtsScope.builder()
          .withScopeId("Sid3")
          .withConnectionId("Cid1")
          .withQuery(strRecordQuerySid3)
          .withState(GtsScopeState.SUBSCRIBED)
          .build();

  @Test
  public void testMutationUpdateItemOutdatesScopeUseCase1() throws IOException {

    /*
     * CASE 1: modified entity (of arbitrary direct access) old/query:
     * [*#{*}.*->]read(*)->Item#{1}.{id,name} new/mutation:
     * [*#{*}.*->]update/delete(*)->Item#{1}.{name}
     */

    List<String> outdatedSids = null;

    /// need to add Query Records !!! withQuery adds query sent by client.
    scopeSid1Cid1.addRecord(recodRecordQuerySid1);
    scopeSid2Cid1.addRecord(recodRecordQuerySid2);

    //// update mutation in scope 1 outdates "Scope" sid1 but not sid2
    scopeSid1Cid1.addRecord(recordMutationUpdateItemInQuerySid1);
    //// update mutation in scope 2 outdates "Scope" sid1 but not sid2
    scopeSid2Cid1.addRecord(recordMutationUpdateItemInQuerySid1);

    List<String> records1 = scopeSid1Cid1.getStringifiedRecords();
    String[] scopeRecords1 = records1.toArray(new String[records1.size()]);
    keyval.putRecords(scopeSid1Cid1.getConnectionId(), scopeSid1Cid1.getScopeId(), scopeRecords1);

    List<String> records2 = scopeSid2Cid1.getStringifiedRecords();
    String[] scopeRecords2 = records2.toArray(new String[records2.size()]);
    keyval.putRecords(scopeSid2Cid1.getConnectionId(), scopeSid2Cid1.getScopeId(), scopeRecords2);

    outdatedSids = graphQLIOEvaluation.evaluateOutdatedSids(scopeSid1Cid1);
    Assertions.assertTrue(outdatedSids.size() == 1);
    Assertions.assertTrue(outdatedSids.contains("Sid1"));

    outdatedSids = graphQLIOEvaluation.evaluateOutdatedSids(scopeSid2Cid1);
    Assertions.assertTrue(outdatedSids.size() == 1);
    Assertions.assertTrue(outdatedSids.contains("Sid1"));
  }

  @Test
  public void testMutationUpdateCardOutdatesScopeUseCase2() throws IOException {

    /*
     * CASE 2: modified entity list (of relationship traversal) old/query
     * Card#1.items->read(*)->Item#{2}.{id,name} new/mutation:
     * [*#{*}.*->]update(*)->Card#{1}.{items}
     */

    List<String> outdatedSids = null;

    /// need to add Query Records !!! withQuery adds query sent by client.
    scopeSid1Cid1.addRecord(recodRecordQuerySid1);
    /// need to add Query Records !!! withQuery adds query sent by client.
    scopeSid3Cid1.addRecord(recodRecordQuerySid3);

    //// card update mutation in scope 1 outdates "Scope" sid3 (read card items)
    scopeSid1Cid1.addRecord(recordMutationUpdateCardItems);

    List<String> records1 = scopeSid1Cid1.getStringifiedRecords();
    String[] scopeRecords1 = records1.toArray(new String[records1.size()]);
    keyval.putRecords(scopeSid1Cid1.getConnectionId(), scopeSid1Cid1.getScopeId(), scopeRecords1);

    List<String> records3 = scopeSid3Cid1.getStringifiedRecords();
    String[] scopeRecords3 = records3.toArray(new String[records3.size()]);
    keyval.putRecords(scopeSid3Cid1.getConnectionId(), scopeSid3Cid1.getScopeId(), scopeRecords3);

    outdatedSids = graphQLIOEvaluation.evaluateOutdatedSids(scopeSid1Cid1);
    Assertions.assertTrue(outdatedSids.size() == 1);
    Assertions.assertTrue(outdatedSids.contains("Sid3"));
  }

  @Test
  public void testMutationDeleteItemOutdatesScopeUseCase3() throws IOException {

    /*
     * CASE 3: modified entity list (of direct query) old/query
     * [*#{*}.*->]read(many/all)->Item#{*}.{id,name} new/mutation:
     * [*#{*}.*->]create/update/delete(*)->Item#{*}.{name}
     *
     * "Read:Many", "Delete:Attr=*
     */

    List<String> outdatedSids = null;

    /// need to add Query Records !!! withQuery adds query sent by client.
    scopeSid1Cid1.addRecord(recodRecordQuerySid1);
    scopeSid2Cid1.addRecord(recodRecordQuerySid2);

    //// delete mutation in scope 1 outdates "Scope" sid1 and sid2
    scopeSid1Cid1.addRecord(recordMutationDeleteItemInQuerySid1);
    //// delete mutation in scope 2 outdates "Scope" sid1 and sid2
    scopeSid2Cid1.addRecord(recordMutationDeleteItemInQuerySid1);

    List<String> records1 = scopeSid1Cid1.getStringifiedRecords();
    String[] scopeRecords1 = records1.toArray(new String[records1.size()]);
    keyval.putRecords(scopeSid1Cid1.getConnectionId(), scopeSid1Cid1.getScopeId(), scopeRecords1);

    List<String> records2 = scopeSid2Cid1.getStringifiedRecords();
    String[] scopeRecords2 = records2.toArray(new String[records2.size()]);
    keyval.putRecords(scopeSid2Cid1.getConnectionId(), scopeSid2Cid1.getScopeId(), scopeRecords2);

    outdatedSids = graphQLIOEvaluation.evaluateOutdatedSids(scopeSid1Cid1);
    Assertions.assertTrue(outdatedSids.size() == 2);
    Assertions.assertTrue(outdatedSids.contains("Sid1"));
    Assertions.assertTrue(outdatedSids.contains("Sid2"));

    outdatedSids = graphQLIOEvaluation.evaluateOutdatedSids(scopeSid2Cid1);
    Assertions.assertTrue(outdatedSids.size() == 2);
    Assertions.assertTrue(outdatedSids.contains("Sid1"));
    Assertions.assertTrue(outdatedSids.contains("Sid2"));
  }

  @Test
  public void testMutationUpdateItemOutdatesScopeUseCase3() throws IOException {

    /*
     * CASE 3: modified entity list (of direct query) old/query
     * [*#{*}.*->]read(many/all)->Item#{*}.{id,name} new/mutation:
     * [*#{*}.*->]create/update/delete(*)->Item#{*}.{name}
     *
     * "Read:Many", "Update (other item, but matching attribute "email" to query in
     * Sid1) ==> outdates Sid1 "Read:Many",
     * "Update (other item, and non matching attribute") ==> does not outdate
     * queries in sid1 or sid2
     */
    List<String> outdatedSids = null;

    /// need to add Query Records !!! withQuery adds query sent by client.
    scopeSid1Cid1.addRecord(recodRecordQuerySid1);
    scopeSid2Cid1.addRecord(recodRecordQuerySid2);

    // updates item id10 which is not in returned list, but "read many
    scopeSid1Cid1.addRecord(recordMutationUpdateOtherItem);

    // updates item id10 which is not in returned list, but "read many
    scopeSid2Cid1.addRecord(recordMutationUpdateOtherItem);

    List<String> records1 = scopeSid1Cid1.getStringifiedRecords();
    String[] scopeRecords1 = records1.toArray(new String[records1.size()]);
    keyval.putRecords(scopeSid1Cid1.getConnectionId(), scopeSid1Cid1.getScopeId(), scopeRecords1);

    List<String> records2 = scopeSid2Cid1.getStringifiedRecords();
    String[] scopeRecords2 = records2.toArray(new String[records2.size()]);
    keyval.putRecords(scopeSid2Cid1.getConnectionId(), scopeSid2Cid1.getScopeId(), scopeRecords2);

    outdatedSids = graphQLIOEvaluation.evaluateOutdatedSids(scopeSid1Cid1);
    Assertions.assertTrue(outdatedSids.size() == 1);
    Assertions.assertTrue(outdatedSids.contains("Sid1"));

    outdatedSids = graphQLIOEvaluation.evaluateOutdatedSids(scopeSid2Cid1);
    Assertions.assertTrue(outdatedSids.size() == 1);
    Assertions.assertTrue(outdatedSids.contains("Sid1"));
  }
}
