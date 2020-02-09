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
package com.graphqlio.gts.evaluation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.graphqlio.gts.exceptions.GtsScopeEvaluationException;
import com.graphqlio.gts.keyvaluestore.GtsKeyValueStore;
import com.graphqlio.gts.tracking.GtsConnection;
import com.graphqlio.gts.tracking.GtsRecord;
import com.graphqlio.gts.tracking.GtsScope;
import com.graphqlio.gts.tracking.GtsScopeState;
import com.graphqlio.gts.tracking.GtsRecord.GtsArityType;
import com.graphqlio.gts.tracking.GtsRecord.GtsOperationType;

import graphql.GraphQLException;


/**
 * Class evaluating which scopes resp. query subscription are out of date once any mutation was performed
 * 
 * @author Michael Schäfer
 * @author Dr. Edgar Müller
 */


/**
 * GtsEvaluation
 */
public class GtsEvaluation {

	private final Logger logger = LoggerFactory.getLogger(GtsEvaluation.class);
		
    private GtsKeyValueStore keyval;

    public GtsEvaluation(GtsKeyValueStore keyval) {
    	this.keyval = keyval;
    }
    
    
    private static boolean outdated(List<GtsRecord> newRecords, List<GtsRecord> oldRecords) {

        // iterate over all new and old records...

        for (GtsRecord recNew : newRecords) {

            for (GtsRecord recOld : oldRecords) {
                boolean outdated = false;

                /*
                 * CASE 1: modified entity (of arbitrary direct access) old/query:
                 * [*#{*}.*->]read(*)->Item#{1}.{id,name} new/mutation:
                 * [*#{*}.*->]update/delete(*)->Item#{1}.{name}
                 */

                if ((recNew.op() == GtsOperationType.UPDATE  ||
                	 recNew.op() == GtsOperationType.DELETE   )   && 
                	recOld.dstType().equals(recNew.dstType()) 			&& 
                	overlap(recOld.dstIds(), recNew.dstIds()) 			&& 
                	overlap(recOld.dstAttrs(), recNew.dstAttrs()))
                    outdated = true;

                /*
                 * CASE 2: modified entity list (of relationship traversal) old/query
                 * Card#1.items->read(*)->Item#{2}.{id,name} new/mutation:
                 * [*#{*}.*->]update(*)->Card#{1}.{items}
                 */

                /// warum nicht Delete???
                else if (recNew.op() == GtsOperationType.UPDATE 				&& 
                		recOld.srcType() != null									&& 
                		recOld.srcType().equals(recNew.dstType())					&& 
                		overlap(new String[] { recOld.srcId() }, recNew.dstIds())	&& 
                		overlap(new String[] { recOld.srcAttr() }, recNew.dstAttrs()))
                    outdated = true;

                /*
                 * CASE 3: modified entity list (of direct query) old/query
                 * [*#{*}.*->]read(many/all)->Item#{*}.{id,name} new/mutation:
                 * [*#{*}.*->]create/update/delete(*)->Item#{*}.{name}
                 */
                else if ((recNew.op() == GtsOperationType.CREATE  ||
                		  recNew.op() == GtsOperationType.UPDATE  ||
                   	 	  recNew.op() == GtsOperationType.DELETE   )  && 
                		 (recOld.arity() == GtsArityType.MANY     ||
                		  recOld.arity() == GtsArityType.ALL       )	&& 
                		 recOld.dstType().equals(recNew.dstType()) 			&& 
                		 overlap(recOld.dstAttrs(), recNew.dstAttrs()))
                    outdated = true;

                /* report outdate combination */
                if (outdated) {
                    // this.emit("scope-outdated", { old: recordsOld[j], new: recordsNew[i] })
                    // let recOld = this.__recordStringify(recordsOld[j])
                    // let recNew = this.__recordStringify(recordsNew[i])
                    // this.emit("debug", `scope-outdated old=${recOld} new=${recNew}`)
                    return true;
                }
            }
        }

        /* ...else the scope is still valid (not outdated) */
        return false;
    }

    private static boolean overlap(String[] list1, String[] list2) {
        // Description : checks, if 2 lists contain overlapping strings
        // Source :
        // https://github.com/rse/graphql-tools-subscribe/blob/master/src/gts-3-evaluation.js
        // __scopeOutdatedEvent (sids) {

        if (list1.length == 0 || list2.length == 0)
            return false;
        if (list1.length == 1 && list1[0].equals("*"))
            return true;
        if (list2.length == 1 && list2[0].equals("*"))
            return true;

        List<String> listA = new ArrayList<>(Arrays.asList(list1));
        List<String> listB = new ArrayList<>(Arrays.asList(list2));

        Set<String> result = listA.stream()
        		  .distinct()
        		  .filter(listB::contains)
        		  .collect(Collectors.toSet());

        return !result.isEmpty();
    }

    public List<String> evaluateOutdatedSids(GtsScope scope) {
        // Description : Activity Diagramm
        // Source :
        // https://github.com/rse/graphql-tools-subscribe/blob/master/src/gts-3-evaluation.js
        // __scopeProcess (scope)

        String scopeId = scope.getScopeId();
        String connectionId = scope.getConnectionId();

        /// Contains for a HashSet is O(1) compared to O(n) for a list, 
        /// therefore we use hashset to check if a scope is outdated        
        HashMap<String, Boolean> outdatedSids = new HashMap<>();

        /* filter out write records in the scope */
        List<GtsRecord> recordsWrite = scope.getRecords().stream()
                .filter(r -> (r.op() == GtsOperationType.CREATE  ||
                			  r.op() == GtsOperationType.UPDATE  ||
                			  r.op() == GtsOperationType.DELETE   ))
                			  .collect(Collectors.toList());

        
        
        
        /* Check Workflow: which component is responsible for storing scope records to KeyValueStore
         * 
         * according Ralf's implementation 
         *   this.keyval.putRecords(connectionId, scopeId, recNew);
         *   is only called if "recordWrite.isEmpty"
         *   why?
         *   
         *   if we have 2 scopes each with a read and write operation
         *   the "else case" "keyset" returns no keys as there was no putRecords call before 
         */
        
                
        /* queries (scopes without writes)... */        
        if (recordsWrite.isEmpty()) {

            if (scope.getScopeState() == GtsScopeState.SUBSCRIBED) {
                /* ...with subscriptions are remembered */
                String[] rec = this.keyval.getRecords(connectionId, scopeId);
                List<String> records = scope.getStringifiedRecords();
                String[] recNew = records.toArray(new String[records.size()]);

                if (rec == null || rec.length == 0 || !Arrays.equals(rec, recNew)) {
                    this.keyval.putRecords(connectionId, scopeId, recNew);
                    // this.emit("debug", `scope-store-update sid=${sid} cid=${cid}`)
                }
            } else {
                /*
                 * ...without subscriptions can be just destroyed (and the processing
                 * short-circuited)
                 */
            	
            	/* To check: what about paused scopes???? */

                // scope.destroy(); ??? Todo: parent needs to destroy scope?
                this.keyval.delete(connectionId, scopeId); /// delete scope records

            }
        }
        /* mutations (scopes with writes) might outdate queries (scopes with reads) */
        else {
            /* determine all stored scope records */
            HashMap<String, List<String>> sids = new HashMap<>();
            Set<String> keys = this.keyval.getAllKeys();
            keys.forEach(key -> {
                // CR 11072019 We should discuss about the compound key
            	// coded analog RedisTemplate
                String cid = GtsKeyValueStore.getConnectionId(key);
                String sid = GtsKeyValueStore.getScopeId(key);

                // CR 11072019 We should discuss the Algorithm
                
                /// ToDo check relation between (sid,cid)
                if (!sids.containsKey(sid)) {
                    List<String> cids = new ArrayList<>();
                    cids.add(cid);
                    sids.put(sid, cids);
                }
            });

            // ToDo: CR 11072019 We should discuss the Algorithm
            HashMap<String, Boolean> checkedRecords = new HashMap<>(); // ??? key serialized String of records???? !!!!

            sids.keySet().forEach(sid -> {
                /// check just once
                if (outdatedSids.containsKey(sid))
                    return;

                sids.get(sid).forEach(cid -> {
                  
                    try {
                        boolean outdated = evaluateOutdatedSidPerCid(checkedRecords, recordsWrite, cid, sid);
                        if (outdated)
                            outdatedSids.put(sid, true);
                    }
                    catch (GtsScopeEvaluationException evalException) {
                    	/// do ignore here.                    	                    	
                    }
                    catch(GraphQLException e) {
                    	////  other exceptions??? 
                    }
                    
                });
            });
        }

        Set<String> set = outdatedSids.keySet();
        List<String> list = new ArrayList<>();
        list.addAll(set);
        return list;
    }

    /// evaluate if scope is outdated for a particular connection
    private boolean evaluateOutdatedSidPerCid(Map<String, Boolean> checkedRecords, /// passes HashMap in real
            List<GtsRecord> recordsWrite, String connectionId, String scopeId) {

        // fetch scope records value
        String[] strRecords = this.keyval.getRecords(connectionId, scopeId);
        boolean recordsChecked = false;
        String stringifiedRecords = null;
        /// ToDo: should be discussed with Ralf if "recordsChecked" this is really necessary and saves performance
        if (strRecords != null && strRecords.length > 0) {

            StringBuilder sb = new StringBuilder();
            for (String record : strRecords) {
            	if ( record != null  &&  record.length() > 0)
            		sb.append(record);
            }
            stringifiedRecords = sb.toString();
            recordsChecked = checkedRecords.containsKey(stringifiedRecords);
        }
        else {
        	logger.warn(String.format("Empty GtsRecord List found for connection id (%s) and scope id (%s)", connectionId, scopeId));
            this.keyval.delete(connectionId, scopeId);
            throw new GtsScopeEvaluationException(String.format("Empty GtsRecord List found for connection id (%s) and scope id (%s)", connectionId, scopeId));
        }

        /*
         * ///////////////////////////////////////////
         * Bei zwei Subscriptions wird im 1. Durchgang (sid1-cid1) recordsChecked auf true gesetzt.
         * Im 2. Durchgang (sid2-cid1 oder sid1-cid2) wird dadurch nicht mehr in dieses if gesprungen.
         * Ist das so gewünscht?
         * Wieso der Aufwand mit recordsChecked?
         * ///////////////////////////////////////////
         */
        if (!recordsChecked) {
            List<GtsRecord> recordsRead = new ArrayList<>();
            for (String strRecord : strRecords) {
            	GtsRecord record = GtsRecord.builder().stringified(strRecord).build();
            	if ( record.op() ==  GtsOperationType.READ)
            		recordsRead.add(record);
            }

            // check whether writes outdate reads
            boolean outdated = GtsEvaluation.outdated(recordsWrite, recordsRead);

            // remember that these scope records were already checked
            if (stringifiedRecords != null)
                checkedRecords.put(stringifiedRecords, true);

            if (outdated)
                return true;
        }

        return false;
    }

    ///  process an outdated event  */
    ///    __scopeOutdatedEvent (sids)    
    // Map<cid, <sids>>
	// method is called by GtsWebSocketHandler::handleTextMessage
    public Map<String, Set<String>> evaluateOutdatedsSidsPerCid(List<String> sids,
            Collection<GtsConnection> connections) {

    	if (connections == null)
    		return null;

    	Map<String, Set<String>> sidsPerCid = new HashMap<>();
        connections.forEach((conn) -> {
        	if ( conn == null)
        		return;
        	String cid = conn.getConnectionId();

        	conn.scopes().forEach((scope) -> { 
        		if (scope == null)
        			return;
        		
            	String sid = scope.getScopeId();
  
            	if ( scope.getScopeState() == GtsScopeState.SUBSCRIBED ) {
            		if (sids.contains(sid)) {
                    	if (!sidsPerCid.containsKey(cid)) {
                    		Set<String> sidSet = new HashSet<>(); 
                    		sidSet.add(sid);
                    		sidsPerCid.put(cid,  sidSet);
                    	}
                    	else {
                    		sidsPerCid.get(cid).add(sid);
                    	}            	
                    }
            	}
            	else if (scope.getScopeState() == GtsScopeState.PAUSED ) {
            		
            		//// ToDo: do we need to set scope outdated???
            		//// see code node.js code in __scopeOutdatedEvent
            		
            	}
            	            	
            });
        	
        });
    	    	
    	return sidsPerCid;
    }

    
    ///   remove all scopes for connection if connection is closed
    public void onCloseConnection(String connectionId) {
    	this.keyval.deleteAllKeysForConnection(connectionId);
    }
    

}
