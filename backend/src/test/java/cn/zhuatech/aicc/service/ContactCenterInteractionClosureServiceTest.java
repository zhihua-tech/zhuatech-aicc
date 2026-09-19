/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.aicc.service;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
class ContactCenterInteractionClosureServiceTest {
    private final ContactCenterInteractionClosureService service = new ContactCenterInteractionClosureService();
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void closesCompliantInteraction() {
        var result = service.assess(new ContactCenterInteractionClosureService.Request("C1", true, true, true,
                true, true, false, false, true, true, true, true));
        assertThat(result.decision()).isEqualTo(ContactCenterInteractionClosureService.Decision.CLOSE);
    }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void reviewsQualityAndRetentionGaps() {
        var result = service.assess(new ContactCenterInteractionClosureService.Request("C2", true, true, true,
                true, true, false, false, true, false, false, false));
        assertThat(result.actions()).hasSize(3);
    }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void blocksComplianceFailures() {
        var result = service.assess(new ContactCenterInteractionClosureService.Request("C3", false, false, false,
                false, false, true, false, false, false, true, true));
        assertThat(result.blockers()).hasSize(6);
    }
}
