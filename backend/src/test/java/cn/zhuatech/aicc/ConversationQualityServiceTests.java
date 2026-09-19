/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.aicc;
import cn.zhuatech.aicc.service.ConversationQualityService;import org.junit.jupiter.api.Test;import static org.junit.jupiter.api.Assertions.*;
/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
class ConversationQualityServiceTests {private final ConversationQualityService service=new ConversationQualityService();/**
                                                                                                                          * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                                                                          */
@Test void recognizesExcellentConversation(){var r=service.evaluate(new ConversationQualityService.Request("C1",8,true,false,20,0,10,30));assertEquals("EXCELLENT",r.status());assertEquals(100,r.qualityScore());}/**
                                                                                                                                                                                                                                                                                                                                             * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                                                                                                                                                                                                                                                                                             */
@Test void sendsRiskyConversationToReview(){var r=service.evaluate(new ConversationQualityService.Request("C2",8,false,true,-30,2,60,30));assertEquals("REVIEW",r.status());}}
