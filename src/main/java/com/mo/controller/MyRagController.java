package com.mo.controller;

import com.mo.util.DocumentParseUtil;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 作者:Momo同学
 * 日期: 2026/7/30 04:54
 */
@RestController
public class MyRagController {
    @Autowired
    private VectorStore vectorStore;
    @Autowired
    private DocumentParseUtil documentParseUtil;
    @Autowired
    private TokenTextSplitter tokenTextSplitter;

    @RequestMapping("/addDocs")
    public String addDocs() {
        addChunkedDocuments("C:\\Users\\32252\\Desktop\\Git讲义.pdf", "Git讲义.pdf");
        addChunkedDocuments("C:\\Users\\32252\\Desktop\\零基础入门LangChain：Model与Agent实战指南.pdf", "零基础入门LangChain：Model与Agent实战指南.pdf");
        return "OK";
    }

    /**
     * 解析后按 token 切分再入库，避免单条 Document 超过嵌入模型输入上限
     */
    private void addChunkedDocuments(String filePath, String label) {
        List<Document> raw = documentParseUtil.parse(filePath);
        List<Document> chunks = tokenTextSplitter.apply(raw);
        vectorStore.add(chunks);
        System.out.println(label + ",向量存储添加成功!（共 " + chunks.size() + " 块）");
    }
}
