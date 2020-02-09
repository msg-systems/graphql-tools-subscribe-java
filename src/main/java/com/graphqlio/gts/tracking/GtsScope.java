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
package com.graphqlio.gts.tracking;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.graphqlio.gts.tracking.GtsConnection.Builder;
import com.graphqlio.uuid.domain.NsUrl;
import com.graphqlio.uuid.domain.TypeFormat;
import com.graphqlio.uuid.domain.UUIDDto;
import com.graphqlio.uuid.helpers.A2HS;
import com.graphqlio.uuid.helpers.UUIDHelper;

/**
 * GtsScope
 *
 * @author Michael Schäfer
 * @author Dr. Edgar Müller
*/
public class GtsScope {

    private String scopeId;
    private String connectionId = null;			//// default no connection `${this.uuid}:none`  ????
    private String query;
    private String variables;
    private GtsScopeState scopeState;
    private List<GtsRecord> records = new ArrayList<>();


    public GtsScope(Builder builder) {
        this.scopeId=builder.scopeId;
        this.connectionId=builder.connectionId;
        this.scopeState=builder.scopeState;
        this.query=builder.query;
        this.variables=builder.variables;
    }

    public String getScopeId() {
        return this.scopeId;
    }

    public String getConnectionId() {
        return this.connectionId;
    }

    public GtsScopeState getScopeState() {
        return this.scopeState;
    }

    public void  setScopeState(GtsScopeState scopeState) {
        this.scopeState = scopeState;
    }

    public List<GtsRecord> getRecords() {
        return this.records;
    }
    
    public void addRecord(GtsRecord record) {
        this.records.add(record);
    }

    public List<String> getStringifiedRecords() {
    	List<String> stringifiedRecords = new ArrayList<>(); 
    	this.records.forEach(r -> stringifiedRecords.add(r.stringify()));
    	return stringifiedRecords;
    }
        
    public static Builder builder() {
		return new Builder();
	}

    public static final class Builder {

        private String scopeId = null;
        private String connectionId = null;		//// default no connection `${this.uuid}:none`  ????
        private GtsScopeState scopeState = GtsScopeState.UNSUBSCRIBED;
        private String query;
        private String variables;
             
		private Builder() {
        }

		public Builder withQuery(String query) {
            this.query=query;
			return this;
        }
        public Builder withVariables(String variables) {
            this.variables=variables;
			return this;
		}

        public Builder withScopeId(String scopeId) {
            this.scopeId=scopeId;
			return this;
		}
        
        public Builder withConnectionId(String connectionId) {
            this.connectionId=connectionId;
			return this;
		}
        
        public Builder withState(GtsScopeState scopeState) {
        	this.scopeState = scopeState;
        	return this;
        }
                
		public GtsScope build() {
            // Build Process 
			
			if (this.scopeId == null) {
	            // scopeId = .... from query and variables and some other stuff 
				///           this.scopeId = this.variables + this.query;
//	            this.scopeId = UUID.randomUUID().toString();

				
	            String uuid = generateUUID	(5
	            							, NsUrl.NS_URL
	            							, "http://engelschall.com/ns/graphql-query"
	            							, this.variables + this.query
	            							);
	            if ( uuid != null && !uuid.isEmpty()) 
	            	this.scopeId = uuid;
	            else
	            	this.scopeId = UUID.randomUUID().toString();
	            	            
			}
            return new GtsScope(this);
		} 
		
		
		
//        const data = ObjectHasher.sort({ query, variables })
//        const ns = new UUID(5, "ns:URL", "http://engelschall.com/ns/graphql-query")
//        this.sid = (new UUID(5, ns, data)).format()

		private String generateUUID(int version, NsUrl urlFormat, String url, String data) {
			
			UUIDDto uUUIDOptions = new UUIDDto();
			uUUIDOptions.setData(url);
			uUUIDOptions.setNsUrl(urlFormat);
			uUUIDOptions.setVersion(version);
			uUUIDOptions.setTypeFormatNs(TypeFormat.STD);

			try {
				
				long[] uuid = UUIDHelper.generateUUIDLongArray(uUUIDOptions, uUUIDOptions.getVersion());
				String uuidFormat = A2HS.format(uUUIDOptions.getTypeFormatNs().getTypeFormat(), uuid);

				uUUIDOptions.setNs(uuid);
				uUUIDOptions.setUuidFormat(uuidFormat);
				uUUIDOptions.setData(data);
				uUUIDOptions.setVersionSid(version);
				uUUIDOptions.setTypeFormatSid(TypeFormat.STD);
				long[] sid = UUIDHelper.generateUUIDLongArray(uUUIDOptions, uUUIDOptions.getVersionSid());
				return A2HS.format(uUUIDOptions.getTypeFormatSid().getTypeFormat(), sid);


			} catch (Exception e) {

				return null;
			}
			
		}

		

	}
    
}
