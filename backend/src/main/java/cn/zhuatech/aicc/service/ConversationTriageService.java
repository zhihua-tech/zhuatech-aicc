/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.aicc.service;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** 对客户消息做可解释的优先级分流，并明确何时必须转人工。 */
@Service
public class ConversationTriageService {
    public TriageResult triage(TriageRequest request) {
        String message = request.message().toLowerCase(Locale.ROOT);
        List<String> reasons = new ArrayList<>();
        boolean complaint = containsAny(message, "投诉", "退款", "赔偿", "欺诈", "泄露", "无法使用");
        boolean urgent = containsAny(message, "紧急", "立即", "马上", "停机", "中断");

        if (complaint) reasons.add("检测到投诉或重大服务风险关键词");
        if (urgent) reasons.add("客户表达了紧急处理诉求");
        if (request.sentimentScore() <= -0.6) reasons.add("负向情绪分值达到人工接管阈值");
        if (request.previousFailures() >= 2) reasons.add("连续自动处理失败");
        if (request.containsCommitment()) reasons.add("消息涉及业务承诺，需要坐席确认");

        boolean handoff = complaint || request.sentimentScore() <= -0.6 || request.previousFailures() >= 2 || request.containsCommitment();
        String priority = urgent || complaint ? "P1" : handoff ? "P2" : request.sentimentScore() < 0 ? "P3" : "P4";
        int slaMinutes = switch (priority) { case "P1" -> 5; case "P2" -> 15; case "P3" -> 60; default -> 240; };
        String queue = complaint ? "客户关怀专席" : handoff ? "人工服务队列" : "AI 自助服务队列";
        String guidance = handoff ? "保留上下文与知识引用后转人工，禁止自动作出承诺" : "可生成带引用的候选回复并继续观察情绪变化";
        return new TriageResult(priority, handoff, slaMinutes, queue, List.copyOf(reasons), guidance);
    }

    private boolean containsAny(String text, String... keywords) {
        for (String keyword : keywords) if (text.contains(keyword)) return true;
        return false;
    }

    public record TriageRequest(
        @NotBlank(message = "请输入客户消息") String message,
        @DecimalMin(value = "-1.0", message = "情绪分值不能小于 -1")
        @DecimalMax(value = "1.0", message = "情绪分值不能大于 1") double sentimentScore,
        @PositiveOrZero(message = "失败次数不能为负数") int previousFailures,
        boolean containsCommitment
    ) {}

    public record TriageResult(
        String priority,
        boolean humanHandoff,
        int slaMinutes,
        String recommendedQueue,
        List<String> reasons,
        String replyGuidance
    ) {}
}
