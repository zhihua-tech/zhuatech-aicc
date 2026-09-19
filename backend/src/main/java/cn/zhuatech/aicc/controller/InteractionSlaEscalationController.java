/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.aicc.controller;

import cn.zhuatech.aicc.common.ApiResponse;
import cn.zhuatech.aicc.service.InteractionSlaEscalationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@RestController
@RequestMapping("/api/enterprise/aicc")
public class InteractionSlaEscalationController {
    private final InteractionSlaEscalationService service;

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public InteractionSlaEscalationController(InteractionSlaEscalationService service) {
        this.service = service;
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping("/sla-escalation")
    public ApiResponse<InteractionSlaEscalationService.EscalationResult> evaluate(
            @Valid @RequestBody InteractionSlaEscalationService.EscalationRequest request) {
        return ApiResponse.ok("客服 SLA 升级决策完成", service.evaluate(request));
    }
}
