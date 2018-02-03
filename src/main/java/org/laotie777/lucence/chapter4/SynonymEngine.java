package org.laotie777.lucence.chapter4;

/**
 * 同义词引擎接口
 */
public interface SynonymEngine {
    /**
     * 获取字符串的同义词
     * @param s
     * @return
     */
    String[] getSynonyms(String s);
}
