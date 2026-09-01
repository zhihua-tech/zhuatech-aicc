/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.aicc.service;

import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContactCenterInteractionClosureService {
    public Assessment assess(Request request) {
        List<String> blockers = new ArrayList<>();
        List<String> actions = new ArrayList<>();
        if (!request.identityVerified()) blockers.add("客户身份未按场景完成验证");
        if (!request.consentCaptured()) blockers.add("录音或数据处理同意未留痕");
        if (!request.requiredDisclosureCompleted()) blockers.add("必要告知未完成");
        if (!request.pciDataMasked()) blockers.add("支付卡数据未脱敏");
        if (!request.sensitiveDataRedacted()) blockers.add("会话敏感信息未脱敏");
        if (request.complaintFlagged() && !request.complaintCaseCreated()) blockers.add("客户投诉未创建正式工单");
        if (!blockers.isEmpty()) {
            actions.add("保持会话开启并完成合规处置");
            return new Assessment(Decision.BLOCKED, blockers, actions);
        }
        if ((request.qaReviewRequired() && !request.qaReviewCompleted())
                || !request.dispositionRecorded() || !request.recordingRetentionApplied()) {
            if (request.qaReviewRequired() && !request.qaReviewCompleted()) actions.add("完成强制质量复核");
            if (!request.dispositionRecorded()) actions.add("记录统一业务处置码");
            if (!request.recordingRetentionApplied()) actions.add("应用录音与会话留存策略");
            return new Assessment(Decision.REVIEW, blockers, actions);
        }
        actions.add("关闭交互并归档同意、处置、质检和留存证据");
        return new Assessment(Decision.CLOSE, blockers, actions);
    }

    public record Request(@NotBlank String interactionId, boolean identityVerified, boolean consentCaptured,
                          boolean requiredDisclosureCompleted, boolean pciDataMasked,
                          boolean sensitiveDataRedacted, boolean complaintFlagged,
                          boolean complaintCaseCreated, boolean qaReviewRequired, boolean qaReviewCompleted,
                          boolean dispositionRecorded, boolean recordingRetentionApplied) {}
    public record Assessment(Decision decision, List<String> blockers, List<String> actions) {}
    public enum Decision { CLOSE, REVIEW, BLOCKED }
}
