package com.example.backend.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class MessageDTO {
    private Long id;
    @NotNull(message = "ID cannot be null")

    @NotEmpty(message = "Content cannot be empty")
    private String content;

    @NotNull(message = "Sender ID cannot be null")
    private Long senderId;

    @NotNull(message = "Recipient ID cannot be null")
    private Long recipientId;

    @NotNull(message = "Conversation ID cannot be null")
    private Long conversationId;

    public MessageDTO() {}

    public MessageDTO(Long id, String content, Long senderId, Long recipientId, Long conversationId) {
        this.id = id;
        this.content = content;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.conversationId = conversationId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }
}
