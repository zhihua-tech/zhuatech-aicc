/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.aicc.service;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 对全渠道客服交互执行 SLA 计时、风险升级和承接队列决策。
 *
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Service
public class InteractionSlaEscalationService {
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public EscalationResult evaluate(EscalationRequest request) {
        double utilization = round(request.elapsedMinutes() * 100d / request.slaMinutes());
        int remainingMinutes = Math.max(0, request.slaMinutes() - request.elapsedMinutes());
        List<String> reasons = new ArrayList<>();
        List<String> actions = new ArrayList<>();

        if (request.securityRisk()) {
            reasons.add("会话命中安全风险");
            actions.add("立即暂停自动回复并转安全响应负责人");
            actions.add("保全会话、身份验证和操作审计证据");
            return result(Decision.EMERGENCY, "security-response", utilization, remainingMinutes, reasons, actions);
        }
        if (request.regulatoryCase()) {
            reasons.add("会话涉及监管或法律时限");
            actions.add("升级合规专席并创建正式案件");
            return result(Decision.EMERGENCY, "compliance-desk", utilization, remainingMinutes, reasons, actions);
        }

        if (request.elapsedMinutes() >= request.slaMinutes()) reasons.add("服务时限已经超期");
        if (request.complaint()) reasons.add("客户明确投诉");
        if (request.negativeSentimentScore() >= 0.85) reasons.add("客户负向情绪达到高风险阈值");
        if (request.priority() == Priority.P1) reasons.add("交互优先级为 P1");
        if (!request.assignedAgentAvailable()) reasons.add("当前责任坐席不可用");

        if (request.elapsedMinutes() >= request.slaMinutes() || request.complaint()
                || request.negativeSentimentScore() >= 0.85 || request.priority() == Priority.P1) {
            actions.add("转交班长队列并锁定首个可用技能坐席");
            if (request.callbackConsent()) actions.add("按照客户同意的联系方式安排回呼");
            if (request.afterHours()) actions.add("通知值班经理接管非工作时间事件");
            return result(Decision.ESCALATE, "service-supervisor", utilization, remainingMinutes, reasons, actions);
        }

        if (utilization >= 80 || request.vipCustomer() || !request.assignedAgentAvailable()) {
            if (utilization >= 80) reasons.add("SLA 使用率达到预警阈值");
            if (request.vipCustomer()) reasons.add("客户为重点服务对象");
            actions.add("提升队列排序并在剩余时限内提醒责任人");
            return result(Decision.PRIORITIZE, request.vipCustomer() ? "vip-service" : "priority-service",
                    utilization, remainingMinutes, reasons, actions);
        }

        actions.add("维持当前坐席与队列，持续监控 SLA 和情绪变化");
        return result(Decision.MONITOR, channelQueue(request.channel()), utilization, remainingMinutes, reasons, actions);
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    private EscalationResult result(Decision decision, String queue, double utilization, int remainingMinutes,
                                    List<String> reasons, List<String> actions) {
        return new EscalationResult(decision, queue, utilization, remainingMinutes,
                List.copyOf(reasons), List.copyOf(actions));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    private String channelQueue(Channel channel) {
        return switch (channel) {
            case VOICE -> "voice-service";
            case CHAT -> "digital-service";
            case EMAIL -> "email-service";
            case SOCIAL -> "social-care";
        };
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    private double round(double value) {
        return Math.round(value * 100d) / 100d;
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record EscalationRequest(
            @NotBlank String interactionId,
            @NotNull Channel channel,
            @NotNull Priority priority,
            @PositiveOrZero int elapsedMinutes,
            @Positive int slaMinutes,
            @DecimalMin("0.0") @DecimalMax("1.0") double negativeSentimentScore,
            boolean complaint,
            boolean regulatoryCase,
            boolean vipCustomer,
            boolean securityRisk,
            boolean assignedAgentAvailable,
            boolean callbackConsent,
            boolean afterHours
    ) {}

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record EscalationResult(Decision decision, String targetQueue, double slaUtilizationPercent,
                                   int remainingMinutes, List<String> reasons, List<String> actions) {}

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public enum Channel { VOICE, CHAT, EMAIL, SOCIAL }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public enum Priority { P1, P2, P3, P4 }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public enum Decision { MONITOR, PRIORITIZE, ESCALATE, EMERGENCY }
}
