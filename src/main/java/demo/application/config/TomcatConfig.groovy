package demo.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.server.Compression;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import java.util.concurrent.Executors;

import org.apache.coyote.http2.Http2Protocol;
//import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.core.task.TaskExecutor;

//@Slf4j
@Configuration
public class TomcatConfig  {

    protected Boolean compression;
    protected Integer maxThreads;
    protected Integer minSpareThreads;
    protected Integer maxConnections;

    @Bean("taskExecutor")
    public TaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(100);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("Async-");
        executor.initialize();
        return executor;
    }

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> servletContainerCustomizer() {
        return new WebServerFactoryCustomizer<TomcatServletWebServerFactory>() {

            @Override
            public void customize(TomcatServletWebServerFactory factory) {
                Compression comp = new Compression();
                comp.setEnabled(false);
                factory.setCompression(comp);
                factory.setUriEncoding(StandardCharsets.UTF_8);
                factory.addConnectorCustomizers(new TomcatConnectorCustomizer() {

                    @Override
                    public void customize(Connector connector) {
                        connector.addUpgradeProtocol(new Http2Protocol());
                        //AbstractHttp11Protocol<?> httpHandler = ((AbstractHttp11Protocol<?>) connector.getProtocolHandler());
                        Http11NioProtocol httpHandler = (Http11NioProtocol) connector.getProtocolHandler();
                        httpHandler.setAcceptCount(500);
                        httpHandler.setMaxKeepAliveRequests(20);
                        //httpHandler.setMaxKeepAliveRequests(-1);
                        httpHandler.setRejectIllegalHeader(true);
                        httpHandler.setMaxThreads(100);
                        httpHandler.setMinSpareThreads(10);
                        httpHandler.setMaxConnections(1000);
                        httpHandler.setUseKeepAliveResponseHeader(true);
                        httpHandler.setKeepAliveTimeout(60000);
                        //httpHandler.setExecutor(threadPoolTaskExecutor());
                        //httpHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
                    }
                });

            }

        };
    }


}
