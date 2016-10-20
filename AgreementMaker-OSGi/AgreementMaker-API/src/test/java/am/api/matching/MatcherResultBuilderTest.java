package am.api.matching;

import am.api.matching.MatcherResult;
import org.junit.Test;

import static org.junit.Assert.*;

public class MatcherResultBuilderTest {
    @Test
    public void should_accept_null() {
        MatcherResult result = new MatcherResult.Builder()
                .build();

        assertFalse(result.getClasses().isPresent());
        assertFalse(result.getProperties().isPresent());
        assertFalse(result.getInstances().isPresent());
    }
}