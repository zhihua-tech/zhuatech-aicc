/* Copyright 2026 上海如静知华信息科技有限公司 */
package cn.zhuatech.aicc.ai;
import org.springframework.stereotype.Component; import java.util.Map;
public interface AiProvider { AiResult execute(String prompt,Map<String,String> context); record AiResult(String provider,String answer,Map<String,Object> evidence){} }
@Component class DemoAiProvider implements AiProvider { public AiResult execute(String prompt,Map<String,String> context){return new AiResult("demo-service-provider","已生成带知识依据的演示回复，涉及业务承诺时必须由坐席确认。",Map.of("sentiment","neutral","citations",2,"humanReview",true));} }
