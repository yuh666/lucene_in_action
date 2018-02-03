package org.laotie777.lucence.chapter4;

import java.util.HashMap;
import java.util.Map;

/**
 * 同义词引擎实现
 */
public class SynonymEngineImpl implements SynonymEngine {

    static Map<String, String[]> words = new HashMap<>();

    static {
        words.put("jumps", new String[]{"hops", "leaps"});
    }

    /**
     * 获取同义词
     *
     * @param s
     * @return
     */
    @Override
    public String[] getSynonyms(String s) {
        return words.get(s);
    }
}
