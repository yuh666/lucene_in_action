package org.laotie777.lucence.chapter2;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

import java.io.IOException;

public class TestUtil {

    /**
     * 命中的总数量
     * @param searcher
     * @param query
     * @return
     * @throws IOException
     */
    public static int hitCount(IndexSearcher searcher, Query query) throws IOException {
        return searcher.search(query,100).totalHits;
    }
}
