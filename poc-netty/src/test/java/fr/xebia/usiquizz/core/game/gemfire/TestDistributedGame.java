package fr.xebia.usiquizz.core.game.gemfire;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;
import com.usi.MockSessionType;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;


public class TestDistributedGame {

    private DistributedGame distributedGame = new DistributedGame();

    private Cache cache = new CacheFactory().set("cache-xml-file", "gemfire/cache.xml").create();

    @Test
    public void basicTest() {
        distributedGame.init(new MockSessionType());
        Object res = cache.getRegion("game-gameRegion").get("long-pooling-duration");
        assertNotNull(res);
        assertTrue(res instanceof Integer);
        assertEquals(10000, res);
    }


}