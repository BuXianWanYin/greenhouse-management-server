package com.server.controller.agriculture;

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
import com.server.annotation.Log;
import com.server.core.controller.BaseController;
import com.server.core.domain.AjaxResult;
import com.server.enums.BusinessType;
import com.server.domain.AgriculturePlanBatch;
import com.server.service.AgriculturePlanBatchService;
import com.server.utils.poi.ExcelUtil;
import com.server.core.page.TableDataInfo;

/**
 * 年度计划批次关联Controller
 *
 * @author bxwu
 * @date 2025-11-05
 */
@RestController
@RequestMapping("/agriculture/planbatch")
public class    AgriculturePlanBatchController extends BaseController
{
    @Autowired
    private AgriculturePlanBatchService agriculturePlanBatchService;

    /**
     * 查询年度计划批次关联列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:planbatch:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgriculturePlanBatch agriculturePlanBatch)
    {
        startPage();
        List<AgriculturePlanBatch> list = agriculturePlanBatchService.selectAgriculturePlanBatchList(agriculturePlanBatch);
        return getDataTable(list);
    }

    /**
     * 导出年度计划批次关联列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:planbatch:export')")
    @Log(title = "年度计划批次关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AgriculturePlanBatch agriculturePlanBatch)
    {
        List<AgriculturePlanBatch> list = agriculturePlanBatchService.selectAgriculturePlanBatchList(agriculturePlanBatch);
        ExcelUtil<AgriculturePlanBatch> util = new ExcelUtil<AgriculturePlanBatch>(AgriculturePlanBatch.class);
        util.exportExcel(response, list, "年度计划批次关联数据");
    }

    /**
     * 获取年度计划批次关联详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:planbatch:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(agriculturePlanBatchService.selectAgriculturePlanBatchById(id));
    }

    /**
     * 新增年度计划批次关联
     */
    @PreAuthorize("@ss.hasPermi('agriculture:planbatch:add')")
    @Log(title = "年度计划批次关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AgriculturePlanBatch agriculturePlanBatch)
    {
        return toAjax(agriculturePlanBatchService.insertAgriculturePlanBatch(agriculturePlanBatch));
    }

    /**
     * 修改年度计划批次关联
     */
    @PreAuthorize("@ss.hasPermi('agriculture:planbatch:edit')")
    @Log(title = "年度计划批次关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AgriculturePlanBatch agriculturePlanBatch)
    {
        return toAjax(agriculturePlanBatchService.updateAgriculturePlanBatch(agriculturePlanBatch));
    }

    /**
     * 删除年度计划批次关联
     */
    @PreAuthorize("@ss.hasPermi('agriculture:planbatch:remove')")
    @Log(title = "年度计划批次关联", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(agriculturePlanBatchService.deleteAgriculturePlanBatchByIds(ids));
    }
}

