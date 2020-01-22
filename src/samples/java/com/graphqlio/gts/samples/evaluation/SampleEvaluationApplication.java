package com.graphqlio.gts.samples.evaluation;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.graphqlio.gts.EnableGraphQLIOGtsLibraryModule;
import com.graphqlio.gts.evaluation.GtsEvaluation;
import com.graphqlio.gts.keyvaluestore.GtsKeyValueStore;
import com.graphqlio.gts.tracking.GtsRecord;
import com.graphqlio.gts.tracking.GtsScope;
import com.graphqlio.gts.tracking.GtsScopeState;

@SpringBootApplication
@EnableGraphQLIOGtsLibraryModule
public class SampleEvaluationApplication {

	public static void main(String[] args) throws InterruptedException {
		Properties properties = new Properties();
		properties.put("graphqlio.toolssubscribe.useEmbeddedRedis", "true");
		properties.put("spring.redis.host", "localhost");
		properties.put("spring.redis.port", "26379");

		SpringApplication application = new SpringApplication(SampleEvaluationApplication.class);
		application.setDefaultProperties(properties);
		ConfigurableApplicationContext cax = application.run(args);

		cax.getBean(SampleEvaluationApplication.class).runSample();

		System.exit(0);
	}

	@Autowired
	private GtsKeyValueStore keyval;

	@Autowired
	private GtsEvaluation graphQLIOEvaluation;

	public void runSample() {
		try {
			keyval.start();

			String strRecordQuerySid1 = "read(many)->item#{id1,id2,id3,id4,id5}.{id,name,address,email}";
			String strRecordMutationUpdateItemInQuerySid1 = "update(one)->item#{id3}.{email}";

			GtsScope scopeSid1Cid1 = GtsScope.builder().withScopeId("Sid1").withConnectionId("Cid1")
					.withQuery(strRecordQuerySid1).withState(GtsScopeState.SUBSCRIBED).build();

			GtsRecord recordRecordQuerySid1 = GtsRecord.builder().stringified(strRecordQuerySid1).build();
			GtsRecord recordMutationUpdateItemInQuerySid1 = GtsRecord.builder()
					.stringified(strRecordMutationUpdateItemInQuerySid1).build();

			scopeSid1Cid1.addRecord(recordRecordQuerySid1);
			scopeSid1Cid1.addRecord(recordMutationUpdateItemInQuerySid1);

			List<String> records1 = scopeSid1Cid1.getStringifiedRecords();
			String[] scopeRecords1 = records1.toArray(new String[records1.size()]);
			keyval.putRecords(scopeSid1Cid1.getConnectionId(), scopeSid1Cid1.getScopeId(), scopeRecords1);

			List<String> outdatedSids = graphQLIOEvaluation.evaluateOutdatedSids(scopeSid1Cid1);

			System.out.println();
			System.out.println("===== Ergebnisse ====");
			System.out.println("strRecordQuerySid1    = " + strRecordQuerySid1);
			System.out.println("strRecordMutationUpdateItemInQuerySid1 = " + strRecordMutationUpdateItemInQuerySid1);
			System.out.println("scopeSid1Cid1         = " + scopeSid1Cid1);
			System.out.println("recordRecordQuerySid1 = " + recordRecordQuerySid1);
			System.out.println("recordMutationUpdateItemInQuerySid1 = " + recordMutationUpdateItemInQuerySid1);
			System.out.println("outdatedSids          = " + outdatedSids);
			System.out.println("outdatedSids          = " + Arrays.toString(outdatedSids.toArray()));
			System.out.println("===== Ende ==========");
			System.out.println();

			keyval.stop();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
