package com.kohlschutter.boilerpipe.corpora;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Code that tests for the assertion of a test but instead of testing with
 * data embedded in unit tests we test with data loaded from system resources.
 */
public class CorporaAsserter {

    /**
     * When in update mode, we just write data to resources and all tests pass.
     * This allows us to bulk approve a large number of tests if we've updated
     * an algorithm and think that all of them pass.
     */
    public static boolean UPDATE_MODE = true;

    private final CorporaCache corporaCache;

    /**
     * Create a corpora assertion tester using the given class as a parent
     * for logging and resource loading.
     * @param parent
     */
    public CorporaAsserter(Class<?> parent) {
        this.corporaCache = new CorporaCache( parent );
    }

    public void assertCorpora( String key, String actual ) throws IOException {


        if ( UPDATE_MODE ) {

            corporaCache.write( key, actual );

        } else {

            String expected = corporaCache.read( key );

            assertEquals( expected, actual );

        }

    }

}
