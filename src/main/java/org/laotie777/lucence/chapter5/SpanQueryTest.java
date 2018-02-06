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
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.SpanQueryFilter;
import org.apache.lucene.search.spans.SpanFirstQuery;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanNotQuery;
import org.apache.lucene.search.spans.SpanOrQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.search.spans.Spans;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.io.StringReader;

// From chapter 5
public class SpanQueryTest extends TestCase {
    private RAMDirectory directory;
    private IndexSearcher searcher;
    private IndexReader reader;

    private SpanTermQuery quick;
    private SpanTermQuery brown;
    private SpanTermQuery red;
    private SpanTermQuery fox;
    private SpanTermQuery lazy;
    private SpanTermQuery sleepy;
    private SpanTermQuery dog;
    private SpanTermQuery cat;
    private Analyzer analyzer;

    @Override
    protected void setUp() throws Exception {
        directory = new RAMDirectory();

        analyzer = new WhitespaceAnalyzer();
        IndexWriter writer = new IndexWriter(directory,
                analyzer,
                IndexWriter.MaxFieldLength.UNLIMITED);

        Document doc = new Document();
        doc.add(new Field("f",
                "the quick brown fox jumps over the lazy dog",
                Field.Store.YES, Field.Index.ANALYZED));
        writer.addDocument(doc);

        doc = new Document();
        doc.add(new Field("f",
                "the quick red fox jumps over the sleepy cat",
                Field.Store.YES, Field.Index.ANALYZED));
        writer.addDocument(doc);

        writer.close();

        searcher = new IndexSearcher(directory);
        reader = searcher.getIndexReader();

        quick = new SpanTermQuery(new Term("f", "quick"));
        brown = new SpanTermQuery(new Term("f", "brown"));
        red = new SpanTermQuery(new Term("f", "red"));
        fox = new SpanTermQuery(new Term("f", "fox"));
        lazy = new SpanTermQuery(new Term("f", "lazy"));
        sleepy = new SpanTermQuery(new Term("f", "sleepy"));
        dog = new SpanTermQuery(new Term("f", "dog"));
        cat = new SpanTermQuery(new Term("f", "cat"));
    }

    private void assertOnlyBrownFox(Query query) throws Exception {
        TopDocs hits = searcher.search(query, 10);
        assertEquals(1, hits.totalHits);
        assertEquals("wrong doc", 0, hits.scoreDocs[0].doc);
    }

    private void assertBothFoxes(Query query) throws Exception {
        TopDocs hits = searcher.search(query, 10);
        assertEquals(2, hits.totalHits);
    }

    private void assertNoMatches(Query query) throws Exception {
        TopDocs hits = searcher.search(query, 10);
        assertEquals(0, hits.totalHits);
    }

    public void testSpanTermQuery() throws Exception {
        assertOnlyBrownFox(brown);
       // dumpSpans(brown);
        dumpSpans(new SpanTermQuery(new Term("f","the")));
    }

    public void testSpanFirstQuery() throws Exception {
        SpanFirstQuery sfq = new SpanFirstQuery(brown, 2);
        assertNoMatches(sfq);

        dumpSpans(sfq);

        sfq = new SpanFirstQuery(brown, 3);
        dumpSpans(sfq);
        assertOnlyBrownFox(sfq);
    }

    /**
     * span覆盖的部分 就是span
     * @throws Exception
     */
    public void testSpanNearQuery() throws Exception {
        SpanNearQuery sfq = new SpanNearQuery(new SpanQuery[]{quick,brown,dog}, 5,true);
        dumpSpans(sfq);
        assertOnlyBrownFox(sfq);

        SpanNearQuery sfq1 = new SpanNearQuery(new SpanQuery[]{lazy,fox}, 3,false);
        dumpSpans(sfq1);
        assertOnlyBrownFox(sfq1);
    }

    public void testSpanOrQuery() throws Exception {
        SpanNearQuery quick_fox =
                new SpanNearQuery(new SpanQuery[]{quick, fox}, 1, true);

        SpanNearQuery lazy_dog =
                new SpanNearQuery(new SpanQuery[]{lazy, dog}, 0, true);

        SpanNearQuery sleepy_cat =
                new SpanNearQuery(new SpanQuery[]{sleepy, cat}, 0, true);

        SpanNearQuery qf_near_ld =
                new SpanNearQuery(
                        new SpanQuery[]{quick_fox, lazy_dog}, 3, true);
        assertOnlyBrownFox(qf_near_ld);
        dumpSpans(qf_near_ld);

        SpanNearQuery qf_near_sc =
                new SpanNearQuery(
                        new SpanQuery[]{quick_fox, sleepy_cat}, 3, true);
        dumpSpans(qf_near_sc);

        SpanOrQuery or = new SpanOrQuery(
                new SpanQuery[]{qf_near_ld, qf_near_sc});
        assertBothFoxes(or);
        dumpSpans(or);
    }




    /**
     * spanQuery带着查询结果在原文档某个域中的位置
     * @param query
     * @throws IOException
     */
    private void dumpSpans(SpanQuery query) throws IOException {
        Spans spans = query.getSpans(reader);
        System.out.println(query + ":");
        int numSpans = 0;

        TopDocs hits = searcher.search(query, 10);
        float[] scores = new float[2];
        for (ScoreDoc sd : hits.scoreDocs) {
            scores[sd.doc] = sd.score;
        }

        while (spans.next()) {                 // A
            numSpans++;

            int id = spans.doc();
            Document doc = reader.document(id);  // B

            TokenStream stream = analyzer.tokenStream("contents",      // C
                    new StringReader(doc.get("f")));   // C
            TermAttribute term = stream.addAttribute(TermAttribute.class);

            StringBuilder buffer = new StringBuilder();
            buffer.append("   ");
            int i = 0;
            while (stream.incrementToken()) {     // D
                if (i == spans.start()) {          // E
                    buffer.append("<");              // E
                }                                  // E
                buffer.append(term.term());        // E
                if (i + 1 == spans.end()) {        // E
                    buffer.append(">");              // E
                }                                  // E
                buffer.append(" ");
                i++;
            }
            buffer.append("(").append(scores[id]).append(") ");
            System.out.println(buffer);
        }

        if (numSpans == 0) {
            System.out.println("   No spans");
        }
        System.out.println();
    }

    // A Step through each span
    // B Retrieve document
    // C Re-analyze text
    // D Step through all tokens
    // E Print < and > around span
}
