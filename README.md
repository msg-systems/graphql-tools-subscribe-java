# About
graphql-tools-subscribe-java is the **Java** implementation of the **JavaScript** graphql-tools-subscribe reference implementation. 
The documentation of the reference implementation can be found at  (https://github.com/rse/graphql-tools-subscribe). 

# Installation
## Maven 

```
mvn clean install
```

## Gradle 

FIXME

# Usage 

## Maven 
```
<dependency>
	<groupId>com.graphqlio</groupId>
	<artifactId>graphql-tools-subscribe-java</artifactId>
	<version>0.0.9</version>
</dependency>

```

## Gradle 

```
dependencies {
  compile 'com.graphqlio:graphql-tools-subscribe-java:0.0.9'
}
```


# Sample 

Create a subscription - how to subscribe:

``` java
String strRecordQuerySid1 = "read(many)->item#{id1,id2,id3,id4,id5}.{id,name,address,email}";

GtsScope scopeSid1Cid1 = GtsScope.builder()
	.withScopeId("Sid1")
	.withConnectionId("Cid1")
	.withQuery(strRecordQuerySid1)
	.withState(GtsScopeState.SUBSCRIBED)
	.build();
```


How to use GTSRecord:

``` java
String strRecordMutationUpdateItemInQuerySid1 = "update(one)->item#{id3}.{email}";

// build two records
GtsRecord recordRecordQuerySid1 = GtsRecord.builder()
	.stringified(strRecordQuerySid1)
	.build();
GtsRecord recordMutationUpdateItemInQuerySid1 = GtsRecord.builder()
	.stringified(strRecordMutationUpdateItemInQuerySid1)
	.build();
```


Save records in store:

``` java
// add records to scope
scopeSid1Cid1.addRecord(recordRecordQuerySid1);
scopeSid1Cid1.addRecord(recordMutationUpdateItemInQuerySid1);

// transform records-string to String[]
List<String> records1 = scopeSid1Cid1.getStringifiedRecords();
String[] scopeRecords1 = records1.toArray(new String[records1.size()]);

@Autowired
private GtsKeyValueStore keyval;

keyval.putRecords(scopeSid1Cid1.getConnectionId(), scopeSid1Cid1.getScopeId(), scopeRecords1);
```


Evaluation of results:

``` java
@Autowired
private GtsEvaluation evaluation;

// GtsScope scope
List<String> sids = evaluation.evaluateOutdatedSids(scope);

// Collection<GtsConnection> connecions
Map<String, Set<String>> sids4cid = evaluation.evaluateOutdatedsSidsPerCid(sids, connecions);
```


# License 
Design and Development by msg Applied Technology Research
Copyright (c) 2019-2020 msg systems ag (http://www.msg-systems.com/)
All Rights Reserved.
 
Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:
 
The above copyright notice and this permission notice shall be included
in all copies or substantial portions of the Software.
 
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.