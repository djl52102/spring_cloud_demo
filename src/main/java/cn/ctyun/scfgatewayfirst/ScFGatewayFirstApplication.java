package cn.ctyun.scfgatewayfirst;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@SpringBootApplication
@RestController
public class ScFGatewayFirstApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScFGatewayFirstApplication.class, args);
	}

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder){
		String httpUrl="http://httpbin.org:80";
		return builder.routes().route(p->p.path("/get")
				.filters(f->f.addRequestHeader("hello","world"))
				.uri(httpUrl))
				.route(p->p.host("*.hystrix.com")
						.filters(f->f.hystrix(config -> config.setName("mycmd").setFallbackUri("forward:/fallback")))
						.uri(httpUrl)).build();
	}

	@RequestMapping("/fallback")
	public Mono<String> fallback(){
		return Mono.just("fallback");
	}
}
