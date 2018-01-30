package org.laotie777.lucence.chapter3;

import junit.framework.TestCase;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.laotie777.lucence.util.TestUtil;

import java.io.IOException;

/**
 * @Author yuh
 * @Date Created in 上午9:07 2018/1/30
 * @Description Lucene的多样化检索
 */
public class TestTermQuery extends TestCase {
    protected IndexSearcher searcher;
    protected Directory directory;

    @Override
    public void setUp() {
        try {
            directory = TestUtil.getIndexDirectory();
            searcher = new IndexSearcher(directory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 字典范围检索
     *
     * @throws IOException
     */
    public void testRangeQueryText() throws IOException {
        TermRangeQuery query = new TermRangeQuery("title2", "d", "j", false, false);
        TopDocs docs = searcher.search(query, 10);
        assertEquals(3, docs.totalHits);
        searcher.close();
        directory.close();
    }

    /**
     * 数字范围检索
     *
     * @throws IOException
     */
    public void testRangeQueryNumber() throws IOException {
        NumericRangeQuery query = NumericRangeQuery.newIntRange("pubmonth", 200605, 200609, true, true);
        TopDocs docs = searcher.search(query, 10);
        assertEquals(1, docs.totalHits);
        searcher.close();
        directory.close();
    }

    /**
     * 前缀检索
     *
     * @throws IOException
     */
    public void testPrefixQuery() throws IOException {
        Term term = new Term("category", "/technology/computers/programming");
        TermQuery termQuery = new TermQuery(term);
        PrefixQuery prefixQuery = new PrefixQuery(term);
        assertTrue(searcher.search(termQuery, 100).totalHits < searcher.search(prefixQuery, 100).totalHits);
        searcher.close();
        directory.close();
    }

    /**
     * 布尔检索
     *
     * @throws IOException
     */
    public void testBooleanQuery() throws IOException {
        TermRangeQuery query = new TermRangeQuery("title2", "d", "j", false, false);
        NumericRangeQuery numQuery = NumericRangeQuery.newIntRange("pubmonth", 200605, 200609, true, true);

        BooleanQuery booleanQuery = new BooleanQuery();
        booleanQuery.add(query, BooleanClause.Occur.SHOULD);
        booleanQuery.add(numQuery, BooleanClause.Occur.SHOULD);

        TopDocs docs = searcher.search(booleanQuery, 100);
        System.out.println(docs.totalHits);

        searcher.close();
        directory.close();

    }

}
