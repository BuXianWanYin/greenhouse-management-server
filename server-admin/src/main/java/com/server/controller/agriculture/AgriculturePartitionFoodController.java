package com.server.controller.agriculture;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.server.domain.dto.AgriculturePartitionFoodPageDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.server.annotation.Log;
import com.server.core.controller.BaseController;
import com.server.core.domain.AjaxResult;
import com.server.enums.BusinessType;
import com.server.domain.AgriculturePartitionFood;
import com.server.service.AgriculturePartitionFoodService;
import com.server.utils.poi.ExcelUtil;
import com.server.core.page.TableDataInfo;

/**
 * 采摘食品Controller
 * 
 * @author bxwy
 * @date 2025-06-24
 */
@RestController
@RequestMapping("/agriculture/partitionFood")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class AgriculturePartitionFoodController extends BaseController
{
    @Autowired
    private AgriculturePartitionFoodService agriculturePartitionFoodService;

    /**
     * 查询采摘食品列表
     */
    @PreAuthorize("@ss.hasPermi('agriculturePartitionFood:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgriculturePartitionFood agriculturePartitionFood)
    {
        startPage();
        return getDataTable(agriculturePartitionFoodService.selectagriculturePartitionFoodList(agriculturePartitionFood));
    }

    /**
     * 导出采摘食品列表
     */
    @PreAuthorize("@ss.hasPermi('agriculturePartitionFood:export')")
    @Log(title = "采摘食品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AgriculturePartitionFood agriculturePartitionFood)
    {
        List<AgriculturePartitionFood> list = agriculturePartitionFoodService.selectagriculturePartitionFoodList(agriculturePartitionFood);
        ExcelUtil<AgriculturePartitionFood> util = new ExcelUtil<AgriculturePartitionFood>(AgriculturePartitionFood.class);
        util.exportExcel(response, list, "采摘食品数据");
    }

    /**
     * 获取采摘食品详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculturePartitionFood:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return success(agriculturePartitionFoodService.selectagriculturePartitionFoodById(id));
    }

    /**
     * 新增采摘食品
     */
    @PreAuthorize("@ss.hasPermi('agriculturePartitionFood:add')")
    @Log(title = "采摘食品", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AgriculturePartitionFood agriculturePartitionFood)
    {
        return toAjax(agriculturePartitionFoodService.insertagriculturePartitionFood(agriculturePartitionFood));
    }

    /**
     * 修改采摘食品
     */
    @PreAuthorize("@ss.hasPermi('agriculturePartitionFood:edit')")
    @Log(title = "采摘食品", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AgriculturePartitionFood agriculturePartitionFood)
    {
        return toAjax(agriculturePartitionFoodService.updateagriculturePartitionFood(agriculturePartitionFood));
    }

    /**
     * 删除采摘食品
     */
    @PreAuthorize("@ss.hasPermi('agriculturePartitionFood:remove')")
    @Log(title = "采摘食品", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(agriculturePartitionFoodService.deleteagriculturePartitionFoodByIds(ids));
    }


}
