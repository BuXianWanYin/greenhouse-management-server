package com.server.controller.agriculture;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.server.annotation.Log;
import com.server.annotation.SeeRefreshData;
import com.server.core.page.TableDataInfo;
import com.server.domain.dto.AgricultureCropBatchDTO;
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
import com.server.core.controller.BaseController;
import com.server.core.domain.AjaxResult;
import com.server.enums.BusinessType;
import com.server.domain.AgricultureCropBatch;
import com.server.service.AgricultureCropBatchService;
import com.server.utils.poi.ExcelUtil;

/**
 * 种植批次Controller
 *
 * @author bxwy
 * @date 2025-09-28
 */
@RestController
@RequestMapping("/agriculture/batch")
public class AgricultureCropBatchController extends BaseController
{
    @Autowired
    private AgricultureCropBatchService agricultureCropBatchService;

    /**
     * 查询批次列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:batch:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgricultureCropBatchDTO agricultureCropBatchDTO){
        startPage();
        return getDataTable(agricultureCropBatchService.getCropBatchWithClassImages(agricultureCropBatchDTO));
    }

    /**
     * 根据温室ID查询批次列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:batch:list')")
    @GetMapping("/listByPasture/{pastureId}")
    public AjaxResult listByPasture(@PathVariable("pastureId") Long pastureId) {
        return success(agricultureCropBatchService.selectBatchByPastureId(pastureId));
    }

    /**
     * 导出批次列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:batch:export')")
    @Log(title = "种植批次", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AgricultureCropBatch agricultureCropBatch)
    {
        List<AgricultureCropBatch> list = agricultureCropBatchService.selectAgricultureCropBatchList(agricultureCropBatch);
        ExcelUtil<AgricultureCropBatch> util = new ExcelUtil<AgricultureCropBatch>(AgricultureCropBatch.class);
        util.exportExcel(response, list, "种植批次数据");
    }

    /**
     * 获取批次详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:batch:query')")
    @GetMapping(value = "/{batchId}")
    public AjaxResult getInfo(@PathVariable("batchId") Long batchId)
    {
        return success(agricultureCropBatchService.selectAgricultureCropBatchByBatchId(batchId));
    }

    /**
     * 新增种植批次
     */
    @PreAuthorize("@ss.hasPermi('agriculture:batch:add')")
    @Log(title = "种植批次", businessType = BusinessType.INSERT)
    @PostMapping
    @SeeRefreshData
    public AjaxResult add(@RequestBody AgricultureCropBatch agricultureCropBatch)
    {
        return toAjax(agricultureCropBatchService.insertAgricultureCropBatch(agricultureCropBatch));
    }

    /**
     * 修改种植批次
     */
    @PreAuthorize("@ss.hasPermi('agriculture:batch:edit')")
    @Log(title = "种植批次", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AgricultureCropBatch agricultureCropBatch)
    {
        return toAjax(agricultureCropBatchService.updateAgricultureCropBatch(agricultureCropBatch));
    }

    /**
     * 删除种植批次
     */
    @PreAuthorize("@ss.hasPermi('agriculture:batch:remove')")
    @Log(title = "种植批次", businessType = BusinessType.DELETE)
	@DeleteMapping("/{batchId}")
    @SeeRefreshData
    public AjaxResult remove(@PathVariable Long[] batchId)
    {
        return toAjax(agricultureCropBatchService.deleteAgricultureCropBatchByBatchIds(batchId));
    }
}
