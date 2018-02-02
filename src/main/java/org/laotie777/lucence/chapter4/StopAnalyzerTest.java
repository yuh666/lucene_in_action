package org.laotie777.lucence.chapter4;

import junit.framework.TestCase;
import org.laotie777.lucence.util.AnalyzerUtils;

import java.io.IOException;

/**
 * @Author yuh
 * @Date Created in 下午1:18 2018/2/2
 * @Description 停止词分析器测试
 */
public class StopAnalyzerTest extends TestCase{

    /**
     * 测试自定义停止词分词器
     */
    public void testStopAnalyzer2() throws IOException {
        AnalyzerUtils.assrtOutputEuqalTExpected(new StopAnalyzer2(),"The quick fox",new String[]{"quick","fox"});
    }

}
