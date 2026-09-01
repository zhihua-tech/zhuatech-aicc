# 企业级客服交互结案治理

`POST /api/enterprise/aicc/interaction-closure` 校验客户身份、同意与告知、支付数据及敏感信息脱敏、投诉建单、强制质检、处置码和录音留存，返回 `CLOSE / REVIEW / BLOCKED`。

生产使用时建议接入录音平台、工单系统、质检抽样规则和数据保留策略，并对投诉及高风险会话进行不可跳过的人工复核。
