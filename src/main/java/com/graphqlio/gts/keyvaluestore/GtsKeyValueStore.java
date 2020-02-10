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
package com.graphqlio.gts.keyvaluestore;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class responsible for providing fast access to records used in (connection-)scopes
 *
 * @author Michael Schäfer
 * @author Dr. Edgar Müller
 */
public class GtsKeyValueStore {

  HashMap<String, String> kvp = new HashMap<>();

  /// key pattern "cid:*:sid:*
  /// value: string, e.g. serialized data structure
  private String generateKey(String connectionId, String scopeId) {
    return "cid:" + connectionId + ":sid:" + scopeId;
  }

  public void store(String connectionId, String scopeId, String value) {
    kvp.put(generateKey(connectionId, scopeId), value);
  }

  public String get(String connectionId, String scopeId) {
    return kvp.get(generateKey(connectionId, scopeId));
  }

  public boolean delete(String connectionId, String scopeId) {
    return kvp.remove(generateKey(connectionId, scopeId)) != null;
  }

  public void deleteAllKeysForConnection(String connectionId) {
    Set<String> connectionKeys = getAllKeysForConnection(connectionId);
    connectionKeys.forEach(key -> kvp.remove(key));
  }

  public boolean hasKey(String connectionId, String scopeId) {
    return kvp.containsKey(generateKey(connectionId, scopeId));
  }

  public Set<String> getAllKeys() {
    return kvp.keySet()
        .stream()
        .filter(key -> (key.indexOf("cid:") > -1))
        .filter(key -> (key.indexOf("sid:") > -1))
        .collect(Collectors.toSet());
  }

  /// delete all keys; e.g. "cleanup" at the very beginning when server starts
  public void deleteAllKeys() {
    kvp.clear();
  }

  public Set<String> getAllKeysForConnection(String connectionId) {
    return kvp.keySet()
        .stream()
        .filter(key -> (key.indexOf("cid:" + connectionId) > -1))
        .collect(Collectors.toSet());
  }

  public Set<String> getAllKeysForScope(String scopeId) {
    return kvp.keySet()
        .stream()
        .filter(key -> (key.indexOf("sid:" + scopeId) > -1))
        .collect(Collectors.toSet());
  }

  public void putRecords(String connectionId, String scopeId, String[] records) {
    String value = String.join("@", records);
    store(connectionId, scopeId, value);
  }

  public String[] getRecords(String connectionId, String scopeId) {
    String value = get(connectionId, scopeId);
    if (value == null) return null;
    return value.split("\\@");
  }

  public static String getConnectionId(String key) {
    int indexCid = key.indexOf("cid:") + 4;
    int indexSid = key.indexOf(":sid:");
    assert (indexCid == 4 && indexSid > 4);

    if (indexCid == 4 && indexSid > 4) {
      return key.substring(indexCid, indexSid);
    } else return null;
  }

  public static String getScopeId(String key) {
    int indexCid = key.indexOf("cid:") + 4;
    int indexSid = key.indexOf(":sid:") + 5;
    assert (indexCid == 4 && indexSid >= 10);

    if (indexCid == 4 && indexSid >= 10) {
      return key.substring(indexSid);
    } else return null;
  }
}
