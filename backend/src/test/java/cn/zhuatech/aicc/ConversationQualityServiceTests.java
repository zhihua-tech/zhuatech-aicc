/* Copyright 2026 上海如静知华信息科技有限公司 */
package cn.zhuatech.aicc;
import cn.zhuatech.aicc.service.ConversationQualityService;import org.junit.jupiter.api.Test;import static org.junit.jupiter.api.Assertions.*;
class ConversationQualityServiceTests {private final ConversationQualityService service=new ConversationQualityService();@Test void recognizesExcellentConversation(){var r=service.evaluate(new ConversationQualityService.Request("C1",8,true,false,20,0,10,30));assertEquals("EXCELLENT",r.status());assertEquals(100,r.qualityScore());}@Test void sendsRiskyConversationToReview(){var r=service.evaluate(new ConversationQualityService.Request("C2",8,false,true,-30,2,60,30));assertEquals("REVIEW",r.status());}}
