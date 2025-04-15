/*
 * Copyright 2013-2022 Owen Rubel
 * API Chaining(R) 2022 Owen Rubel
 *
 * Licensed under the AGPL v2 License;
 * you may not use this file except in compliance with the License.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Owen Rubel (orubel@gmail.com)
 *
 */
package demo.application.config;

import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.ApplicationContext;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import demo.application.controller.TestController;

@Configuration(proxyBeanMethods = false)
public class BeapiWebAutoConfiguration{

	@Autowired private ApplicationContext context;
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(BeapiWebAutoConfiguration.class);

	public BeapiWebAutoConfiguration() {

	}

	/**
	 *
	 * @return
	 */
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:message");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	/**
	 *
	 * @return
	 */
	@Bean(name='simpleUrlHandlerMapping')
	public SimpleUrlHandlerMapping simpleUrlHandlerMapping() {
		Map<String, Object> urlMap = new LinkedHashMap<>();
		urlMap += createControllerMappings()

		SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
		mapping.setUrlMap(urlMap);
		mapping.setOrder(Integer.MAX_VALUE - 5);
		mapping.setApplicationContext(context);
		return mapping;
	}

	@Bean
	public RequestMappingHandlerMapping requestMappingHandlerMapping() {
		RequestMappingHandlerMapping handler = super.requestMappingHandlerMapping();
		//now i have a handle on the handler i can lower it's priority
		//in the super class implementation this is set to 0
		handler.setOrder(Integer.MAX_VALUE);
		return handler;
	}

	@Bean
	public TestController testController() {
		return new TestController();
	}

	/**
	 *
	 * 	mapping needs to include 4 'callTypes' for load balancing:
	 * 	v : regular api call
	 * 	b : batching call
	 * 	c : chain call
	 * 	r : resource call
	 *
	 * 	This allows us the ability to move different call to different servers (should we want/need)
	 * 	so they do not affect 'regular calls' (ie 'v' callType)
	 *
	 * @param controller
	 * @param action
	 * @param apiVersion
	 * @param obj
	 * @return
	 */
	private Map createControllerMappings() {
		//String path = "${controller}/${action}" as String
		Map<String, Object> urlMap = new LinkedHashMap<>();

		try {
			List url = [
					"/test/show" as String,
			]
			url.each() { urlMap.put(it, testController()); }
		}catch(Exception e) {
			println("### BeapiWebAutoConfiguration > CreateControllerMappings : Exception : "+e)
		}
		return urlMap
	}

}



