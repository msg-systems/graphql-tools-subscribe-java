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
package com.thinkenterprise.gts.keyvaluestore;

import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;


/**
 * Component responsible to save records in Redis Store
 *
 * @author Michael Schäfer
 * @author Dr. Edgar Müller
 */


@Component
public class GtsKeyValueStore {

	private RedisTemplate<String, String> redisTemplate;
	
	public GtsKeyValueStore( RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
	public RedisTemplate<String, String> getRedisTemplate() {return redisTemplate; }
	
	/// key pattern "cid:*:sid:*
	/// value: string, e.g. serialized data structure

	private String generateKey(String connectionId, String scopeId) {
		return "cid:" + connectionId+ ":sid:" +scopeId;
	}
	
	public void store(String connectionId, String scopeId, String value ) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(generateKey(connectionId, scopeId), value);		
	}

	public String get (String connectionId, String scopeId) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(generateKey(connectionId, scopeId));		
	}
	
	public boolean delete (String connectionId, String scopeId) {
    	return redisTemplate.delete(generateKey(connectionId, scopeId));
	}

	public Long deleteAllKeysForConnection(String connectionId) {
		return redisTemplate.delete(getAllKeysForConnection(connectionId));
	}
	
	public boolean hasKey (String connectionId, String scopeId) {
    	return redisTemplate.hasKey(generateKey(connectionId, scopeId));
	}

	public Set<String> getAllKeys()  {
    	return redisTemplate.keys("cid:*:sid:*");
	}

	/// delete all keys; e.g. "cleanup" at the very beginning when server starts
	public void deleteAllKeys() { 
		redisTemplate.delete(getAllKeys());
	}
	
	public Set<String> getAllKeysForConnection(String connectionId)  {
		String keyPattern = "cid:" + connectionId + ":sid:*";
    	return redisTemplate.keys(keyPattern);
	}
	
	public Set<String> getAllKeysForScope(String scopeId)  {
		String keyPattern = "cid:*:sid:" + scopeId;
    	return redisTemplate.keys(keyPattern);
	}
	
	
	public void putRecords(String connectionId, String scopeId, String[] records) {
		String value = String.join("@", records);
		store(connectionId, scopeId, value);		
	}

	public String[] getRecords(String connectionId, String scopeId) {
		String value = get(connectionId, scopeId);
		if ( value == null)
			return null;
		return value.split("\\@");
	}
	
	public static String getConnectionId(String key) {
		int indexCid =  key.indexOf("cid:") + 4;
		int indexSid = key.indexOf(":sid:");
		assert (indexCid == 4 &&  indexSid > 4);
		
		if (indexCid == 4 &&  indexSid > 4) {
			return key.substring(indexCid, indexSid);
		}
		else return null;
	}

	public static String getScopeId(String key) {
		int indexCid =  key.indexOf("cid:") + 4;
		int indexSid = key.indexOf(":sid:") + 5;
		assert (indexCid == 4 &&  indexSid >= 10);
		
		if (indexCid == 4 &&  indexSid >= 10) {
			return key.substring(indexSid);
		}
		else return null;
	}
}
