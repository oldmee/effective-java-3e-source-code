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

    //日志
    private static final Logger LOG = Logger.getLogger("SensitiveWordTree");
    // 根节点
    private static SensitiveWordNode root = null;
    // 敏感词库编码
    private static final String ENCODING = "gbk";
    // 敏感词库位置
    private static final String filePath = "D:/1.txt";

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
    private static List censorWords(String text) {
        if (root == null) {
            init();
        }
        List sensitiveWords = new ArrayList();
        char[] text_to_char = text.toCharArray();
        for (int start = 0; start < text_to_char.length; start++) {
            SensitiveWordNode sensitiveWordNode = root.getNextNode(text_to_char[start]);

            if (null != sensitiveWordNode) {

                while (sensitiveWordNode.getNextNodes() == null) {

                    sensitiveWords.add(sensitiveWordNode.getKey());
//                    sensitiveWordNode.
                }

                sensitiveWords.add(sensitiveWordNode.getKey());
                if (sensitiveWordNode.isEnd()) {
                    sensitiveWords.add(",");
                } else {

                }
            }

        }
        return sensitiveWords;
    }
    public static void main(String[] args) {
        init();
        System.out.println(root);
        List list = censorWords("我今天去上数学课");
        System.out.println(list);
    }

}
