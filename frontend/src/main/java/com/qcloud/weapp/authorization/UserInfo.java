package com.qcloud.weapp.authorization;

import com.huanghuo.common.util.JsonUtil;

/**
 * 表示微信用户信息.
 */
public class UserInfo {
	
	/** The open id. */
	private String openId;
	
	/** The nick name. */
	private String nickName;
	
	/** The avatar url. */
	private String avatarUrl;
	
	/** The gender. */
	private Integer gender;
	
	/** The language. */
	private String language;
	
	/** The city. */
	private String city;
	
	/** The province. */
	private String province;
	
	/** The country. */
	private String country;

	/**
	 *
	 * @param jsonStr
	 * @return
	 */
	static UserInfo BuildFromJson(String jsonStr) {
		return JsonUtil.objectFromJson(jsonStr, UserInfo.class);
	}

	/**
	 * Gets the open id.
	 *
	 * @return the open id
	 */
	public String getOpenId() {
		return openId;
	}

	/**
	 * Gets the nick name.
	 *
	 * @return the nick name
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * Gets the avatar url.
	 *
	 * @return the avatar url
	 */
	public String getAvatarUrl() {
		return avatarUrl;
	}

	/**
	 * Gets the gender.
	 *
	 * @return the gender
	 */
	public Integer getGender() {
		return gender;
	}

	/**
	 * Gets the language.
	 *
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * Gets the city.
	 *
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Gets the province.
	 *
	 * @return the province
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * Gets the country.
	 *
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
}
