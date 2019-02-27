package effectivejava.sensitiveword;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * 敏感词树
 * Created by ScienJus on 2015/7/19.
 */
public class SensitiveWordTree {

    // 日志
    private static final Logger LOG = Logger.getLogger("SensitiveWordTree");
    // 根节点
    private static SensitiveWordNode root = null;
    // 敏感词库编码
    private static final String ENCODING = "utf-8";
    // 敏感词库位置
    private static final String filePath = "/Users/lirenren/intellijProjects/effective-java-3e-source-code/resource/1.txt";

    /**
     * 读取敏感词库
     *
     * @return
     */
    private static Set readSensitiveWords() {
        Set keyWords = new HashSet();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), ENCODING));
            String line;
            while ((line = reader.readLine()) != null) {
                keyWords.add(line.trim());
            }
        } catch (UnsupportedEncodingException e) {
            LOG.info("敏感词库编码错误!");
        } catch (FileNotFoundException e) {
            LOG.info("敏感词库不存在!");
        } catch (IOException e) {
            LOG.info("读取敏感词库失败!");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    LOG.info("读取敏感词库失败!");
                }
            }
        }
        return keyWords;
    }

    /**
     * 初始化敏感词库
     */
    private static void init() {
        // 读取敏感词库
        Set<String> keyWords = readSensitiveWords();
        // 初始化根节点
        root = new SensitiveWordNode(' ');
        // 创建敏感词
        for (String keyWord : keyWords) {
            createSensitiveWordNode(keyWord);
        }
    }

    /**
     * 构建敏感词
     *
     * @param keyWord
     */
    private static void createSensitiveWordNode(String keyWord) {
        if (root == null) {
            LOG.info("根节点不存在!");
            return;
        }
        SensitiveWordNode nowNode = root;
        for (Character c : keyWord.toCharArray()) {
            SensitiveWordNode nextNode = nowNode.getNextNode(c);
            if (nextNode == null) {
                nextNode = new SensitiveWordNode(c);
                nowNode.putNextNode(nextNode);
            }
            nowNode = nextNode;
        }
        nowNode.setEnd(true);
    }

    /**
     * 检查敏感词
     *
     * @return 所有查出的敏感词
     */
    private static String censorWords(String text) {
        if (root == null) {
            init();
        }
        StringBuilder sensitiveWords = new StringBuilder();
        StringBuilder temp_sensitiveWords = new StringBuilder();
        char[] text_to_char = text.toCharArray();
        SensitiveWordNode sensitiveWordNode = root;
        SensitiveWordNode this_sensitiveWordNode = null;
        for (int start = 0; start < text_to_char.length; start++) {
            SensitiveWordNode temp_sensitiveWordNode = sensitiveWordNode.getNextNode(text_to_char[start]);

            if (temp_sensitiveWordNode != null) {
                temp_sensitiveWords = new StringBuilder();
                temp_sensitiveWords.append("(" + temp_sensitiveWordNode.getKey());
                this_sensitiveWordNode = temp_sensitiveWordNode;
            }

            SensitiveWordNode temp = this_sensitiveWordNode.getNextNode(text_to_char[start]);
            if (this_sensitiveWordNode != null
                    && temp != null
                    && temp.getKey() == text_to_char[start]) {
                if (temp.isEnd()
                        && temp.getNextNodes().size() == 0) {
                    sensitiveWords.append(temp_sensitiveWords);
                    sensitiveWords.append(text_to_char[start] + "),");
                    temp_sensitiveWords = new StringBuilder();
                } else {
                    temp_sensitiveWords.append(text_to_char[start]);
                }
                continue;
            }

        }
        return sensitiveWords.toString();
    }

    public static void main(String[] args) {
        init();
        System.out.println(root);
        String list = censorWords("数学和英语还有语文");
        System.out.println(list);
    }

}
