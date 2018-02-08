package org.laotie777.lucence.chapter5;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.search.*;
import org.apache.lucene.util.DocIdBitSet;
import org.apache.lucene.util.OpenBitSet;
import org.laotie777.lucence.util.TestUtil;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

/**
 * @Author yuh
 * @Date Created in 上午11:22 2018/2/8
 * @Description
 */
public class SpecialAcessorFilter extends Filter {

    private SpecialAcessor specialAcessor;

    public SpecialAcessorFilter(SpecialAcessor acessor){
        this.specialAcessor = acessor;
    }

    @Override
    public DocIdSet getDocIdSet(IndexReader reader) throws IOException {
        OpenBitSet set = new OpenBitSet(reader.maxDoc());
        String[] isbn = specialAcessor.isbn();
        int[] docID = new int[1];
        int[] freq = new int[1];
        for (String s : isbn) {
            TermDocs isbn1 = reader.termDocs(new Term("isbn", s));
            isbn1.read(docID,freq);
            if(docID.length > 0){
                set.set(docID[0]);
            }
        }
        return set;
    }


    public static void main(String[] args) throws IOException {
        MatchAllDocsQuery allBooks = new MatchAllDocsQuery();
        IndexSearcher searcher = new IndexSearcher(TestUtil.getIndexDirectory());
        String[] isbns = new String[] {"9780061142666", "9780394756820"};
        SpecialAcessor accessor = new SpecialAcessor.TestSpecialsAccessor(isbns);
        Filter filter = new SpecialAcessorFilter(accessor);
        TopDocs hits = searcher.search(allBooks, filter, 10);
        assertEquals("the specials", isbns.length, hits.totalHits);
    }
}
