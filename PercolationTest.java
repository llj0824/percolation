import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class PercolationTest {

    @Test
    public void testMapReferencesUpdate() {
        Map<String, Set<String>> map = new HashMap<>();
        final String key1 = "k1";
        final String key2 = "k2";
        map.put(key1, new HashSet<>(Arrays.asList(key1)));
        map.put(key2, new HashSet<>(Arrays.asList(key2)));

        map.get(key1).addAll(map.get(key2));
        assertEquals(map.get(key1).size(), 2);

    }
}
