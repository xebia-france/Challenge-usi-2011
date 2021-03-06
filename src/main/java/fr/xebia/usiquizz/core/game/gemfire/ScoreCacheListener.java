package fr.xebia.usiquizz.core.game.gemfire;

import com.gemstone.gemfire.cache.EntryEvent;
import com.gemstone.gemfire.cache.util.CacheListenerAdapter;
import fr.xebia.usiquizz.core.game.Score;
import fr.xebia.usiquizz.core.persistence.GemfireRepository;
import fr.xebia.usiquizz.core.persistence.Joueur;
import fr.xebia.usiquizz.core.sort.LocalBTree;
import fr.xebia.usiquizz.core.sort.RBTree;
import org.jboss.netty.handler.execution.MemoryAwareThreadPoolExecutor;

import java.util.concurrent.*;


public class ScoreCacheListener extends CacheListenerAdapter<String, Score> {

    private ConcurrentSkipListSet<Joueur> tree;

    ThreadFactory treeInsertionThreadFactory = new ThreadFactory() {

        private int i = 1;

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("Score ordering #1-" + i++);
            return thread;
        }
    };

    private ExecutorService executorService = new MemoryAwareThreadPoolExecutor(1, 400000000, 2000000000, 60, TimeUnit.SECONDS, treeInsertionThreadFactory);


    public ScoreCacheListener(ConcurrentSkipListSet<Joueur> tree) {
        this.tree = tree;
    }

    /**
     * Compute final ranking after score has been added to final score region
     * if and only if we own the score lock
     *
     * @param entryEvent
     */
    @Override
    public void afterCreate(final EntryEvent<String, Score> entryEvent) {
        // Don't block gemfire thread
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                Score score = entryEvent.getNewValue();
                tree.add(new Joueur(score.getCurrentScore(), score.lname, score.fname, score.email));
            }
        });

    }
}
