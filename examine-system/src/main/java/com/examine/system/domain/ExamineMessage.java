package com.examine.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.examine.common.annotation.Excel;
import com.examine.common.core.domain.BaseEntity;

/**
 * 审批通知对象 examine_message
 *
 * @author Evan-WH
 * @date 2022-03-09
 */
public class ExamineMessage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 通知编号 */
    private Long id;

    /** 通知标题 */
    @Excel(name = "通知标题")
    private String messageTitle;

    /** 通知内容 */
    @Excel(name = "通知内容")
    private String messageContent;

    /** 创建者 */
    @Excel(name = "创建者")
    private String messageCreateby;

    /** 接收人 */
    @Excel(name = "接收人")
    private String roleId;

    /** 接收状态（0:未读，1：已读） */
    @Excel(name = "接收状态", readConverterExp = "0=:未读，1：已读")
    private Integer messageStatus;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date messageTime;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setMessageTitle(String messageTitle)
    {
        this.messageTitle = messageTitle;
    }

    public String getMessageTitle()
    {
        return messageTitle;
    }
    public void setMessageContent(String messageContent)
    {
        this.messageContent = messageContent;
    }

    public String getMessageContent()
    {
        return messageContent;
    }
    public void setMessageCreateby(String messageCreateby)
    {
        this.messageCreateby = messageCreateby;
    }

    public String getMessageCreateby()
    {
        return messageCreateby;
    }
    public void setRoleId(String roleId)
    {
        this.roleId = roleId;
    }

    public String getRoleId()
    {
        return roleId;
    }
    public void setMessageStatus(Integer messageStatus)
    {
        this.messageStatus = messageStatus;
    }

    public Integer getMessageStatus()
    {
        return messageStatus;
    }
    public void setMessageTime(Date messageTime)
    {
        this.messageTime = messageTime;
    }

    public Date getMessageTime()
    {
        return messageTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("messageTitle", getMessageTitle())
                .append("messageContent", getMessageContent())
                .append("messageCreateby", getMessageCreateby())
                .append("roleId", getRoleId())
                .append("messageStatus", getMessageStatus())
                .append("messageTime", getMessageTime())
                .toString();
    }
}
