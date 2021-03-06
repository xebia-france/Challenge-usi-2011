
package fr.xebia.usiquizz.server.http.netty.rest;

import fr.xebia.usiquizz.core.game.AsyncGame;
import fr.xebia.usiquizz.core.game.Game;
import fr.xebia.usiquizz.core.game.Scoring;
import fr.xebia.usiquizz.core.persistence.UserRepository;
import fr.xebia.usiquizz.server.http.netty.FastSessionKeyCookieDecoder;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.Cookie;
import org.jboss.netty.handler.codec.http.CookieDecoder;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.COOKIE;

public class JsonQuestionRestService extends RestService {

    private static final Logger logger = LoggerFactory.getLogger(JsonQuestionRestService.class);

    private static final String SESSION_KEY = "session_key";

    private static final CookieDecoder cookieDecoder = new CookieDecoder();

    private LongPollingQuestionManager longPollingQuestionManager;

    public JsonQuestionRestService(UserRepository userRepository, LongPollingQuestionManager longPollingQuestionManager, Game game, Scoring scoring, ExecutorService executorService) {
        super(game, scoring, executorService);
        this.longPollingQuestionManager = longPollingQuestionManager;
    }

    @Override
    public void get(String path, ChannelHandlerContext ctx, MessageEvent e) {
        try {
            HttpRequest request = (HttpRequest) e.getMessage();

            // Get session_key
            String sessionKey = FastSessionKeyCookieDecoder.findSessionKey(request.getHeader(COOKIE));
            
            if (sessionKey == null) {
                logger.info("Player with no cookies... Rejected");
                responseWriter.writeResponse(HttpResponseStatus.UNAUTHORIZED, ctx, e);
                return;
            }

            // Verify question asked... is active
            String questionNbr = path.substring(path.lastIndexOf("/") + 1);
            if (!game.isPlayerCanAskQuestion(sessionKey, questionNbr)) {
                // Bad player flow
                logger.warn("Bad question requested {} by {}", questionNbr, sessionKey);
                responseWriter.writeResponse(HttpResponseStatus.BAD_REQUEST, ctx, e);
                return;
            }

            responseWriter.writeResponseWithoutClose(HttpResponseStatus.OK, ctx, e);
            longPollingQuestionManager.addPlayer(sessionKey, ctx, questionNbr);
        } catch (Exception exc) {
            logger.error("error during question rest service", exc);
            responseWriter.writeResponse(HttpResponseStatus.BAD_REQUEST, ctx, e);
        }
        return;

    }
}
