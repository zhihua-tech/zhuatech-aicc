/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.aicc.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InteractionSlaEscalationServiceTest {
    private final InteractionSlaEscalationService service = new InteractionSlaEscalationService();

    @Test
    void monitorsHealthyChatInteraction() {
        var result = service.evaluate(request(20, 60, 0.2, false, false, false,
                false, true, false));
        assertThat(result.decision()).isEqualTo(InteractionSlaEscalationService.Decision.MONITOR);
        assertThat(result.targetQueue()).isEqualTo("digital-service");
        assertThat(result.remainingMinutes()).isEqualTo(40);
    }

    @Test
    void prioritizesVipBeforeBreach() {
        var result = service.evaluate(request(50, 60, 0.3, false, false, true,
                false, true, false));
        assertThat(result.decision()).isEqualTo(InteractionSlaEscalationService.Decision.PRIORITIZE);
        assertThat(result.reasons()).hasSize(2);
    }

    @Test
    void escalatesBreachedComplaintWithCallback() {
        var result = service.evaluate(request(70, 60, 0.9, true, false, false,
                false, true, true));
        assertThat(result.decision()).isEqualTo(InteractionSlaEscalationService.Decision.ESCALATE);
        assertThat(result.actions()).contains("按照客户同意的联系方式安排回呼");
    }

    @Test
    void sendsSecurityRiskToEmergencyQueue() {
        var result = service.evaluate(request(5, 60, 0.1, false, false, false,
                true, true, false));
        assertThat(result.decision()).isEqualTo(InteractionSlaEscalationService.Decision.EMERGENCY);
        assertThat(result.targetQueue()).isEqualTo("security-response");
    }

    private InteractionSlaEscalationService.EscalationRequest request(
            int elapsed, int sla, double sentiment, boolean complaint, boolean regulatory,
            boolean vip, boolean security, boolean agentAvailable, boolean callback) {
        return new InteractionSlaEscalationService.EscalationRequest("I-100", InteractionSlaEscalationService.Channel.CHAT,
                InteractionSlaEscalationService.Priority.P2, elapsed, sla, sentiment, complaint, regulatory,
                vip, security, agentAvailable, callback, false);
    }
}
