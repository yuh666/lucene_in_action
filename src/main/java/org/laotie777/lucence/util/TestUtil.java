package org.laotie777.lucence.util;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
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

    /**
     * 返回索引路径
     * @return
     */
    public static Directory getIndexDirectory() throws IOException {
        return FSDirectory.open(new File("./index"));
    }

}
