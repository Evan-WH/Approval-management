package com.examine.web.controller.system;

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
import com.examine.system.domain.ExamineProcess;
import com.examine.system.service.IExamineProcessService;
import com.examine.common.utils.poi.ExcelUtil;
import com.examine.common.core.page.TableDataInfo;

/**
 * 访客申请Controller
 *
 * @author Evan-WH
 * @date 2022-03-06
 */
@RestController
@RequestMapping("/system/process")
public class ExamineProcessController extends BaseController
{
    @Autowired
    private IExamineProcessService examineProcessService;

    /**
     * 查询访客申请列表
     */
    @PreAuthorize("@ss.hasPermi('system:process:list')")
    @GetMapping("/list")
    public TableDataInfo list(ExamineProcess examineProcess)
    {
        startPage();
        List<ExamineProcess> list = examineProcessService.selectExamineProcessList(examineProcess);
        return getDataTable(list);
    }

    /**
     * 导出访客申请列表
     */
    @PreAuthorize("@ss.hasPermi('system:process:export')")
    @Log(title = "访客申请", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExamineProcess examineProcess)
    {
        List<ExamineProcess> list = examineProcessService.selectExamineProcessList(examineProcess);
        ExcelUtil<ExamineProcess> util = new ExcelUtil<ExamineProcess>(ExamineProcess.class);
        util.exportExcel(response, list, "访客申请数据");
    }

    /**
     * 获取访客申请详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:process:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(examineProcessService.selectExamineProcessById(id));
    }

    /**
     * 新增访客申请
     */
    @PreAuthorize("@ss.hasPermi('system:process:add')")
    @Log(title = "访客申请", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ExamineProcess examineProcess)
    {
        return toAjax(examineProcessService.insertExamineProcess(examineProcess));
    }

    /**
     * 修改访客申请
     */
    @PreAuthorize("@ss.hasPermi('system:process:edit')")
    @Log(title = "访客申请", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ExamineProcess examineProcess)
    {
        return toAjax(examineProcessService.updateExamineProcess(examineProcess));
    }

    /**
     * 删除访客申请
     */
    @PreAuthorize("@ss.hasPermi('system:process:remove')")
    @Log(title = "访客申请", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(examineProcessService.deleteExamineProcessByIds(ids));
    }
}
