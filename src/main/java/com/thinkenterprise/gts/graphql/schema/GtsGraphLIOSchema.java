package com.thinkenterprise.gts.graphql.schema;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class GtsGraphLIOSchema {
	
	private static String GtsGraphQLIOSchemaFilePath = "schema-graphqlio.graphql";
	
	private GtsGraphLIOSchema() {}
	
	public static Resource[] getSchemaResources() {
		Resource[] resources = null;

		try {
			PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			resources = resolver.getResources("classpath*:" + GtsGraphQLIOSchemaFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return resources;
	}
		
}
