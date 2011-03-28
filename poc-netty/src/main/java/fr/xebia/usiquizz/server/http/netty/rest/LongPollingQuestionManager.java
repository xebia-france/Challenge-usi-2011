package fr.xebia.usiquizz.server.http.netty.rest;

import fr.xebia.usiquizz.core.game.AsyncGame;
import fr.xebia.usiquizz.core.game.Game;
import fr.xebia.usiquizz.core.game.QuestionLongpollingCallback;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class LongPollingQuestionManager implements QuestionLongpollingCallback {

    private static final Logger logger = LoggerFactory.getLogger(LongPollingQuestionManager.class);

    private Map<String, ChannelHandlerContext> longPollingResponse = new ConcurrentHashMap<String, ChannelHandlerContext>();

    private Game game;

    private ResponseWriter responseWriter;

    private ExecutorService executorService;

    private AtomicBoolean sendQuestionStarted = new AtomicBoolean(false);

    private byte currentQuestionIndex = 1;

    public LongPollingQuestionManager(Game game, ResponseWriter responseWriter, ExecutorService executorService) {
        this.game = game;
        this.responseWriter = responseWriter;
        this.executorService = executorService;
        this.game.registerLongpollingCallback(this);
    }

    public void initNewQuestion(byte currentQuestionIndex) {
        this.longPollingResponse.clear();
        this.currentQuestionIndex = currentQuestionIndex;
        sendQuestionStarted.set(false);
    }

    public void addPlayer(String sessionKey, ChannelHandlerContext ctx, byte questionNbr) {
        longPollingResponse.put(sessionKey, ctx);
        game.addPlayerForQuestion(sessionKey, questionNbr);
    }

    public void sendQuestionToAllPlayer() {
        List<ChannelFuture> channels = new ArrayList<ChannelFuture>();
        if (!sendQuestionStarted.getAndSet(true)) {
            logger.info("Send all question to player");
            //On incremente la réponse attendu courante
            logger.info("DEBUT REPONSE {}", currentQuestionIndex);
            game.setCurrentAnswerIndex((byte) (currentQuestionIndex));
            // On incrémente l'index de question attendu
            logger.info("FIN QUESTION {}", currentQuestionIndex);
            logger.info("DEBUT QUESTION {}", currentQuestionIndex + 1);
            game.setCurrentQuestionIndex((byte) (currentQuestionIndex + 1));

            // On demande la réinitialisation
            game.resetPlayerAskedQuestion();

            long startTime = System.currentTimeMillis();
            logger.info("Start send question at {}", startTime);
            for (final String sessionKey : longPollingResponse.keySet()) {
                //executorService.submit(new Runnable() {
                //    @Override
                //    public void run() {
                channels.add(responseWriter.endWritingResponseWithoutClose(game.createQuestionInJson(currentQuestionIndex, sessionKey), sessionKey, longPollingResponse.get(sessionKey)));
                //    }
                //});

            }
            // On démarre un timer locale...
            // On le démarre apres avoir envoyer toute les questions histoire que tous les clients est au moins questiontimeframe pour répondre.
            // Et ca nous arrange....
            awaitAllQuestionSend(channels);
            logger.info("{} question send in {}", longPollingResponse.size(), System.currentTimeMillis() - startTime);
            game.startQuestionTimeframe(currentQuestionIndex);
        }
        //longPollingResponse.clear();
    }

    private void awaitAllQuestionSend(List<ChannelFuture> channels) {
        for (ChannelFuture cf : channels) {
            cf.awaitUninterruptibly();
            //cf.awaitUninterruptibly(5, TimeUnit.SECONDS);
        }
    }

    @Override
    public void startSendAll() {
        sendQuestionToAllPlayer();
    }
}
