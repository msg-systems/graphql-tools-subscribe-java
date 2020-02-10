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

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.graphqlio.gts.context.GtsContext;
import com.graphqlio.gts.exceptions.GtsScopeStateException;
import com.graphqlio.gts.tracking.GtsScope;
import com.graphqlio.gts.tracking.GtsScopeState;

import graphql.schema.DataFetchingEnvironment;

/**
 * GraphQL IO Subscription Resolver
 *
 * @author Michael Schäfer
 * @author Dr. Edgar Müller
 */
public class GtsSubscriptionResolver implements GraphQLResolver<GtsSubscription> {

  private final Logger logger = LoggerFactory.getLogger(GtsSubscriptionResolver.class);

  public UUID subscribe(GtsSubscription subscription, DataFetchingEnvironment env) {
    GtsContext context = env.getContext();
    GtsScope scope = context.getScope();
    scope.setScopeState(GtsScopeState.SUBSCRIBED);
    return UUID.fromString(scope.getScopeId());
  }

  public void unsubscribe(GtsSubscription subscription, String sid, DataFetchingEnvironment env) {
    GtsContext context = env.getContext();
    GtsScope scope = context.getScope();

    if (!scope.getScopeId().equals(sid)
        || (scope.getScopeState() != GtsScopeState.SUBSCRIBED
            && scope.getScopeState() != GtsScopeState.PAUSED)) {
      logger.warn("GtsSubscriptionResolver.unsubscribe: unexpected scope state");
      throw new GtsScopeStateException(
          "GtsSubscriptionResolver.unsubscribe: unexpected scope state");
    }

    scope.setScopeState(GtsScopeState.UNSUBSCRIBED);
    return;
  }

  // Todo: implementation
  public List<UUID> subscriptions(GtsSubscription subscription, DataFetchingEnvironment env) {
    return null;
  }

  public void pause(GtsSubscription subscription, String sid, DataFetchingEnvironment env) {
    GtsContext context = env.getContext();
    GtsScope scope = context.getScope();

    if (!scope.getScopeId().equals(sid) || scope.getScopeState() != GtsScopeState.SUBSCRIBED) {
      logger.warn("GtsSubscriptionResolver.pause: unexpected scope state");
      throw new GtsScopeStateException("GtsSubscriptionResolver.pause: unexpected scope state");
    }

    scope.setScopeState(GtsScopeState.PAUSED);
    return;
  }

  public void resume(GtsSubscription subscription, String sid, DataFetchingEnvironment env) {
    GtsContext context = env.getContext();
    GtsScope scope = context.getScope();

    if (!scope.getScopeId().equals(sid) || scope.getScopeState() != GtsScopeState.PAUSED) {
      logger.warn("GtsSubscriptionResolver.resume: unexpected scope state");
      throw new GtsScopeStateException("GtsSubscriptionResolver.resume: unexpected scope state");
    }

    scope.setScopeState(GtsScopeState.SUBSCRIBED);
    return;
  }
}
