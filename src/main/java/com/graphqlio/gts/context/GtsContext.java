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
package com.graphqlio.gts.context;

import org.springframework.web.socket.WebSocketSession;

import com.graphqlio.gts.tracking.GtsScope;

import graphql.schema.GraphQLSchema;

/**
 * Context to be passed to GraphQL engine resp. DataFetchingEnvironment
 * 
 * @author Michael Schäfer
 * @author Dr. Edgar Müller
 */

public class GtsContext {

	private WebSocketSession webSocketSession;
	private GraphQLSchema graphQLSchema;
	private GtsScope scope;

	private GtsContext(Builder builder) {
		this.webSocketSession=builder.webSocketSession;
		this.graphQLSchema=builder.graphQLSchema;
		this.scope=builder.scope;
	}

	public GtsScope getScope() {
		return this.scope;
	}

	public WebSocketSession getWebSocketSession() {
		return webSocketSession;
	}

	public GraphQLSchema getGraphQLSchema() {
		return this.graphQLSchema;
	}

	public static Builder builder() {
		return new Builder();
	} 

	public static final class Builder {

		private WebSocketSession webSocketSession;
		private GraphQLSchema graphQLSchema;
		private GtsScope scope;

		private Builder() {

		}

		public Builder webSocketSession(WebSocketSession webSocketSession) {
			this.webSocketSession = webSocketSession;
			return this;
		}

		public Builder graphQLSchema(GraphQLSchema graphQLSchema) {
			this.graphQLSchema = graphQLSchema;
			return this;
		}


		public Builder scope(GtsScope scope) {
			this.scope=scope;
			return this;
		}

		public GtsContext build() {
			return new GtsContext(this);
		}

	}

}
