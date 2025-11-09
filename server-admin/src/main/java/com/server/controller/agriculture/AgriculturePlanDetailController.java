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
import com.server.domain.AgriculturePlanDetail;
import com.server.service.AgriculturePlanDetailService;
import com.server.utils.poi.ExcelUtil;
import com.server.core.page.TableDataInfo;

/**
 * 种植计划明细Controller
 *
 * @author bxwu
 * @date 2025-11-05
 */
@RestController
@RequestMapping("/agriculture/plandetail")
public class AgriculturePlanDetailController extends BaseController
{
    @Autowired
    private AgriculturePlanDetailService agriculturePlanDetailService;

    /**
     * 查询种植计划明细列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:plandetail:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgriculturePlanDetail agriculturePlanDetail)
    {
        startPage();
        List<AgriculturePlanDetail> list = agriculturePlanDetailService.selectAgriculturePlanDetailList(agriculturePlanDetail);
        return getDataTable(list);
    }

    /**
     * 导出种植计划明细列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:plandetail:export')")
    @Log(title = "种植计划明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AgriculturePlanDetail agriculturePlanDetail)
    {
        List<AgriculturePlanDetail> list = agriculturePlanDetailService.selectAgriculturePlanDetailList(agriculturePlanDetail);
        ExcelUtil<AgriculturePlanDetail> util = new ExcelUtil<AgriculturePlanDetail>(AgriculturePlanDetail.class);
        util.exportExcel(response, list, "种植计划明细数据");
    }

    /**
     * 获取种植计划明细详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:plandetail:query')")
    @GetMapping(value = "/{detailId}")
    public AjaxResult getInfo(@PathVariable("detailId") Long detailId)
    {
        return success(agriculturePlanDetailService.selectAgriculturePlanDetailByDetailId(detailId));
    }

    /**
     * 新增种植计划明细
     */
    @PreAuthorize("@ss.hasPermi('agriculture:plandetail:add')")
    @Log(title = "种植计划明细", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AgriculturePlanDetail agriculturePlanDetail)
    {
        return toAjax(agriculturePlanDetailService.insertAgriculturePlanDetail(agriculturePlanDetail));
    }

    /**
     * 修改种植计划明细
     */
    @PreAuthorize("@ss.hasPermi('agriculture:plandetail:edit')")
    @Log(title = "种植计划明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AgriculturePlanDetail agriculturePlanDetail)
    {
        return toAjax(agriculturePlanDetailService.updateAgriculturePlanDetail(agriculturePlanDetail));
    }

    /**
     * 删除种植计划明细
     */
    @PreAuthorize("@ss.hasPermi('agriculture:plandetail:remove')")
    @Log(title = "种植计划明细", businessType = BusinessType.DELETE)
	@DeleteMapping("/{detailIds}")
    public AjaxResult remove(@PathVariable Long[] detailIds)
    {
        return toAjax(agriculturePlanDetailService.deleteAgriculturePlanDetailByDetailIds(detailIds));
    }
}

