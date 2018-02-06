package org.laotie777.lucence.chapter5;

import junit.framework.TestCase;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.laotie777.lucence.util.TestUtil;

/**
 * @Author yuh
 * @Date Created in 下午1:16 2018/2/6
 * @Description
 */
public class CustomQueryTest extends TestCase{

    @Test
    public void testRecency() throws Throwable {
        Directory dir = TestUtil.getIndexDirectory();
        IndexReader r = IndexReader.open(dir);
        IndexSearcher s = new IndexSearcher(r);
        s.setDefaultFieldSortScoring(true, true);

        QueryParser parser = new QueryParser(
                Version.LUCENE_30,
                "contents",
                new StandardAnalyzer(
                        Version.LUCENE_30));
        Query q = parser.parse("java in action");       // #A
        Query q2 = new RecencyBoostQuery(q, "pubmonthAsDay",// #B
                10 * 365, 2.0
        );
        Sort sort = new Sort(new SortField[]{
                SortField.FIELD_SCORE,
                new SortField("title2", SortField.STRING)});
        TopDocs hits = s.search(q, null, 5, sort);

        for (int i = 0; i < hits.scoreDocs.length; i++) {
            Document doc = r.document(hits.scoreDocs[i].doc);
            System.out.println((1 + i) + ": " +
                    doc.get("title") +
                    ": pubmonth=" +
                    doc.get("pubmonth") +
                    " score=" + hits.scoreDocs[i].score);
        }
        s.close();
        r.close();
        dir.close();
    }
}
