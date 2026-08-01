# AICC API 摘要

版权所有 © 2026 上海如静知华信息科技有限公司。

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/api/auth/login` | 登录并获取 JWT |
| GET | `/api/admin/dashboard` | 客户服务运营数据 |
| GET | `/api/admin/work-orders` | 客户服务会话清单 |
| GET | `/api/shopfloor/dashboard` | 智能坐席工作台 |
| POST | `/api/shopfloor/work-orders/{id}/reports` | 提交会话处理结果 |
| POST | `/api/shopfloor/ai-preview` | 调用可替换 AI Provider 生成回复建议 |
