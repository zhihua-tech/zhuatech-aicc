/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.aicc.controller;

import cn.zhuatech.aicc.common.ApiResponse;
import cn.zhuatech.aicc.service.ContactCenterInteractionClosureService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enterprise/aicc")
public class ContactCenterInteractionClosureController {
    private final ContactCenterInteractionClosureService service;
    public ContactCenterInteractionClosureController(ContactCenterInteractionClosureService service) { this.service = service; }
    @PostMapping("/interaction-closure")
    public ApiResponse<ContactCenterInteractionClosureService.Assessment> assess(
            @Valid @RequestBody ContactCenterInteractionClosureService.Request request) {
        return ApiResponse.ok(service.assess(request));
    }
}
