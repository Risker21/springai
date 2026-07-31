package com.mo.controller;

import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * 作者:Momo同学
 * 日期: 2026/7/28 01:28
 */
@RestController
public class MyEmController {
    @Autowired
    private OpenAiEmbeddingModel openAiEmbeddingModel;
    @Autowired
    private VectorStore vectorStore;

    @RequestMapping("/em")
    public String Em(){
        float[] embed = openAiEmbeddingModel.embed("你好，CC");
        return embed.length+" "+ Arrays.toString(embed);
    }

    @RequestMapping("/em2")
    public String Em2(){
        List<Document> docs = List.of(
                new Document("你好，CC"),
                new Document("你好，Momo"),
                new Document("Momo喜欢CC"),
                new Document("CC不喜欢Momo"),
                new Document("CC很漂亮")
        );
        vectorStore.add(docs);
        return "ok";
    }

    @RequestMapping("/query")
    public String query(){
        List<Document> docs = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query("Momo喜欢CC吗")
                        .topK(3)
                        .build()
        );
        System.out.println(docs);
                return "ok";
    }
}
