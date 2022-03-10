package com.examine.system.mapper;

import java.util.List;
import com.examine.system.domain.ExamineProcess;

/**
 * 访客申请Mapper接口
 *
 * @author Evan-WH
 * @date 2022-03-06
 */
public interface ExamineProcessMapper
{
    /**
     * 查询访客申请
     *
     * @param id 访客申请主键
     * @return 访客申请
     */
    public ExamineProcess selectExamineProcessById(Long id);

    /**
     * 查询访客申请列表
     *
     * @param examineProcess 访客申请
     * @return 访客申请集合
     */
    public List<ExamineProcess> selectExamineProcessList(ExamineProcess examineProcess);

    /**
     * 新增访客申请
     *
     * @param examineProcess 访客申请
     * @return 结果
     */
    public int insertExamineProcess(ExamineProcess examineProcess);

    /**
     * 修改访客申请
     *
     * @param examineProcess 访客申请
     * @return 结果
     */
    public int updateExamineProcess(ExamineProcess examineProcess);

    /**
     * 删除访客申请
     *
     * @param id 访客申请主键
     * @return 结果
     */
    public int deleteExamineProcessById(Long id);

    /**
     * 批量删除访客申请
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteExamineProcessByIds(Long[] ids);
}
