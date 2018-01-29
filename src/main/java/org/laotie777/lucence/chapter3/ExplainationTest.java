package org.laotie777.lucence.chapter3;

import junit.framework.TestCase;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.util.Version;
import org.laotie777.lucence.util.TestUtil;

import java.io.IOException;

/**
 * 评分解释机制
 * @author yuh
 * 2018/1/29.
 */
public class ExplainationTest extends TestCase{

    public void testExplain() throws IOException, ParseException {
        IndexSearcher indexSearcher = new IndexSearcher(TestUtil.getIndexDirectory());
        QueryParser parser = new QueryParser(Version.LUCENE_30,"contents",new SimpleAnalyzer());
        Query query = parser.parse("junit");
        TopDocs docs = indexSearcher.search(query, 10);
        ScoreDoc[] scoreDocs = docs.scoreDocs;
        for(ScoreDoc doc:scoreDocs){
            Explanation explain = indexSearcher.explain(query, doc.doc);
            Document doc1 = indexSearcher.doc(doc.doc);
            System.out.println(doc1.get("title"));
            System.out.println(explain);
        }
        indexSearcher.close();
    }

}
