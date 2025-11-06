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
import com.server.domain.AgricultureGrowthNode;
import com.server.service.AgricultureGrowthNodeService;
import com.server.utils.poi.ExcelUtil;
import com.server.core.page.TableDataInfo;

/**
 * 生长关键节点Controller
 *
 * @author bxwu
 * @date 2025-11-05
 */
@RestController
@RequestMapping("/agriculture/growthnode")
public class AgricultureGrowthNodeController extends BaseController
{
    @Autowired
    private AgricultureGrowthNodeService agricultureGrowthNodeService;

    /**
     * 查询生长关键节点列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:growthnode:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgricultureGrowthNode agricultureGrowthNode)
    {
        startPage();
        List<AgricultureGrowthNode> list = agricultureGrowthNodeService.selectAgricultureGrowthNodeList(agricultureGrowthNode);
        return getDataTable(list);
    }

    /**
     * 导出生长关键节点列表
     */
    @PreAuthorize("@ss.hasPermi('agriculture:growthnode:export')")
    @Log(title = "生长关键节点", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AgricultureGrowthNode agricultureGrowthNode)
    {
        List<AgricultureGrowthNode> list = agricultureGrowthNodeService.selectAgricultureGrowthNodeList(agricultureGrowthNode);
        ExcelUtil<AgricultureGrowthNode> util = new ExcelUtil<AgricultureGrowthNode>(AgricultureGrowthNode.class);
        util.exportExcel(response, list, "生长关键节点数据");
    }

    /**
     * 获取生长关键节点详细信息
     */
    @PreAuthorize("@ss.hasPermi('agriculture:growthnode:query')")
    @GetMapping(value = "/{nodeId}")
    public AjaxResult getInfo(@PathVariable("nodeId") Long nodeId)
    {
        return success(agricultureGrowthNodeService.selectAgricultureGrowthNodeByNodeId(nodeId));
    }

    /**
     * 新增生长关键节点
     */
    @PreAuthorize("@ss.hasPermi('agriculture:growthnode:add')")
    @Log(title = "生长关键节点", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AgricultureGrowthNode agricultureGrowthNode)
    {
        return toAjax(agricultureGrowthNodeService.insertAgricultureGrowthNode(agricultureGrowthNode));
    }

    /**
     * 修改生长关键节点
     */
    @PreAuthorize("@ss.hasPermi('agriculture:growthnode:edit')")
    @Log(title = "生长关键节点", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AgricultureGrowthNode agricultureGrowthNode)
    {
        return toAjax(agricultureGrowthNodeService.updateAgricultureGrowthNode(agricultureGrowthNode));
    }

    /**
     * 删除生长关键节点
     */
    @PreAuthorize("@ss.hasPermi('agriculture:growthnode:remove')")
    @Log(title = "生长关键节点", businessType = BusinessType.DELETE)
	@DeleteMapping("/{nodeIds}")
    public AjaxResult remove(@PathVariable Long[] nodeIds)
    {
        return toAjax(agricultureGrowthNodeService.deleteAgricultureGrowthNodeByNodeIds(nodeIds));
    }
}

