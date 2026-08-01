/* Copyright 2026 上海如静知华信息科技有限公司 */
package cn.zhuatech.aicc.controller;

import cn.zhuatech.aicc.ai.AiProvider;
import cn.zhuatech.aicc.common.ApiResponse;
import cn.zhuatech.aicc.dto.AiccDto.*;
import cn.zhuatech.aicc.service.AiccService;
import cn.zhuatech.aicc.service.ConversationTriageService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/shopfloor")
@PreAuthorize("hasAnyRole('DOMAIN_USER','ADMIN')")
public class WorkspaceController {
    private final AiccService service;
    private final AiProvider ai;
    private final ConversationTriageService triageService;

    public WorkspaceController(AiccService service, AiProvider ai, ConversationTriageService triageService) {
        this.service = service;
        this.ai = ai;
        this.triageService = triageService;
    }

    @GetMapping("/dashboard")
    public ApiResponse<Dashboard> dashboard() { return ApiResponse.ok(service.shopfloorDashboard()); }

    @PostMapping("/work-orders/{id}/reports")
    public ApiResponse<ReportResult> report(@PathVariable Long id, @Valid @RequestBody ReportRequest request) {
        return ApiResponse.ok("反馈提交成功", service.report(id, request));
    }

    @PostMapping("/ai-preview")
    public ApiResponse<AiProvider.AiResult> preview(@RequestBody Map<String, String> body) {
        return ApiResponse.ok(ai.execute(body.getOrDefault("prompt", ""), Map.of("mode", "demo")));
    }

    @PostMapping("/conversation-triage")
    public ApiResponse<ConversationTriageService.TriageResult> triage(@Valid @RequestBody ConversationTriageService.TriageRequest request) {
        return ApiResponse.ok("会话分级完成", triageService.triage(request));
    }
}
