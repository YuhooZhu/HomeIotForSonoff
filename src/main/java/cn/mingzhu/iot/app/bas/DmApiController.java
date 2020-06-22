//package cn.mingzhu.iot.app.bas;
//
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.oauth2.client.OAuth2ClientContext;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.security.oauth2.provider.OAuth2Authentication;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//
//import cn.mingzhu.iot.app.bas.constant.DmCode;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//public class DmApiController {
//
//	public final static DmCode ERROR = DmCode.NOT_FOUND;
//	public final static DmCode CREATED = DmCode.CREATED;
//	public final static DmCode READ = DmCode.OK;
//	public final static DmCode LISTED = DmCode.OK;
//	public final static DmCode UPDATED = DmCode.OK;
//	public final static DmCode REMOVED = DmCode.NO_CONTENT;
//	public final static DmCode DELETED = DmCode.NO_CONTENT;
//
//	public static boolean isOK(ResponseData rsp) {
//    	return DmCode.valueOf(rsp.getCode()).is2xxSuccessful();
//    }
//	
////	public Integer getSiteId() {
////		Integer siteId = null;
////		Map<String, Object> extraInfo = getExtraInfo();
////		try {
////			Object tokenSiteId = extraInfo.get("siteId");
////			siteId = Integer.parseInt(tokenSiteId.toString());
////		} catch (Exception ex) {
////			log.error("SITE => TOKEN extraInfo is bad", ex);
////		}
////		return siteId;
////	}
////	
////	public Integer getUserId() {
////		Integer userId = null;
////		Map<String, Object> extraInfo = getExtraInfo();
////		try {
////			Object tokenUserId = extraInfo.get("userId");
////			userId = Integer.parseInt(tokenUserId.toString());
////		} catch (Exception ex) {
////			log.error("USER => TOKEN extraInfo is bad", ex);
////		}
////		return userId;
////	}
////
////	private Map<String, Object> getExtraInfo() {
////		Map<String, Object> rslt = null;
////		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
////		log.info("Authentication={}", auth);
////		try {
////			Object authDetails = auth.getDetails();
////			if (authDetails != null) {
////				if (authDetails instanceof OAuth2AuthenticationDetails) {
////					OAuth2AuthenticationDetails oAuth2Details = (OAuth2AuthenticationDetails) authDetails;
////					
////					Object decodedDetails = oAuth2Details.getDecodedDetails();
////					
////					rslt = (Map<String, Object>)decodedDetails;
////				}
////			}
////		} catch (Exception ex) {
////			log.error("NEW => TOKEN Extra failed.", ex);
////		}
////
//////		log.info("NEW => extraInfo={}",  rslt);
////		
////	    return rslt;
////	}
//	
//	
//	@Autowired
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
//	/**
//	 * 当前请求用户是否为管理员
//	 * @return
//	 */
//	protected boolean isAdmin() {
//		LinkedHashMap detailHash = getOAuth2();
//		if (detailHash != null && detailHash.get("authorities") != null) {
//			try {
//				List<String> authorities = (List<String>) detailHash.get("authorities");
//				for (String authority : authorities) {
//					log.info("TOKEN authority={}", authority);
//					if ("ROLE_ADMIN".equalsIgnoreCase(authority) ) {
//						return true;
//					}
//				}
//			} catch (Exception ex) {
//				log.error("TOKEN parse authorities \"" + detailHash.get("authorities") + "\" failed", ex);
//			}
//		}
//		return false;
//	}
//	
//	private LinkedHashMap getOAuth2() {
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
//		LinkedHashMap oauth2Details = null;
//		if (details != null) {
//			if (details instanceof LinkedHashMap) {
//				oauth2Details = ( LinkedHashMap ) details;
//			}
//		}
//		return oauth2Details;
//    }
//	
//	HashMap<String, Object> srchFilter = null;
//	protected void initSearchFilter(String jsonFilter) {
//		Gson gson = new Gson();
//		srchFilter = gson.fromJson(jsonFilter, new TypeToken<HashMap<String, Object>>() {
//		}.getType());
//	}
//
//	@SuppressWarnings("unchecked")
//	protected <T> T getFilterValue(String param, Class<T> t) {
//		// 过滤处理 
//		if (srchFilter != null) {
//			log.debug("srchFilter={}", srchFilter);
//			Object value = null;
//			try {
//				value = srchFilter.get(param);
//			} catch (Exception ex) {
//			}
//			log.debug("Object value={}", value);
//			
//			if (value == null) {
//				return null;
//			}
//			
//			if(String.class.equals(t)){
//				log.info("String value={}", value);
//				return (T) ("".equals(value.toString().trim()) ? null : value.toString().trim());
//			} else if (Integer.class.equals(t)) {
//				log.info("Integer value={}", value);
//				Integer rslt = null;
//				try {
//					Double d = (Double) value;
//					rslt = d.intValue();
//				} catch (Exception ex) {
//				}
//				return (T) rslt;
//			} else if (Double.class.equals(t)) {
//				log.info("Double value={}", value);
//				Double rslt = null;
//				try {
//					rslt = (Double) value;
//				} catch (Exception ex) {
//				}
//				return (T) rslt;
//			}
//		}
//		return null;
//	}
//	
//}
