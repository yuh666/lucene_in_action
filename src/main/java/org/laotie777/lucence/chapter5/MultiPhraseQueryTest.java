package org.laotie777.lucence.chapter5;

/**
 * Copyright Manning Publications Co.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific lan
 */

import junit.framework.TestCase;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.MultiPhraseQuery;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.IOException;

// From chapter 5
public class MultiPhraseQueryTest extends TestCase {
    private IndexSearcher searcher;

    @Override
    protected void setUp() throws Exception {
        Directory directory = new RAMDirectory();
        IndexWriter writer = new IndexWriter(directory,
                new WhitespaceAnalyzer(),
                IndexWriter.MaxFieldLength.UNLIMITED);
        Document doc1 = new Document();
        doc1.add(new Field("field",
                "the quick brown fox jumped over the lazy dog",
                Field.Store.YES, Field.Index.ANALYZED));
        writer.addDocument(doc1);
        Document doc2 = new Document();
        doc2.add(new Field("field",
                "the fast fox hopped over the hound",
                Field.Store.YES, Field.Index.ANALYZED));
        writer.addDocument(doc2);
        writer.close();

        searcher = new IndexSearcher(directory);
    }

    /**
     * 查询基本 是phrase查询的多级别版本 短语中出现的实际必须出现
     */
    public void testBasicQuery() throws IOException {
        MultiPhraseQuery multiPhraseQuery = new MultiPhraseQuery();
        multiPhraseQuery.add(new Term[]{new Term("field", "quick"), new Term("field", "fast")});
        multiPhraseQuery.add(new Term("field", "fox"));
        System.out.println(multiPhraseQuery);

        TopDocs docs = searcher.search(multiPhraseQuery, 10);
        multiPhraseQuery.setSlop(1);
        TopDocs docs1 = searcher.search(multiPhraseQuery, 10);

        System.out.println(docs.totalHits);
        System.out.println(docs1.totalHits);
    }

    private void debug(TopDocs hits) throws IOException {
        for (ScoreDoc sd : hits.scoreDocs) {
            Document doc = searcher.doc(sd.doc);
            System.out.println(sd.score + ": " + doc.get("field"));
        }

    }
}