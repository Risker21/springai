package com.mo.util;

import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import java.io.File;
import java.util.List;

/**
 * 作者:Momo同学
 * 日期: 2026/7/30 04:35
 */
@Component
public class DocumentParseUtil {
    public List<Document> parse(String filePath) {
        File file = new File(filePath);
        String suffix = filePath.substring(filePath.lastIndexOf('.') + 1).toLowerCase();
        Resource resource = new FileSystemResource(file);
        DocumentReader reader;
        switch (suffix) {
            case "pdf":
            case "doc":
            case "docx":
            case "txt":
            case "text":
                reader = new TikaDocumentReader(resource);
                break;
            case "md":
            case "markdown":
                reader = new MarkdownDocumentReader(file.toURI().toString());
                break;
            default:
                throw new IllegalArgumentException("不支持的文件格式: " + suffix);
        }
        return reader.get();
    }
}