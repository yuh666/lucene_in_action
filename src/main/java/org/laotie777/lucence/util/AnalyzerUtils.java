package org.laotie777.lucence.util;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.junit.Assert;

import java.io.IOException;
import java.io.StringReader;

/**
 * 分析器工具类
 */
public class AnalyzerUtils {
    /**
     * 将单元转成词汇单元流
     *
     * @param analyzer
     * @param text
     * @throws IOException
     */
    public static void displayTokens(Analyzer analyzer, String text) throws IOException {
        displayTokens(analyzer.tokenStream("field", new StringReader(text)));
    }

    /**
     * 显示被切分的单元
     *
     * @param tokenStream
     * @throws IOException
     */
    public static void displayTokens(TokenStream tokenStream) throws IOException {
        TermAttribute term = tokenStream.addAttribute(TermAttribute.class);
        while (tokenStream.incrementToken()) {
            System.out.print("[" + term.term() + "]" + "\t");
        }
    }

    /**
     * 词汇单元的详细信息
     *
     * @param analyzer
     * @param text
     */
    public static void displayTokensWithFullDetails(Analyzer analyzer, String text) throws IOException {
        TokenStream stream = analyzer.tokenStream("field", new StringReader(text));
        TermAttribute termAttribute = stream.addAttribute(TermAttribute.class);
        PositionIncrementAttribute positionIncrementAttribute = stream.addAttribute(PositionIncrementAttribute.class);
        OffsetAttribute offsetAttribute = stream.addAttribute(OffsetAttribute.class);
        TypeAttribute typeAttribute = stream.addAttribute(TypeAttribute.class);
        int position = 0;
        while (stream.incrementToken()) {
            int positionIncrement = positionIncrementAttribute.getPositionIncrement();
            if (positionIncrement > 0) {
                position += positionIncrement;
                System.out.println();
                System.out.print(position + ": ");
            }
            System.out.print(
                    "[" + termAttribute.term() + ":" + offsetAttribute.startOffset() + "->" + offsetAttribute.endOffset() + "]" + ":" + typeAttribute.type()
            );
        }
    }

    /**
     * 断言输出和预期是一致的
     *
     * @param analyzer
     * @param line
     * @param terms
     */
    public static void assrtOutputEuqalTExpected(Analyzer analyzer, String line, String[] terms) throws IOException {
        TokenStream stream = analyzer.tokenStream("field", new StringReader(line));
        TermAttribute termAttribute = stream.addAttribute(TermAttribute.class);
        for (String term : terms) {
            Assert.assertTrue(stream.incrementToken());
            Assert.assertEquals(term,termAttribute.term());
        }
        Assert.assertFalse(stream.incrementToken());
        stream.close();
    }


}
