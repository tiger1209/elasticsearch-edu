package com.edu.esedu.model;

/**
 * @Author wangcunlei
 * @Date 2020-1-2 14:39
 * @Description
 */
public class Message {
    private long msgId;
    private String content;
    private String createTime;

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Message{" +
            "msgId=" + msgId +
            ", content='" + content + '\'' +
            ", createTime='" + createTime + '\'' +
            '}';
    }
}
