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
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
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
import org.laotie777.lucence.chapter4.SynonymAnalyzer;
import org.laotie777.lucence.chapter4.SynonymEngine;
import org.laotie777.lucence.util.TestUtil;

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


    public void testQueryParser() throws ParseException {
        QueryParser parser = new QueryParser(Version.LUCENE_30, "field", new SynonymAnalyzer(new SynonymEngine() {
            @Override
            public String[] getSynonyms(String s) {
                if ("quick".equals(s)) {
                    return new String[]{"fast"};
                }
                return null;
            }
        }
        ));

        Query parse = parser.parse("\"quick fox\"");
        assertTrue(parse instanceof MultiPhraseQuery);
        System.out.println(parse);

    }

    /**
     * 多值域检索
     */
    public void testMultiQuery() throws ParseException, IOException {
        MultiFieldQueryParser multiFieldQueryParser = new MultiFieldQueryParser(Version.LUCENE_30, new String[]{"title", "subject"}, new SimpleAnalyzer());
        Query query = multiFieldQueryParser.parse("development");
        searcher = new IndexSearcher(TestUtil.getIndexDirectory());
        TopDocs docs = searcher.search(query, 10);
        TestUtil.printRes(docs,searcher);
    }

    /**
     * 多值域检索2
     */
    public void testMultiQuery2() throws ParseException, IOException {
        Query query = MultiFieldQueryParser.parse(Version.LUCENE_30, "lucene",new String[]{"title", "subject"}, new BooleanClause.Occur[]{BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD},new SimpleAnalyzer());
        searcher = new IndexSearcher(TestUtil.getIndexDirectory());
        TopDocs docs = searcher.search(query, 10);
        TestUtil.printRes(docs,searcher);
    }


    private void debug(TopDocs hits) throws IOException {
        for (ScoreDoc sd : hits.scoreDocs) {
            Document doc = searcher.doc(sd.doc);
            System.out.println(sd.score + ": " + doc.get("field"));
        }

    }
}