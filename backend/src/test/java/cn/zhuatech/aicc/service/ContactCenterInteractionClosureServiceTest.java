/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.aicc.service;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ContactCenterInteractionClosureServiceTest {
    private final ContactCenterInteractionClosureService service = new ContactCenterInteractionClosureService();
    @Test void closesCompliantInteraction() {
        var result = service.assess(new ContactCenterInteractionClosureService.Request("C1", true, true, true,
                true, true, false, false, true, true, true, true));
        assertThat(result.decision()).isEqualTo(ContactCenterInteractionClosureService.Decision.CLOSE);
    }
    @Test void reviewsQualityAndRetentionGaps() {
        var result = service.assess(new ContactCenterInteractionClosureService.Request("C2", true, true, true,
                true, true, false, false, true, false, false, false));
        assertThat(result.actions()).hasSize(3);
    }
    @Test void blocksComplianceFailures() {
        var result = service.assess(new ContactCenterInteractionClosureService.Request("C3", false, false, false,
                false, false, true, false, false, false, true, true));
        assertThat(result.blockers()).hasSize(6);
    }
}
