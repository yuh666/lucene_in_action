package org.laotie777.lucence.chapter4;

import junit.framework.TestCase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

import java.io.IOException;
import java.io.StringReader;

/**
 * 同义词分析器测试
 */
public class SynonymAnalyzerTest extends TestCase {

    public void testIncrePos() throws IOException {
        SynonymAnalyzer synonymAnalyzer = new SynonymAnalyzer();
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
}
