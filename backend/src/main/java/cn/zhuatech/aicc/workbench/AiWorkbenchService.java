/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.aicc.workbench;

import static cn.zhuatech.aicc.workbench.WorkbenchInput.*;

import java.math.*;
import java.util.*;
import org.springframework.stereotype.Service;

/** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
@Service
public class AiWorkbenchService {
  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  public Map<String, Object> execute(Map<String, Object> r) {
    validate(r);

    keys(r, "calls");
    Set<String> ids = new HashSet<>();
    int answered = 0;
    long answeredWait = 0, handling = 0, abandonedWait = 0;
    List<Map<String, Object>> out = new ArrayList<>();
    for (var c : rows(r, "calls", 1, 500)) {
      keys(c, "id", "queuedAt", "answeredAt", "endedAt");
      String id = id(c, "id");
      unique(ids, id);
      var queued = instant(text(c, "queuedAt", 40));
      var ended = instant(text(c, "endedAt", 40));
      if (ended.isBefore(queued)) throw new IllegalArgumentException("结束时间不能早于排队");
      Object raw = c.get("answeredAt");
      if (!(raw instanceof String s)) throw new IllegalArgumentException("接通时间须为UTC时间或空字符串");
      if (s.isEmpty()) {
        long wait = java.time.Duration.between(queued, ended).toSeconds();
        abandonedWait += wait;
        out.add(
            Map.of("id", id, "status", "ABANDONED", "waitSeconds", wait, "handlingSeconds", 0L));
      } else {
        var connected = instant(s);
        if (connected.isBefore(queued) || connected.isAfter(ended))
          throw new IllegalArgumentException("接通时间必须位于排队与结束之间");
        long wait = java.time.Duration.between(queued, connected).toSeconds(),
            handle = java.time.Duration.between(connected, ended).toSeconds();
        answered++;
        answeredWait += wait;
        handling += handle;
        out.add(
            Map.of("id", id, "status", "ANSWERED", "waitSeconds", wait, "handlingSeconds", handle));
      }
    }
    int count = out.size();
    return Map.of(
        "answerPercent",
        round(answered * 100.0 / count),
        "abandonPercent",
        round((count - answered) * 100.0 / count),
        "answeredMetricsDefined",
        answered > 0,
        "averageAnsweredWaitSeconds",
        answered == 0 ? BigDecimal.ZERO : round(answeredWait / (double) answered),
        "averageHandlingSeconds",
        answered == 0 ? BigDecimal.ZERO : round(handling / (double) answered),
        "abandonedWaitSeconds",
        abandonedWait,
        "calls",
        out);
  }
}
