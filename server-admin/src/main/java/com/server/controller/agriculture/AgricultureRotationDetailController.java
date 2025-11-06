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
import com.server.domain.AgricultureRotationDetail;
import com.server.service.AgricultureRotationDetailService;
import com.server.utils.poi.ExcelUtil;
import com.server.core.page.TableDataInfo;

/**
 * 轮作计划明细Controller
 *
 * @author bxwu
 * @date 2025-11-05
 */
@RestController
@RequestMapping("/agriculture/rotationdetail")
public class AgricultureRotationDetailController extends BaseController
{
    @Autowired
    private AgricultureRotationDetailService agricultureRotationDetailService;

    /**
     * 查询轮作计划明细列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:rotationdetail:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgricultureRotationDetail agricultureRotationDetail)
    {
        startPage();
        List<AgricultureRotationDetail> list = agricultureRotationDetailService.selectAgricultureRotationDetailList(agricultureRotationDetail);
        return getDataTable(list);
    }

    /**
     * 导出轮作计划明细列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:rotationdetail:export')")
    @Log(title = "轮作计划明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AgricultureRotationDetail agricultureRotationDetail)
    {
        List<AgricultureRotationDetail> list = agricultureRotationDetailService.selectAgricultureRotationDetailList(agricultureRotationDetail);
        ExcelUtil<AgricultureRotationDetail> util = new ExcelUtil<AgricultureRotationDetail>(AgricultureRotationDetail.class);
        util.exportExcel(response, list, "轮作计划明细数据");
    }

    /**
     * 获取轮作计划明细详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:rotationdetail:query')")
    @GetMapping(value = "/{detailId}")
    public AjaxResult getInfo(@PathVariable("detailId") Long detailId)
    {
        return success(agricultureRotationDetailService.selectAgricultureRotationDetailByDetailId(detailId));
    }

    /**
     * 新增轮作计划明细
     */
    @PreAuthorize("@ss.hasPermi('agriculture:rotationdetail:add')")
    @Log(title = "轮作计划明细", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AgricultureRotationDetail agricultureRotationDetail)
    {
        return toAjax(agricultureRotationDetailService.insertAgricultureRotationDetail(agricultureRotationDetail));
    }

    /**
     * 修改轮作计划明细
     */
    @PreAuthorize("@ss.hasPermi('agriculture:rotationdetail:edit')")
    @Log(title = "轮作计划明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AgricultureRotationDetail agricultureRotationDetail)
    {
        return toAjax(agricultureRotationDetailService.updateAgricultureRotationDetail(agricultureRotationDetail));
    }

    /**
     * 删除轮作计划明细
     */
    @PreAuthorize("@ss.hasPermi('agriculture:rotationdetail:remove')")
    @Log(title = "轮作计划明细", businessType = BusinessType.DELETE)
	@DeleteMapping("/{detailIds}")
    public AjaxResult remove(@PathVariable Long[] detailIds)
    {
        return toAjax(agricultureRotationDetailService.deleteAgricultureRotationDetailByDetailIds(detailIds));
    }
}

