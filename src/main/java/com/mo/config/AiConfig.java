package com.mo.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
// import org.springframework.ai.chat.client.advisor.ToolCallingAdvisor; // 移除未使用的导入
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 作者:Momo同学
 * 日期: 2026/7/15 22:59
 */
@Configuration
public class AiConfig {

    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .maxMessages(10) // 记住最近10条消息的上下文
                .chatMemoryRepository(new InMemoryChatMemoryRepository()) // 使用内存存储
                .build();
    }

    @Bean
    public ChatClient chatClient1(OpenAiChatModel model, ChatMemory chatMemory) {
        String systemPrompt = "你是一个专业的中文翻译家,你可以将用户输入的英文翻译成中文，将中文翻译成英文";
        return ChatClient
                .builder(model)
//                .defaultAdvisors(new MySimpleLoggerAdvisor()) // 添加自定义日志Advisor
                .defaultAdvisors(new SimpleLoggerAdvisor(), // 添加默认日志Advisor
                        MessageChatMemoryAdvisor.builder(chatMemory).build() // 添加聊天记录Advisor
                )
                .defaultSystem(systemPrompt)
                .build();
    }

    @Bean
    public ChatClient chatClient2(OllamaChatModel model) {
        return ChatClient
                .builder(model)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    /**
     * 配置一个默认的系统提示的ChatClient
     * @param model
     * @return
     */
    @Bean
    public ChatClient chatClient3(OpenAiChatModel model) {
//        String systemPrompt = "你是一个专业的中文翻译家,你可以将用户输入的英文翻译成中文，将中文翻译成英文";
        return ChatClient
                .builder(model)
//                .defaultSystem(systemPrompt)
                .defaultAdvisors(new SimpleLoggerAdvisor()
//                        MessageChatMemoryAdvisor.builder(chatMemory()).build()
                        )
                .build();
    }
}