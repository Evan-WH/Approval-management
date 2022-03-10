package com.examine.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.examine.system.mapper.ExamineProcessMapper;
import com.examine.system.domain.ExamineProcess;
import com.examine.system.service.IExamineProcessService;

/**
 * 访客申请Service业务层处理
 *
 * @author Evan-WH
 * @date 2022-03-06
 */
@Service
public class ExamineProcessServiceImpl implements IExamineProcessService
{
    @Autowired
    private ExamineProcessMapper examineProcessMapper;

    /**
     * 查询访客申请
     *
     * @param id 访客申请主键
     * @return 访客申请
     */
    @Override
    public ExamineProcess selectExamineProcessById(Long id)
    {
        return examineProcessMapper.selectExamineProcessById(id);
    }

    /**
     * 查询访客申请列表
     *
     * @param examineProcess 访客申请
     * @return 访客申请
     */
    @Override
    public List<ExamineProcess> selectExamineProcessList(ExamineProcess examineProcess)
    {
        return examineProcessMapper.selectExamineProcessList(examineProcess);
    }

    /**
     * 新增访客申请
     *
     * @param examineProcess 访客申请
     * @return 结果
     */
    @Override
    public int insertExamineProcess(ExamineProcess examineProcess)
    {
        return examineProcessMapper.insertExamineProcess(examineProcess);
    }

    /**
     * 修改访客申请
     *
     * @param examineProcess 访客申请
     * @return 结果
     */
    @Override
    public int updateExamineProcess(ExamineProcess examineProcess)
    {
        return examineProcessMapper.updateExamineProcess(examineProcess);
    }

    /**
     * 批量删除访客申请
     *
     * @param ids 需要删除的访客申请主键
     * @return 结果
     */
    @Override
    public int deleteExamineProcessByIds(Long[] ids)
    {
        return examineProcessMapper.deleteExamineProcessByIds(ids);
    }

    /**
     * 删除访客申请信息
     *
     * @param id 访客申请主键
     * @return 结果
     */
    @Override
    public int deleteExamineProcessById(Long id)
    {
        return examineProcessMapper.deleteExamineProcessById(id);
    }
}
