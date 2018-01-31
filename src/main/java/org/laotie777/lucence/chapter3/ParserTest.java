package org.laotie777.lucence.chapter3;

import junit.framework.TestCase;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
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

    /**
     * 测试大小写
     */
    public void testLowerCase() throws ParseException {
        QueryParser parser = new QueryParser(Version.LUCENE_30,"field",new SimpleAnalyzer());
        Query query = parser.parse("prefixQuery*");
        assertEquals("prefixquery*",query.toString("field"));
        parser.setLowercaseExpandedTerms(false);
        Query query1 = parser.parse("prefixQuery*");
        assertEquals("prefixQuery*",query1.toString("field"));
        //toString()传入域的名称的就会显示域的查询条件 不显示前面的冒号
    }

    /**
     * 测试Phrase 双引号扩住的多个词汇会被解析成Phrase 单个词汇会被解析成termQuery 通配符不会被解析 停止词会被替换为“？”
     */
    public void testPhrase() throws ParseException {
        QueryParser parser = new QueryParser(Version.LUCENE_30,"field",new StandardAnalyzer(Version.LUCENE_30));
        Query query = parser.parse("\"This is Some Phrases*\"");
        assertEquals("\"? ? some phrases\"",query.toString("field"));
        Query query1 = parser.parse("\"term\"");
        assertTrue(query1 instanceof TermQuery);
    }

    /**
     * 测试Slop 设置slop就会变成slop查询
     */
    public void testSlop() throws ParseException {
        QueryParser parser = new QueryParser(Version.LUCENE_30,"field",new StandardAnalyzer(Version.LUCENE_30));
        parser.setPhraseSlop(5);
        Query query = parser.parse("\"Some Phrases\"");
        assertEquals("\"some phrases\"~5",query.toString("field"));
    }




}


































