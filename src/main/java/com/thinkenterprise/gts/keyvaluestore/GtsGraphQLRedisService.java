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

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import redis.embedded.RedisServer;

/**
 * Class used to process any incoming message sent by clients via WebSocket
 * supports subprotocols (CBOR, MsgPack, Text)
 * triggers process to indicate outdating queries and notifies clients
 *
 * @author Michael Schäfer
 * @author Torsten Kühnert
 */

@Service
@Scope("singleton")
public class GtsGraphQLRedisService {

	final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger();

	@Autowired
	private RedisProperties redisProperties;

	private RedisServer embeddedRedisServer;

	public void start() throws IOException {
		log.info("Starting embedded RedisServer.");

		// this.embeddedRedisServer = new RedisServer(redisProperties.getRedisPort());

		this.embeddedRedisServer = RedisServer
				.builder()
				.port(redisProperties
						.getRedisPort())
				// .redisExecProvider(customRedisExec) //com.github.kstyrc (not
				// com.orange.redis-embedded)
				.setting("maxmemory 128M") // maxheap 128M
				.build();

		this.embeddedRedisServer.start();
		log.info("Started embedded RedisServer.");
	}

	public void stop() {
		log.info("Stopping embedded RedisServer.");
		this.embeddedRedisServer.stop();
		log.info("Stopped embedded RedisServer.");
	}

}
