/* Copyright 2026 上海如静知华信息科技有限公司 */
package cn.zhuatech.aicc.controller;
import cn.zhuatech.aicc.common.ApiResponse;import cn.zhuatech.aicc.service.ConversationQualityService;import jakarta.validation.Valid;import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/aicc/insights/conversation-quality") public class ConversationQualityController {private final ConversationQualityService service;public ConversationQualityController(ConversationQualityService service){this.service=service;}@PostMapping ApiResponse<ConversationQualityService.Result> evaluate(@Valid @RequestBody ConversationQualityService.Request request){return ApiResponse.ok(service.evaluate(request));}}
