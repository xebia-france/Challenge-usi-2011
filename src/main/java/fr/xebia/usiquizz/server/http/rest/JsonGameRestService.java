package fr.xebia.usiquizz.server.http.rest;

import com.usi.Sessiontype;
import fr.xebia.usiquizz.core.game.Game;
import fr.xebia.usiquizz.core.xml.GameParameterParser;
import fr.xebia.usiquizz.core.xml.InvalidParameterFileException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JsonGameRestService extends RestService {

    private static Logger logger = LoggerFactory.getLogger(JsonGameRestService.class);

    private GameParameterParser gameParameterParser;

    private Game game;

    public JsonGameRestService(GameParameterParser gameParameterParser, Game game) {
        this.gameParameterParser = gameParameterParser;
        this.game = game;
    }

    @Override
    public void post(String path, ChannelHandlerContext ctx, MessageEvent e) {
        logger.debug("REST call for path " + path);
        logger.trace("Message : " + e.getMessage().toString());
        HttpRequest request = (HttpRequest) e.getMessage();
        logger.info("Parameters : " + new String(request.getContent().array()));
        try {
            String authenticationKey = null;
            String parameters = null;
            JsonParser jp = jsonFactory.createJsonParser(request.getContent().array());
            jp.nextToken(); // will return JsonToken.START_OBJECT (verify?)
            while (jp.nextToken() != JsonToken.END_OBJECT) {
                String fieldname = jp.getCurrentName();
                jp.nextToken(); // move to value, or START_OBJECT/START_ARRAY
                if ("authentication_key".equals(fieldname)) { // contains an object
                    authenticationKey = jp.getText();
                }
                else if ("parameters".equals(fieldname)) {
                    parameters = jp.getText();
                }
                else {
                    writeResponse(HttpResponseStatus.BAD_REQUEST, ctx, e);
                    logger.error("Unrecognized field '" + fieldname + "'!");
                    return;
                }
            }
            if (authenticationKey != null && parameters != null) {
                Sessiontype sessionType = gameParameterParser.parseXmlParameter(parameters);
                // FIXME Fill the mock for the moment
                game.init(sessionType);
                writeResponse(HttpResponseStatus.CREATED, ctx, e);
                return;
            }
        }
        catch (IOException e1) {
            e1.printStackTrace();
            writeResponse(HttpResponseStatus.BAD_REQUEST, ctx, e);
        }
        catch (InvalidParameterFileException e2) {
            writeResponse(HttpResponseStatus.BAD_REQUEST, ctx, e);
        }
        // ERROR
        writeResponse(HttpResponseStatus.BAD_REQUEST, ctx, e);
    }
}
