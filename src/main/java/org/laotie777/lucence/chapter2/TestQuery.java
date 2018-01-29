package org.laotie777.lucence.chapter2;

import junit.framework.TestCase;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.laotie777.lucence.util.TestUtil;

import java.io.IOException;

/**
 * 查询测试
 *
 * @author yuh
 * 2018/1/29.
 */
public class TestQuery extends TestCase {

    IndexSearcher searcher = null;
    Directory directory = null;

    @Override
    protected void setUp() throws Exception {
        directory = TestUtil.getIndexDirectory();
        searcher = new IndexSearcher(directory);
    }

    /**
     * 简单查询 TermQuery不会被分词
     */
    public void testSimpleQuery() throws IOException {
        TermQuery termQuery = new TermQuery(new Term("subject", "ant"));
        TopDocs docs = searcher.search(termQuery, 100);
        assertEquals("ant => ", 1, docs.totalHits);

        TermQuery termQuery1 = new TermQuery(new Term("subject", "junit"));
        TopDocs docs1 = searcher.search(termQuery1, 100);
        assertEquals("junit => ", 2, docs1.totalHits);
    }

    /**
     * 解析查询条件
     */
    public void testQueryParser() throws ParseException, IOException {
        QueryParser parser = new QueryParser(Version.LUCENE_30, null, new SimpleAnalyzer());
        String q1 = "+contents:JUNIT +contents:ANT -contents:MOCK";
        String q2 = "contents:junit OR contents:mock";
        Query parse = parser.parse(q1);
        Query parse2 = parser.parse(q2);
        TopDocs docs = searcher.search(parse, 10);
        TopDocs docs1 = searcher.search(parse2, 10);
        Document doc = searcher.doc(docs.scoreDocs[0].doc);
        System.out.println(doc.get("title"));
        assertEquals(docs.totalHits, 1);
        assertEquals(docs1.totalHits, 2);

    }

    //commit才进行磁盘IO

}
