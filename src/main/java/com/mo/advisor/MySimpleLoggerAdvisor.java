package com.mo.advisor;


import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import reactor.core.publisher.Flux;

/**
 * 作者:Momo同学
 * 日期: 2026/7/16 18:33
 */

public class MySimpleLoggerAdvisor implements CallAdvisor, StreamAdvisor {

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {
        System.out.println("发送阻塞请求：" + request);
        ChatClientResponse response = chain.nextCall(request);
        System.out.println("接收到响应:：" + response);
        return response;
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest request, StreamAdvisorChain chain) {
        System.out.println("发送流式请求：" + request);
        return chain.nextStream(request)
                .doOnNext(response -> System.out.println("接收到流式响应:" + response));

    }

    @Override
    public String getName() {
        return "简单日志Advisor";
    }
    /**
     * 执行顺序
     * 优先级越高，越先执行
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
