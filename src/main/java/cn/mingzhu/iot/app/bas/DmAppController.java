//package cn.mingzhu.iot.app.bas;
//
//import java.util.LinkedHashMap;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.oauth2.client.OAuth2ClientContext;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.security.oauth2.provider.OAuth2Authentication;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//
//import cn.mingzhu.iot.app.bas.constant.DmCode;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//public class DmAppController {
//
//	public static boolean isOK(ResponseData rsp) {
//    	return DmCode.valueOf(rsp.getCode()).is2xxSuccessful();
//    }
//
//    @Autowired
//	private OAuth2ClientContext oauth2ClientCtx;
//
//    @Autowired
//    private TokenStore tokenStore;
//
//	public Integer getSiteId() {
//		Integer siteId = null;
//		if (siteId == null) {
//			LinkedHashMap detailHash = getOAuth2();
//			if (detailHash != null && detailHash.get("siteId") != null) {
//				try {
//					siteId = Integer.parseInt(detailHash.get("siteId").toString());
//				} catch (NumberFormatException ex) {
//					log.error("TOKEN siteId \"" + detailHash.get("siteId") + "\" is bad", ex);
//				}
//			}
//		}
//		log.info("TOKEN SiteId={}", siteId);
//		return siteId;
//	}
//
//	public Integer getUserId() {
//		Integer userId = null;
//		LinkedHashMap detailHash = getOAuth2();
//		log.info("detailHash={}", detailHash);
//		if (detailHash != null && detailHash.get("userId") != null) {
//			try {
//				userId = Integer.parseInt(detailHash.get("userId").toString());
//			} catch (NumberFormatException ex) {
//				log.error("TOKEN userId \"" + detailHash.get("userId") + "\" is bad", ex);
//			}
//		}
//		log.info("TOKEN userId={}", userId);
//
//		return userId;
//	}
//
//	private LinkedHashMap getOAuth2() {
//		LinkedHashMap oauth2Details = null;
//
//		OAuth2AccessToken oauth2Token = oauth2ClientCtx.getAccessToken();
//		OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(oauth2Token);
//		Object details = oAuth2Authentication.getDetails();
//		
////		log.info("");
////		log.info("----------------------------------------------------");
////		log.info("oauth2Token={}", oauth2Token);
////		log.info("oAuth2Authentication={}", oAuth2Authentication);
////		log.info("details={}", details);
////		log.info("----------------------------------------------------");
////		log.info("");
//
//		if (details != null) {
//			if (details instanceof LinkedHashMap) {
//				oauth2Details = ( LinkedHashMap ) details;
//			}
//		}
//		return oauth2Details;
//    }
//	
//}
