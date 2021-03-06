/*
 * Copyright 2009 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package fr.xebia.usiquizz.server.http.netty;

import fr.xebia.usiquizz.core.game.DistributedScoring;
import fr.xebia.usiquizz.core.game.Game;
import fr.xebia.usiquizz.core.game.Scoring;
import fr.xebia.usiquizz.core.game.gemfire.DistributedGame;
import fr.xebia.usiquizz.core.persistence.GemfireRepository;
import fr.xebia.usiquizz.core.persistence.GemfireUserRepository;
import fr.xebia.usiquizz.core.persistence.UserRepository;
import fr.xebia.usiquizz.server.http.netty.resources.CachedResourcesRequestHandler;
import fr.xebia.usiquizz.server.http.netty.rest.LongPollingQuestionManager;
import fr.xebia.usiquizz.server.http.netty.rest.ResponseWriter;
import fr.xebia.usiquizz.server.http.netty.rest.RestRequestHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpContentCompressor;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;

import java.util.concurrent.ExecutorService;

import static org.jboss.netty.channel.Channels.pipeline;

public class HttpServerPipelineFactory implements ChannelPipelineFactory {

    //private UserRepository userRepository = new MongoUserRepository();
    private GemfireRepository gemfireRepository;
    private UserRepository userRepository;
    private Game game;
    private Scoring scoring;
    private LongPollingQuestionManager longPollingQuestionManager;

    private RestRequestHandler restRequestHandler;

    private CachedResourcesRequestHandler staticRequestHandler = new CachedResourcesRequestHandler();

    public HttpServerPipelineFactory(ExecutorService executorService) {
        gemfireRepository = new GemfireRepository();
        userRepository = new GemfireUserRepository(gemfireRepository);
        scoring = new DistributedScoring(gemfireRepository);
        game = new DistributedGame(gemfireRepository, scoring);
        longPollingQuestionManager = new LongPollingQuestionManager(game, new ResponseWriter());
        restRequestHandler = new RestRequestHandler(userRepository, game, scoring, longPollingQuestionManager, executorService);
    }

    public ChannelPipeline getPipeline() throws Exception {
        // Create a default pipeline implementation.
        ChannelPipeline pipeline = pipeline();
        Channels.pipeline();

        // Uncomment the following line if you want HTTPS
        //SSLEngine engine = SecureChatSslContextFactory.getServerContext().createSSLEngine();
        //engine.setUseClientMode(false);
        //pipeline.addLast("ssl", new SslHandler(engine));

        pipeline.addLast("decoder", new HttpRequestDecoder());
        // Uncomment the following line if you don't want to handle HttpChunks.
        pipeline.addLast("aggregator", new HttpChunkAggregator(1048576));
        pipeline.addLast("encoder", new HttpResponseEncoder());
        // Remove the following line if you don't want automatic content compression.
        pipeline.addLast("deflater", new HttpContentCompressor());
        pipeline.addLast("handler", new HttpRequestHandler(restRequestHandler, staticRequestHandler));
        return pipeline;
    }

    public void shutdown() {
        gemfireRepository.shutdown();
    }
}
