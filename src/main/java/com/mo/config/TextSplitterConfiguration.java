package com.mo.config;

import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 作者:Momo同学
 * 日期: 2026/7/30 04:39
 */
@Component
public class TextSplitterConfiguration {
    @Bean
    public TokenTextSplitter tokenTextSplitter() {
        return TokenTextSplitter.builder()
                .withChunkSize(512)
                .withMaxNumChunks(1000)
                .build();
    }
}
