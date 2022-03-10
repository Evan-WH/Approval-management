package com.examine.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.examine.common.annotation.Log;
import com.examine.common.core.controller.BaseController;
import com.examine.common.core.domain.AjaxResult;
import com.examine.common.enums.BusinessType;
import com.examine.system.domain.ExamineMessage;
import com.examine.system.service.IExamineMessageService;
import com.examine.common.utils.poi.ExcelUtil;
import com.examine.common.core.page.TableDataInfo;

/**
 * 审批通知Controller
 *
 * @author Evan-WH
 * @date 2022-03-09
 */
@RestController
@RequestMapping("/system/message")
public class ExamineMessageController extends BaseController
{
    @Autowired
    private IExamineMessageService examineMessageService;

    /**
     * 查询审批通知列表
     */
    @PreAuthorize("@ss.hasPermi('system:message:list')")
    @GetMapping("/list")
    public TableDataInfo list(ExamineMessage examineMessage)
    {
        startPage();
        List<ExamineMessage> list = examineMessageService.selectExamineMessageList(examineMessage);
        return getDataTable(list);
    }

    /**
     * 导出审批通知列表
     */
    @PreAuthorize("@ss.hasPermi('system:message:export')")
    @Log(title = "审批通知", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExamineMessage examineMessage)
    {
        List<ExamineMessage> list = examineMessageService.selectExamineMessageList(examineMessage);
        ExcelUtil<ExamineMessage> util = new ExcelUtil<ExamineMessage>(ExamineMessage.class);
        util.exportExcel(response, list, "审批通知数据");
    }

    /**
     * 获取审批通知详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:message:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(examineMessageService.selectExamineMessageById(id));
    }

    /**
     * 新增审批通知
     */
    @PreAuthorize("@ss.hasPermi('system:message:add')")
    @Log(title = "审批通知", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ExamineMessage examineMessage)
    {
        return toAjax(examineMessageService.insertExamineMessage(examineMessage));
    }

    /**
     * 修改审批通知
     */
    @PreAuthorize("@ss.hasPermi('system:message:edit')")
    @Log(title = "审批通知", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ExamineMessage examineMessage)
    {
        return toAjax(examineMessageService.updateExamineMessage(examineMessage));
    }

    /**
     * 删除审批通知
     */
    @PreAuthorize("@ss.hasPermi('system:message:remove')")
    @Log(title = "审批通知", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(examineMessageService.deleteExamineMessageByIds(ids));
    }
}
