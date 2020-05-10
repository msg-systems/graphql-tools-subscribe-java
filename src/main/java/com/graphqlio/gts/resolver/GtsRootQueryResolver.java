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
package com.graphqlio.gts.resolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.graphqlio.gts.context.GtsContext;
import com.graphqlio.gts.evaluation.GtsEvaluation;
import com.graphqlio.gts.tracking.GtsScope;
import com.graphqlio.gts.tracking.GtsScopeState;


import graphql.schema.DataFetchingEnvironment;
import jdk.internal.org.jline.utils.Log;

/**
 * GraphQL IO Root Query Resolver.
 *
 * @author Michael Schäfer
 * @author Dr. Edgar Müller
 */
public class GtsRootQueryResolver implements GraphQLQueryResolver {

  private final Logger logger = LoggerFactory.getLogger(GtsRootQueryResolver.class);

  public GtsSubscription _Subscription() {
    return new GtsSubscription();
  }
  

  /**
   * List of outdated Scopes for this connection.
   *
   * @author Michael Schäfer
   * @author Dr. Edgar Müller
   */
  public List<String> outdated(DataFetchingEnvironment env) {
    List<String> sids = new ArrayList<>();
    GtsContext context = env.getContext();
    if (context != null) {
      GtsScope scope = context.getScope();
      GtsEvaluation gtsEvaluation = context.getGtsEvaluation();
      if (gtsEvaluation != null) {
        List<String> sidArray = new ArrayList<>();
        Set<String> sidSet = gtsEvaluation.readOutdatedScopes4ConnectionID(scope.getConnectionId());
        if (sidSet != null && !sidSet.isEmpty()) {
          sidArray.addAll(sidSet);
        }
        logger.info("GtsRootQueryResolver::outdated Scopes for Connection " 
            + scope.getConnectionId() 
            + ": " 
            + sidArray);
        return sidArray;
      }
    }
    return sids;
  }
  
}
