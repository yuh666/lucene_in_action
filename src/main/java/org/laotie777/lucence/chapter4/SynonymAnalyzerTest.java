package org.laotie777.lucence.chapter4;

import junit.framework.TestCase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.laotie777.lucence.util.AnalyzerUtils;
import org.laotie777.lucence.util.TestUtil;

import java.io.IOException;
import java.io.StringReader;

/**
 * 同义词分析器测试
 */
public class SynonymAnalyzerTest extends TestCase {


    protected IndexSearcher searcher;
    protected IndexWriter writer;
    protected Directory directory;


    @Override
    protected void setUp() throws Exception {
        directory = new RAMDirectory();
        writer = new IndexWriter(directory, new SynonymAnalyzer(new SynonymEngineImpl()), IndexWriter.MaxFieldLength.UNLIMITED);
        Document document = new Document();
        document.add(new Field("field", "The quick brown fox jumps over the lazy dog", Field.Store.YES, Field.Index.ANALYZED));
        writer.addDocument(document);
        writer.optimize();
        writer.commit();
        writer.close();
        searcher = new IndexSearcher(directory);
    }

    public void testIncrePos() throws IOException {
        SynonymAnalyzer synonymAnalyzer = new SynonymAnalyzer(new SynonymEngineImpl());
        TokenStream tokenStream = synonymAnalyzer.tokenStream("field", new StringReader("jumps"));

        TermAttribute termAttribute = tokenStream.addAttribute(TermAttribute.class);
        PositionIncrementAttribute positionIncrementAttribute = tokenStream.addAttribute(PositionIncrementAttribute.class);

        String[] strs = new String[]{"jumps", "leaps", "hops"};
        int i = 0;
        while (tokenStream.incrementToken()) {
            assertEquals(termAttribute.term(), strs[i]);
            int expectedPos = 0;
            expectedPos = i == 0 ? 1 : 0;
            assertEquals(expectedPos, positionIncrementAttribute.getPositionIncrement());
            i++;
        }
        assertEquals(3, i);
    }

    /**
     * 測試單項測試和短語測試
     * @throws IOException
     */
    public void testTermAndPhrase() throws IOException {
        Term term = new Term("field","hops");
        TermQuery tq = new TermQuery(term);
        assertEquals(TestUtil.hitCount(searcher,tq),1);

        PhraseQuery pq = new PhraseQuery();
        pq.add(new Term("field","fox"));
        pq.add(term);
        assertEquals(TestUtil.hitCount(searcher,pq),1);
    }

    /**
     * 測試分析器對queryParse的分詞效果 被“”包圍就變成PhraseQuery 任何都需要先轉換為Term 在進行組合
     */
    public void testAnalyzeQuery() throws ParseException, IOException {
        QueryParser parser = new QueryParser(Version.LUCENE_30,"field",new SynonymAnalyzer(new SynonymEngineImpl()));
        Query query = parser.parse("\"fox jumps\"");
        System.out.println(query.toString("field"));
        QueryParser parser1 = new QueryParser(Version.LUCENE_30,"field",new StandardAnalyzer(Version.LUCENE_30));
        Query query1 = parser1.parse("\"fox jumps\"");
        System.out.println(query1.getClass().getSimpleName());
        System.out.println(query1.toString("field"));
        TopDocs docs = searcher.search(query,10);
        System.out.println(docs.totalHits);
    }

    /**
     * 顯示位置增量信息 位置增量為0 則索引在同一位置 指向同一個文檔連
     */
    public void testDisIncrement() throws IOException {
        AnalyzerUtils.displayTokensWithIncrement(new SynonymAnalyzer(new SynonymEngineImpl()),"The quick brown fox jumps over the lazy dog");
    }

    @Override
    protected void tearDown() throws Exception {
        searcher.close();
    }
}
