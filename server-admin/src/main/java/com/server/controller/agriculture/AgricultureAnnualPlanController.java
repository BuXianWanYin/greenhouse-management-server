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
import com.server.domain.AgricultureAnnualPlan;
import com.server.service.AgricultureAnnualPlanService;
import com.server.utils.poi.ExcelUtil;
import com.server.core.page.TableDataInfo;
import com.server.domain.dto.AgricultureCropBatchDTO;

/**
 * 年度种植规划Controller
 *
 * @author bxwu
 * @date 2025-11-05
 */
@RestController
@RequestMapping("/agriculture/annualplan")
public class AgricultureAnnualPlanController extends BaseController
{
    @Autowired
    private AgricultureAnnualPlanService agricultureAnnualPlanService;

    /**
     * 查询年度种植规划列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:annualplan:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgricultureAnnualPlan agricultureAnnualPlan)
    {
        startPage();
        List<AgricultureAnnualPlan> list = agricultureAnnualPlanService.selectAgricultureAnnualPlanList(agricultureAnnualPlan);
        return getDataTable(list);
    }

    /**
     * 导出年度种植规划列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:annualplan:export')")
    @Log(title = "年度种植规划", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AgricultureAnnualPlan agricultureAnnualPlan)
    {
        List<AgricultureAnnualPlan> list = agricultureAnnualPlanService.selectAgricultureAnnualPlanList(agricultureAnnualPlan);
        ExcelUtil<AgricultureAnnualPlan> util = new ExcelUtil<AgricultureAnnualPlan>(AgricultureAnnualPlan.class);
        util.exportExcel(response, list, "年度种植规划数据");
    }

    /**
     * 获取年度种植规划详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:annualplan:query')")
    @GetMapping(value = "/{planId}")
    public AjaxResult getInfo(@PathVariable("planId") Long planId)
    {
        return success(agricultureAnnualPlanService.selectAgricultureAnnualPlanByPlanId(planId));
    }

    /**
     * 新增年度种植规划
     */
    @PreAuthorize("@ss.hasPermi('agriculture:annualplan:add')")
    @Log(title = "年度种植规划", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AgricultureAnnualPlan agricultureAnnualPlan)
    {
        return toAjax(agricultureAnnualPlanService.insertAgricultureAnnualPlan(agricultureAnnualPlan));
    }

    /**
     * 修改年度种植规划
     */
    @PreAuthorize("@ss.hasPermi('agriculture:annualplan:edit')")
    @Log(title = "年度种植规划", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AgricultureAnnualPlan agricultureAnnualPlan)
    {
        return toAjax(agricultureAnnualPlanService.updateAgricultureAnnualPlan(agricultureAnnualPlan));
    }

    /**
     * 删除年度种植规划
     */
    @PreAuthorize("@ss.hasPermi('agriculture:annualplan:remove')")
    @Log(title = "年度种植规划", businessType = BusinessType.DELETE)
	@DeleteMapping("/{planIds}")
    public AjaxResult remove(@PathVariable Long[] planIds)
    {
        return toAjax(agricultureAnnualPlanService.deleteAgricultureAnnualPlanByPlanIds(planIds));
    }

    /**
     * 获取计划关联的批次列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:annualplan:query')")
    @GetMapping("/{planId}/batches")
    public AjaxResult getPlanBatches(@PathVariable("planId") Long planId)
    {
        List<AgricultureCropBatchDTO> batchList = agricultureAnnualPlanService.getPlanBatches(planId);
        return success(batchList);
    }

    /**
     * 将批次添加到计划
     */
    @PreAuthorize("@ss.hasPermi('agriculture:annualplan:edit')")
    @Log(title = "年度种植规划", businessType = BusinessType.UPDATE)
    @PostMapping("/{planId}/batches")
    public AjaxResult addBatchToPlan(@PathVariable("planId") Long planId, @RequestBody Long[] batchIds)
    {
        return toAjax(agricultureAnnualPlanService.addBatchToPlan(planId, batchIds));
    }

    /**
     * 从计划中移除批次
     */
    @PreAuthorize("@ss.hasPermi('agriculture:annualplan:edit')")
    @Log(title = "年度种植规划", businessType = BusinessType.UPDATE)
    @DeleteMapping("/{planId}/batches/{batchId}")
    public AjaxResult removeBatchFromPlan(@PathVariable("planId") Long planId, @PathVariable("batchId") Long batchId)
    {
        return toAjax(agricultureAnnualPlanService.removeBatchFromPlan(planId, batchId));
    }
}

