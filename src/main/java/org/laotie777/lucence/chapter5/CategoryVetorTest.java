package org.laotie777.lucence.chapter5;

import junit.framework.TestCase;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermFreqVector;
import org.laotie777.lucence.util.TestUtil;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Author yuh
 * @Date Created in 上午10:56 2018/2/7
 * @Description
 */
public class CategoryVetorTest extends TestCase {
    Map<String, Map<String, Integer>> categoryMap;

    @Override
    protected void setUp() throws Exception {
        categoryMap = new TreeMap<>();
        buildCategoryVectors();
    }

    /**
     * 建立category到向量的映射
     */
    private void buildCategoryVectors() throws IOException {
        IndexReader reader = IndexReader.open(TestUtil.getIndexDirectory());
        int maxDoc = reader.maxDoc();
        for (int i = 0; i < maxDoc; i++) {
            Document document = reader.document(i);
            String category = document.get("category");
            Map<String, Integer> termIntegerMap = categoryMap.get(category);
            if (termIntegerMap == null) {
                termIntegerMap = new TreeMap<>();
                categoryMap.put(category, termIntegerMap);
            }
            TermFreqVector vector = reader.getTermFreqVector(i, "subject");
            addToTermVector(category, vector);
        }
    }

    /**
     * 添加索引到vetorMap
     *
     * @param category
     * @param vector
     */
    private void addToTermVector(String category, TermFreqVector vector) {
        Map<String, Integer> termIntegerMap = categoryMap.get(category);
        String[] terms = vector.getTerms();
        int[] termFrequencies = vector.getTermFrequencies();
        for (int i = 0; i < terms.length; i++) {
            Integer integer = termIntegerMap.get(terms[i]);
            if (integer == null) {
                termIntegerMap.put(terms[i], termFrequencies[i]);
            } else {
                termIntegerMap.put(terms[i], termFrequencies[i] + integer);
            }
        }
    }

    /**
     * 根据subject获取类别 求输入的subject和类别Map里面的项向量夹角
     *
     * @param subject
     * @return
     */
    public String getCategory(String subject) {
        String[] words = subject.split(" ");
        double bestAngle = Double.MAX_VALUE;
        String category = "";


        Iterator<String> iterator = categoryMap.keySet().iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            double angle = computeAngle(words, next);
            if (angle < bestAngle) {
                bestAngle = angle;
                category = next;
            }
        }
        return category;
    }

    /**
     * 计算项向量夹角
     *
     * @param words
     * @param next
     * @return
     */
    private double computeAngle(String[] words, String category) {
        Map<String, Integer> vectorMap = (Map) categoryMap.get(category);

        int dotProduct = 0;
        int sumOfSquares = 0;
        for (String word : words) {
            int categoryWordFreq = 0;

            if (vectorMap.containsKey(word)) {
                categoryWordFreq = vectorMap.get(word);
            }

            dotProduct += categoryWordFreq;  //#1
            sumOfSquares += categoryWordFreq * categoryWordFreq;
        }


        double denominator;
        if (sumOfSquares == words.length) {
            denominator = sumOfSquares; // #2
        } else {
            denominator = Math.sqrt(sumOfSquares) *
                    Math.sqrt(words.length);
        }

        double ratio = dotProduct / denominator;

        return Math.acos(ratio);
    }

}
