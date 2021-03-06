package fr.xebia.usiquizz.server.http.netty.rest;

import fr.xebia.usiquizz.core.game.Game;
import fr.xebia.usiquizz.core.game.Scoring;
import org.antlr.stringtemplate.StringTemplate;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

//Non requis par le challenge permet au client web de récupérer les param de conf
public class JsonParameterRestService extends RestService {

    private static Logger logger = LoggerFactory.getLogger(JsonGameRestService.class);

    private static final StringTemplate st = new StringTemplate("{ \n" +
                "\"nbQuestion\" : $nbQuestion$, \n" +
                "\"questionTimeFrame\" : $questionTimeFrame$, \n" +
                "\"synchrotime\" : $synchrotime$\n" +
                "}");

    protected JsonParameterRestService(Game game, Scoring scoring, ExecutorService executorService) {
        super(game, scoring, executorService);
    }

    @Override
    public void get(String path, ChannelHandlerContext ctx, MessageEvent e) {
        game.getQuestiontimeframe();
        responseWriter.writeResponse(constructJsonResponse(game.getNbquestions(), game.getQuestiontimeframe(), game.getSynchrotime()), HttpResponseStatus.OK, ctx, e, null);
    }

    private ChannelBuffer constructJsonResponse(int nbQuestion, int questionTimeFrame, int synchrotime) {

        st.reset();
        st.setAttribute("nbQuestion", Integer.toString(nbQuestion));
        st.setAttribute("questionTimeFrame", Integer.toString(questionTimeFrame));
        st.setAttribute("synchrotime", Integer.toString(synchrotime));
        ChannelBuffer cb = ChannelBuffers.dynamicBuffer();

        cb.writeBytes(st.toString().getBytes());
        return cb;
    }
}
