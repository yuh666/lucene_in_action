package org.laotie777.lucence.chapter4;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;
import org.laotie777.lucence.util.AnalyzerUtils;

import java.io.IOException;

/**
 * 分析器分词分词
 */
public class AnalyzerTermTest {

    private static final Analyzer[] analyers = {new WhitespaceAnalyzer(), new SimpleAnalyzer(), new StopAnalyzer(Version.LUCENE_30), new StandardAnalyzer(Version.LUCENE_30)};
    private static final String[] examples = {"the quick brown fox jumped over the lazy dog", "XY&Z Corporation - xyz@example.com"};

    public static void main(String[] args) throws IOException {
       /* String[] strings = args.length > 0 ? args : examples;
        for (String string : strings) {
            analyze(string);
        }*/
        AnalyzerUtils.displayTokensWithFullDetails(new SimpleAnalyzer(),"the quick brown fox");
    }

    /**
     * 分析语句
     *
     * @param text
     */
    public static void analyze(String text) throws IOException {
        System.out.println("Analyzing " + "\"" + text + "\"");
        for (Analyzer analyzer : analyers) {
            String simpleName = analyzer.getClass().getSimpleName();
            System.out.println("analyzer is " + simpleName);
            AnalyzerUtils.displayTokens(analyzer,text);
            System.out.println();
        }
    }


}
