package org.laotie777.lucence.chapter3;

import junit.framework.TestCase;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.laotie777.lucence.util.TestUtil;

import java.io.IOException;

/**
 *解析器测试
 */
public class ParserTest extends TestCase{

    protected IndexSearcher searcher;
    protected Directory directory;


    @Override
    protected void setUp() throws Exception {
        directory = TestUtil.getIndexDirectory();
        searcher = new IndexSearcher(directory);
    }

    /**
     * 测试parser的toString方法
     */
    public void testToString(){
        BooleanQuery booleanQuery = new BooleanQuery();
        booleanQuery.add(new TermQuery(new Term("field1","fieldContent")), BooleanClause.Occur.MUST);
        booleanQuery.add(new TermQuery(new Term("field2","fieldContent1")), BooleanClause.Occur.SHOULD);
        assertEquals("+field1:fieldContent field2:fieldContent1",booleanQuery.toString());
    }

    /**
     * 测试解析单个词条
     */
    public void testParseTerm() throws ParseException {
        QueryParser parser = new QueryParser(Version.LUCENE_30, "term", new SimpleAnalyzer());
        Query filedContent = parser.parse("filedContent");
        System.out.println(filedContent.toString());
    }

    /**
     * 测试范围检索
     */
    public void testRangeQuery() throws ParseException, IOException {
        QueryParser parser = new QueryParser(Version.LUCENE_30, "subject", new SimpleAnalyzer());
        Query query = parser.parse("title2:[Q TO V]");
        assertTrue(query instanceof TermRangeQuery);
        System.out.println(query);
        TopDocs docs = searcher.search(query,10);
        assertTrue(TestUtil.hitTitle(searcher,docs,"Tapestry in Action"));

        Query query1 = parser.parse("title:{Q TO \"Tapestry in Action\"}");
        System.out.println(query1);
        TopDocs docs1 = searcher.search(query1,10);
        assertTrue(TestUtil.hitTitle(searcher,docs1,"Tapestry in Action"));
    }

}


































