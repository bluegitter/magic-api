package org.ssssssss.magicapi.spring.boot.starter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;
import org.ssssssss.magicapi.utils.PathUtils;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * 输出服务访问地址
 *
 * @author 冰点
 * @date 2021-6-3 12:08:59
 * @since 1.2.1
 */
@Component
@ConditionalOnProperty(name = "magic-api.show-url", havingValue = "true", matchIfMissing = true)
@Order
public class ApplicationUriPrinter implements CommandLineRunner {
	@Resource
	private ConfigurableEnvironment springEnv;
	@Autowired
	private MagicAPIProperties properties;
	@Autowired
	private WebServerApplicationContext serverApplicationContext;

	@Override
	public void run(String... args) throws Exception {
		System.out.println("********************************************当前服务相关地址********************************************");
		String ip = "IP";
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			System.out.println("当前服务地址获取失败");
		}
		int port = serverApplicationContext.getWebServer().getPort();
		String path = springEnv.getProperty("server.servlet.context-path", "");
		String magicWebPath = properties.getWeb();
		String schema = "http://";
		String localUrl = schema + PathUtils.replaceSlash(String.format("localhost:%s/%s/%s/",port,path, Objects.toString(properties.getPrefix(),"")));
		String externUrl = schema + PathUtils.replaceSlash(String.format("%s:%s/%s/%s/",ip,port,path, Objects.toString(properties.getPrefix(),"")));
		System.out.println(
				"服务启动成功，magic-api已内置启动! Access URLs:\n\t" +
						"接口本地地址: \t\t"+localUrl+"\n\t" +
						"接口外部访问地址: \t" + externUrl
		);
		if (!StringUtils.isEmpty(magicWebPath)) {
            String webPath = schema + PathUtils.replaceSlash(String.format("%s:%s/%s/%s/index.html", ip, port, path, magicWebPath));
            System.out.println("\t接口配置平台: \t\t" + webPath);
		}
		System.out.println("\t可通过配置关闭输出: \tmagic-api.show-url=false");
		System.out.println("********************************************当前服务相关地址********************************************");

	}
}
