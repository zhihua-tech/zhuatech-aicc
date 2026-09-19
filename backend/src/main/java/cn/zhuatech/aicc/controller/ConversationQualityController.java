/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.aicc.controller;
import cn.zhuatech.aicc.common.ApiResponse;import cn.zhuatech.aicc.service.ConversationQualityService;import jakarta.validation.Valid;import org.springframework.web.bind.annotation.*;
/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@RestController @RequestMapping("/api/aicc/insights/conversation-quality") public class ConversationQualityController {private final ConversationQualityService service;/**
                                                                                                                                                                         * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                                                                                                                         */
public ConversationQualityController(ConversationQualityService service){this.service=service;}/**
                                                                                                                                                                                                                                                                        * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                                                                                                                                                                                                                        */
@PostMapping ApiResponse<ConversationQualityService.Result> evaluate(@Valid @RequestBody ConversationQualityService.Request request){return ApiResponse.ok(service.evaluate(request));}}
