package com.examine.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.examine.system.mapper.ExamineMessageMapper;
import com.examine.system.domain.ExamineMessage;
import com.examine.system.service.IExamineMessageService;

/**
 * 审批通知Service业务层处理
 *
 * @author Evan-WH
 * @date 2022-03-09
 */
@Service
public class ExamineMessageServiceImpl implements IExamineMessageService
{
    @Autowired
    private ExamineMessageMapper examineMessageMapper;

    /**
     * 查询审批通知
     *
     * @param id 审批通知主键
     * @return 审批通知
     */
    @Override
    public ExamineMessage selectExamineMessageById(Long id)
    {
        return examineMessageMapper.selectExamineMessageById(id);
    }

    /**
     * 查询审批通知列表
     *
     * @param examineMessage 审批通知
     * @return 审批通知
     */
    @Override
    public List<ExamineMessage> selectExamineMessageList(ExamineMessage examineMessage)
    {
        return examineMessageMapper.selectExamineMessageList(examineMessage);
    }

    /**
     * 新增审批通知
     *
     * @param examineMessage 审批通知
     * @return 结果
     */
    @Override
    public int insertExamineMessage(ExamineMessage examineMessage)
    {
        return examineMessageMapper.insertExamineMessage(examineMessage);
    }

    /**
     * 修改审批通知
     *
     * @param examineMessage 审批通知
     * @return 结果
     */
    @Override
    public int updateExamineMessage(ExamineMessage examineMessage)
    {
        return examineMessageMapper.updateExamineMessage(examineMessage);
    }

    /**
     * 批量删除审批通知
     *
     * @param ids 需要删除的审批通知主键
     * @return 结果
     */
    @Override
    public int deleteExamineMessageByIds(Long[] ids)
    {
        return examineMessageMapper.deleteExamineMessageByIds(ids);
    }

    /**
     * 删除审批通知信息
     *
     * @param id 审批通知主键
     * @return 结果
     */
    @Override
    public int deleteExamineMessageById(Long id)
    {
        return examineMessageMapper.deleteExamineMessageById(id);
    }
}
