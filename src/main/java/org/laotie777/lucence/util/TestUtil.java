package org.laotie777.lucence.util;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
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

    /**
     * 返回结果中是否包含指定标题
     * @param searcher
     * @param docs
     * @param title
     * @return
     */
    public static boolean hitTitle(IndexSearcher searcher, TopDocs docs,String title) throws IOException {
        for (ScoreDoc doc : docs.scoreDocs) {
            Document document = searcher.doc(doc.doc);
            if(title.equals(document.get("title"))){
                return true;
            }
        }
        return false;
    }
}
