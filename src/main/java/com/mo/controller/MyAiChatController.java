package com.mo.controller;

import com.mo.tool.DateTimeTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

/**
 * 作者:Momo同学
 * 日期: 2026/7/15 23:03
 */
@RestController
public class MyAiChatController {
    @Autowired
    private ChatClient chatClient1;
    @Autowired
    private ChatClient chatClient2;
    @Autowired
    private ChatClient chatClient3;
    @Autowired
    private DateTimeTools dateTimeTools;


    /**
     * 阻塞式调用
     * @param question
     * @return
     */
    @RequestMapping("/ai1")
    public String ai(String question){
        return chatClient1.prompt()
                .user(question)
                .call()
                .content();
    }

    /**
     * 流式调用
     * @param question
     * @return
     */
    @RequestMapping(value = "/ai2",produces = "text/html;charset=utf-8")
    public Flux<String> ai2(String question){
        return chatClient2.prompt()
                .user(question)
                .stream()
                .content();
    }

    /**
     * 一套代码，适用所用场景
     * @param topic
     * @return
     */
    @RequestMapping(value = "/ask1 ",produces = "text/html;charset=utf-8")
    public Flux<String> ask(String topic){
        PromptTemplate template = new PromptTemplate("请回答，{topic}");
        Prompt prompt = template.create(Map.of("topic", topic));
        return chatClient2.prompt(prompt)
                .stream()
                .content();
    }

    /**
     * 配置链式提示词（简化版）
     * @param topic
     * @return
     */
    @RequestMapping(value = "/ask2",produces = "text/html;charset=utf-8")
    public Flux<String> ask2(String topic){
        return chatClient2.prompt()
                .system("你是一个MBTI分析小助手")
                .user(u ->u.text("指出{topic}这种人格的最优伴侣是哪种MBTI类型？").param("topic", topic))
                .stream()
                .content();
    }

    @RequestMapping(value = "/ask3",produces = "text/html;charset=utf-8")
    public Flux<String> ask3(String topic){
        return chatClient3.prompt()
                .user(topic)
                .stream()
                .content();
    }

    public record TranslationRequest(String text, List<String> languages) {

    }
    @RequestMapping(value = "/ask4",produces = "text/html;charset=utf-8")
    public String ask4(String topic){
        TranslationRequest translationRequest = chatClient3.prompt()
                .system("你是一个专业的中文翻译家")
                .user(promptUserSpec -> promptUserSpec.text("请用英文写出三句和{topic}相关的句子").param("topic", topic))
                .call()
                .entity(TranslationRequest.class);
        System.out.println(translationRequest);
        return translationRequest.text();
    }

    public record BookReview(  String title,
            String author,
            String rating,
            String comment){
    }
    @RequestMapping(value = "/ask5",produces = "text/html;charset=utf-8")
    public String ask5(String bookTheme){
        List<BookReview> bookReviews = chatClient3.prompt()
                .system("你是一个专业的评论家，你可以根据用户输入的书籍主题，去找到相应的书籍，并写出对书籍的评论。")
                .user(promptUserSpec -> promptUserSpec.text("请找出三本和{bookTheme}相关的书籍").param("bookTheme", bookTheme))
                .call()
                .entity(new ParameterizedTypeReference<List<BookReview>>() {} // 泛型类型引用
                );
        System.out.println(bookReviews);
        return bookReviews.toString();
    }

    @RequestMapping(value = "/aiPlus",produces = "text/html;charset=utf-8")
    public String aiPlus(String topic,String convId){
        return chatClient1.prompt()
                .user(topic)
                .advisors(u -> u.param(ChatMemory.CONVERSATION_ID, convId))
                .call()
                .content();
    }

    @RequestMapping("/aiTool")
    public String aiTool(String question){
        return chatClient3.prompt()
                .user(question)
                .tools(dateTimeTools)
                .call()
                .content();
    }

}