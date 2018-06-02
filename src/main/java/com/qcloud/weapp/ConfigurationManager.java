package com.qcloud.weapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.huanghuo.util.JsonUtil;
import org.apache.commons.io.FileUtils;


/**
 * 配置管理，使用该类进行 SDK 配置
 * */
public class ConfigurationManager {
	
	private static Configuration currentConfiguration;
	
	/**
	 * 获取当前的 SDK 配置
	 * */
	public static Configuration getCurrentConfiguration() throws ConfigurationException {
		if (currentConfiguration == null) {
			throw new ConfigurationException("SDK 还没有进行配置，请调用 ConfigurationManager.setup() 方法配置 SDK");
		}
		return currentConfiguration;
	}
	
	/**
	 * 使用指定的配置初始化 SDK
	 * 
	 * @param configuration 配置
	 * @see <a target="_blank" href="https://github.com/tencentyun/weapp-solution/wiki/%E6%9C%8D%E5%8A%A1%E7%AB%AF-SDK-%E9%85%8D%E7%BD%AE">服务端 SDK 配置</a>
	 * */
	public static void setup(Configuration configuration) throws ConfigurationException {
		if (configuration == null) {
			throw new ConfigurationException("配置不能为空");
		}
		if (configuration.getServerHost() == null) throw new ConfigurationException("服务器主机配置不能为空");
		if (configuration.getAuthServerUrl() == null) throw new ConfigurationException("鉴权服务器配置不能为空");
		if (configuration.getTunnelServerUrl() == null) throw new ConfigurationException("信道服务器配置不能为空");
		if (configuration.getTunnelSignatureKey() == null) throw new ConfigurationException("SDK 密钥配置不能为空");
		currentConfiguration = configuration;
	}
	
	/**
	 * 从配置文件初始化 SDK
	 * 
	 * @param configFilePath 配置文件的路径
	 * @see <a target="_blank" href="https://github.com/tencentyun/weapp-solution/wiki/%E6%9C%8D%E5%8A%A1%E7%AB%AF-SDK-%E9%85%8D%E7%BD%AE">服务端 SDK 配置</a>
	 * */
	public static void setupFromFile(String configFilePath) throws ConfigurationException {
		Configuration configuration = JsonUtil.objectFromJson(getConfigJson(configFilePath),Configuration.class);
		ConfigurationManager.setup(configuration);
	}

	private static String getConfigJson(String configFilePath) {
		String configJsonText="";
		try {
			configJsonText = FileUtils.readFileToString(new File(configFilePath), "utf8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return configJsonText;
	}
	
}

