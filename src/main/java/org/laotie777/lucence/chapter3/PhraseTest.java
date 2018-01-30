package org.laotie777.lucence.chapter3;

import junit.framework.TestCase;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.laotie777.lucence.util.TestUtil;

import java.io.IOException;

/**
 * @Author yuh
 * @Date Created in 上午9:57 2018/1/30
 * @Description 短语检索测试 Phrase的slop就是传入的序列为了和文章匹配所能移动的最大的次数的总和
 */
public class PhraseTest extends TestCase {
    protected IndexSearcher searcher;
    protected Directory directory;
    protected IndexWriter indexWriter;

    @Override
    public void setUp() throws IOException {
        directory = new RAMDirectory();
        indexWriter = new IndexWriter(directory,new WhitespaceAnalyzer(), IndexWriter.MaxFieldLength.UNLIMITED);
        Document document = new Document();
        document.add(new Field("field","the quick brown fox jumped over the lazy dog", Field.Store.YES, Field.Index.ANALYZED));
        indexWriter.addDocument(document);
        indexWriter.commit();
        indexWriter.close();
        searcher = new IndexSearcher(directory);
    }

    @Override
    protected void tearDown() throws Exception {
        searcher.close();
        directory.close();
    }

    /**
     * 是否匹配
     * @param phrases
     * @param searcher
     * @return
     */
    protected boolean match(String[] phrases,IndexSearcher searcher,int slot) throws IOException {
        PhraseQuery phraseQuery = new PhraseQuery();
        phraseQuery.setSlop(slot);
        for (String phrase : phrases) {
            phraseQuery.add(new Term("field",phrase));
        }
        TopDocs docs = searcher.search(phraseQuery, 100);
        return docs.totalHits > 0;
    }

    /**
     * 两个词检索
     * @throws IOException
     */
    public void testTwoPhrases() throws IOException {
        assertTrue(match(new String[]{"fox","quick"},searcher,2));
    }

    /**
     * 三个词检索
     * @throws IOException
     */
    public void testThreePhrases() throws IOException {
        assertTrue(match(new String[]{"lazy","jumped","quick"},searcher,7));
    }


}
