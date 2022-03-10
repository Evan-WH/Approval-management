package com.examine.system.service;

import java.util.List;
import com.examine.system.domain.ExamineMessage;

/**
 * 审批通知Service接口
 *
 * @author Evan-WH
 * @date 2022-03-09
 */
public interface IExamineMessageService
{
    /**
     * 查询审批通知
     *
     * @param id 审批通知主键
     * @return 审批通知
     */
    public ExamineMessage selectExamineMessageById(Long id);

    /**
     * 查询审批通知列表
     *
     * @param examineMessage 审批通知
     * @return 审批通知集合
     */
    public List<ExamineMessage> selectExamineMessageList(ExamineMessage examineMessage);

    /**
     * 新增审批通知
     *
     * @param examineMessage 审批通知
     * @return 结果
     */
    public int insertExamineMessage(ExamineMessage examineMessage);

    /**
     * 修改审批通知
     *
     * @param examineMessage 审批通知
     * @return 结果
     */
    public int updateExamineMessage(ExamineMessage examineMessage);

    /**
     * 批量删除审批通知
     *
     * @param ids 需要删除的审批通知主键集合
     * @return 结果
     */
    public int deleteExamineMessageByIds(Long[] ids);

    /**
     * 删除审批通知信息
     *
     * @param id 审批通知主键
     * @return 结果
     */
    public int deleteExamineMessageById(Long id);
}
