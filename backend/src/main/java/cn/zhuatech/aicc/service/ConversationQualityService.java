/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.aicc.service;
import jakarta.validation.constraints.*;import org.springframework.stereotype.Service;import java.util.*;
@Service public class ConversationQualityService {
 public Result evaluate(Request r){int score=100;List<String> actions=new ArrayList<>();if(!r.resolved()){score-=25;actions.add("补充客户问题闭环记录");}if(r.escalated()){score-=15;actions.add("复盘升级原因和知识缺口");}if(r.sentimentDelta()<0){score-=Math.min(20,(int)Math.abs(r.sentimentDelta()));actions.add("复核负向情绪节点与应答方式");}score-=Math.min(30,r.complianceFlags()*15);if(r.complianceFlags()>0)actions.add("由质检人员复核合规命中项");if(r.averageResponseSeconds()>r.responseSlaSeconds()){score-=15;actions.add("优化坐席响应节奏");}score=Math.max(0,score);String status=score>=85?"EXCELLENT":score>=65?"COACH":"REVIEW";if(actions.isEmpty())actions.add("会话质量良好，可沉淀为优秀案例");return new Result(score,status,actions);}
 public record Request(@NotBlank String conversationId,@Min(1) int turns,@NotNull Boolean resolved,@NotNull Boolean escalated,@Min(-100) @Max(100) int sentimentDelta,@Min(0) int complianceFlags,@Min(0) int averageResponseSeconds,@Min(1) int responseSlaSeconds){}
 public record Result(int qualityScore,String status,List<String> actions){}
}
