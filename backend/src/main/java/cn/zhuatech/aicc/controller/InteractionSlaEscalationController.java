/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.aicc.controller;

import cn.zhuatech.aicc.common.ApiResponse;
import cn.zhuatech.aicc.service.InteractionSlaEscalationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/enterprise/aicc")
public class InteractionSlaEscalationController {
    private final InteractionSlaEscalationService service;

    public InteractionSlaEscalationController(InteractionSlaEscalationService service) {
        this.service = service;
    }

    @PostMapping("/sla-escalation")
    public ApiResponse<InteractionSlaEscalationService.EscalationResult> evaluate(
            @Valid @RequestBody InteractionSlaEscalationService.EscalationRequest request) {
        return ApiResponse.ok("客服 SLA 升级决策完成", service.evaluate(request));
    }
}
