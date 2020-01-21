# Review Changes 

1. Change the Name in ```graphql-tools-subscribe-java``` 
2. Change the POM File Parent to BOM 
3. Maven POM Reneame the Project 
3. Maven POM Delete some unimportant dependencies 
4. Maven POM Add Spring Repository URLs 
5. Maven POM Delete GraphQL Toools & Kotlin replace with GraphQL Tools 5.6 / 2.2 



# Review Open Issues 
1. Short Project Description
2. Change the Base Package Name in com.thinkenterprise.gts 
```
-> done! (tku)
```
3. TestGraphQLIORecordStringifyRegExp Pure Java JUnit Test 5 Test & Naming Conventions & Discussion  
4. TestGraphQLIORecordStringifyManual Pure Java JUnit Test 5 Test & Naming Conventions & Discussion  
5. TestRedisKVPServiceTest Pure Java JUnit Test 5 Test & Naming Conventions & Discussion 
6. TestGraphQLIOEvaluation Pure Java JUnit Test 5 Test & Naming Conventions & Discussion 
7. TestPureUuidGenerator should be tested in the UUID Project 
```
-> no! this test includes GtsScope with internal builder and method generateUUID.
-> pure-uuid-java is used, but this is no pure-uuid-java test i think. (tku)
```
8. TestGraphQLIOEvaluation Problem with Do you want the application "redis-server-2.8.19.app" to accept incoming network connections?
9. Test Header Description of the most tests are not correct copy paste failures 
10. TestRedisKVPServiceTest Poblem with Do you want the application "redis-server-2.8.19.app" to accept incoming network connections?
11. RedisProperties Should be implemented as configuration properties 
12. Class Header Description of the most Classes are not correct copy paste failures 
13. GtsGraphQLRedisService Scope Singleton is not to define its default?! 
14. GtsEvaluation should be documented. Because the class is complex an very important 
15. GtsContext Do we need that class!? 
16. Motivation of EnableGraphQLIOGtsLibraryModule GraphQLIOLibraryGtsConfiguration is not clear 
17. Replace the Spring Boot Redis Support with a pure Java Implementation (lettuce, jedis) 


## Questions 

1. How does GtsKeyValueStore works 
2. 


