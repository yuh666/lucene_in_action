package org.laotie777.lucence.chapter5;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.CharStream;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.QueryParserTokenManager;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.util.Version;

/**
 * @Author yuh
 * @Date Created in 上午10:20 2018/2/8
 * @Description spanNearQuery就是能够处理方向的phraseQuery
 */
public class CustomQueryParser extends QueryParser {
    public CustomQueryParser(Version matchVersion, String f, Analyzer a) {
        super(matchVersion, f, a);
    }

    @Override
    protected Query getFuzzyQuery(String field, String termStr, float minSimilarity) throws ParseException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Query getWildcardQuery(String field, String termStr) throws ParseException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Query getRangeQuery(String field, String part1, String part2, boolean inclusive) throws ParseException {
        TermRangeQuery termRangeQuery = (TermRangeQuery) super.getRangeQuery(field, part1, part2, inclusive);
        if ("price".equals(field)) {
            return NumericRangeQuery.newDoubleRange(field, Double.parseDouble(termRangeQuery.getLowerTerm()), Double.parseDouble(termRangeQuery.getUpperTerm()), termRangeQuery.includesLower(), termRangeQuery.includesUpper());
        } else {
            return termRangeQuery;
        }
    }

    @Override
    protected Query getFieldQuery(String field, String queryText) throws ParseException {
        Query query = super.getFieldQuery(field, queryText);
        if(!(query instanceof PhraseQuery)){
            return query;
        }

        Term[] terms = ((PhraseQuery) query).getTerms();
        SpanQuery[] spans = new SpanTermQuery[terms.length];
        int i = 0;
        for (Term term : terms) {
            spans[i++] = new SpanTermQuery(term);
        }
        SpanNearQuery spanNearQuery = new SpanNearQuery(spans,((PhraseQuery) query).getSlop(),true);
        return spanNearQuery;
    }

    public static void main(String[] args) throws ParseException {
        CustomQueryParser contents = new CustomQueryParser(Version.LUCENE_30, "contents", new WhitespaceAnalyzer());
        Query parse = contents.parse("\"a phrase\"");
        System.out.println(parse.getClass().getSimpleName());

    }
}
