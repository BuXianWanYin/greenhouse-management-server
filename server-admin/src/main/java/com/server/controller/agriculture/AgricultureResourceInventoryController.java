package com.server.controller.agriculture;

import com.server.annotation.Log;
import com.server.annotation.SeeRefreshData;
import com.server.core.controller.BaseController;
import com.server.core.domain.AjaxResult;
import com.server.core.page.TableDataInfo;
import com.server.domain.AgricultureResource;
import com.server.domain.AgricultureResourceInventory;
import com.server.domain.dto.StockInRequest;
import com.server.domain.dto.StockOutRequest;
import com.server.enums.BusinessType;
import com.server.enums.SeeMessageType;
import com.server.domain.AgricultureResourceUsage;
import com.server.service.AgricultureResourceInventoryService;
import com.server.service.AgricultureResourceService;
import com.server.service.AgricultureResourceUsageService;
import com.server.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 农资库存Controller
 * 
 * @author server
 * @date 2025-01-XX
 */
@RestController
@RequestMapping("/agriculture/resource/inventory")
@Api(tags = "农资库存管理")
public class AgricultureResourceInventoryController extends BaseController {

    @Autowired
    private AgricultureResourceInventoryService agricultureResourceInventoryService;

    @Autowired
    private AgricultureResourceUsageService agricultureResourceUsageService;

    @Autowired
    private AgricultureResourceService agricultureResourceService;

    /**
     * 查询农资库存列表
     */
    @GetMapping("/list")
    @ApiOperation("查询农资库存列表")
    public TableDataInfo list(AgricultureResourceInventory agricultureResourceInventory) {
        startPage();
        return getDataTable(agricultureResourceInventoryService.selectAgricultureResourceInventoryList(agricultureResourceInventory));
    }

    /**
     * 获取农资库存详细信息
     */
    @GetMapping("/{inventoryId}")
    @ApiOperation("获取农资库存详细信息")
    public AjaxResult getInfo(@PathVariable("inventoryId") Long inventoryId) {
        return success(agricultureResourceInventoryService.getById(inventoryId));
    }

    /**
     * 根据农资ID获取库存信息
     */
    @GetMapping("/resource/{resourceId}")
    @ApiOperation("根据农资ID获取库存信息")
    public AjaxResult getByResourceId(@PathVariable("resourceId") Long resourceId) {
        return success(agricultureResourceInventoryService.selectByResourceId(resourceId));
    }

    /**
     * 新增农资库存
     */
    @Log(title = "农资库存管理", businessType = BusinessType.INSERT)
    @PostMapping
    @SeeRefreshData(seeMessageType = SeeMessageType.DATA)
    @ApiOperation("新增农资库存")
    public AjaxResult add(@RequestBody AgricultureResourceInventory agricultureResourceInventory) {
        return toAjax(agricultureResourceInventoryService.addAgricultureResourceInventory(agricultureResourceInventory));
    }

    /**
     * 修改农资库存
     */
    @Log(title = "农资库存管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @SeeRefreshData(seeMessageType = SeeMessageType.DATA)
    @ApiOperation("修改农资库存")
    public AjaxResult edit(@RequestBody AgricultureResourceInventory agricultureResourceInventory) {
        return toAjax(agricultureResourceInventoryService.updateAgricultureResourceInventory(agricultureResourceInventory));
    }

    /**
     * 删除农资库存
     */
    @Log(title = "农资库存管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{inventoryIds}")
    @SeeRefreshData(seeMessageType = SeeMessageType.DATA)
    @ApiOperation("删除农资库存")
    public AjaxResult remove(@PathVariable Long[] inventoryIds) {
        return toAjax(agricultureResourceInventoryService.removeByIds(java.util.Arrays.asList(inventoryIds)));
    }

    /**
     * 入库操作（增加库存并记录使用记录）
     */
    @Log(title = "农资库存管理", businessType = BusinessType.UPDATE)
    @PostMapping("/stockIn")
    @SeeRefreshData(seeMessageType = SeeMessageType.DATA)
    @ApiOperation("入库操作")
    public AjaxResult stockIn(@RequestBody StockInRequest request) {
        // 增加库存
        int result = agricultureResourceInventoryService.addInventory(request.getResourceId(), request.getQuantity());
        // 记录入库使用记录
        if (result > 0) {
            // 获取农资信息以获取计量单位
            AgricultureResource resource = agricultureResourceService.getById(request.getResourceId());
            AgricultureResourceUsage usage = new AgricultureResourceUsage();
            usage.setResourceId(request.getResourceId());
            usage.setUsageQuantity(request.getQuantity());
            usage.setUsageDate(LocalDateTime.now());
            usage.setUsageType("2"); // 2表示入库
            usage.setMeasureUnit(resource != null ? resource.getMeasureUnit() : "");
            usage.setOperator(request.getOperator());
            usage.setRemark(request.getRemark() != null ? request.getRemark() : "入库操作");
            usage.setStatus("0");
            agricultureResourceUsageService.addAgricultureResourceUsage(usage);
        }
        return toAjax(result);
    }

    /**
     * 出库操作（扣减库存）
     */
    @Log(title = "农资库存管理", businessType = BusinessType.UPDATE)
    @PostMapping("/stockOut")
    @SeeRefreshData(seeMessageType = SeeMessageType.DATA)
    @ApiOperation("出库操作")
    public AjaxResult stockOut(@RequestBody StockOutRequest request) {
        return toAjax(agricultureResourceInventoryService.deductInventory(request.getResourceId(), request.getQuantity()));
    }

    /**
     * 导出农资库存列表
     */
    @Log(title = "农资库存管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ApiOperation("导出农资库存列表")
    public void export(HttpServletResponse response, AgricultureResourceInventory agricultureResourceInventory) {
        List<AgricultureResourceInventory> list = agricultureResourceInventoryService.selectAgricultureResourceInventoryList(agricultureResourceInventory);
        ExcelUtil<AgricultureResourceInventory> util = new ExcelUtil<AgricultureResourceInventory>(AgricultureResourceInventory.class);
        util.exportExcel(response, list, "农资库存数据");
    }
}

