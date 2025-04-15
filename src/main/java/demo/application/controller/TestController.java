package demo.application.controller;

import demo.application.controller.BeapiRequestHandler;
import org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.*;
import org.springframework.scheduling.annotation.Async;
import java.util.concurrent.CompletableFuture;

@Controller("test")
@Async("taskExecutor")
public class TestController extends BeapiRequestHandler{

	public CompletableFuture<String> show(HttpServletRequest request, HttpServletResponse response){
		return CompletableFuture.completedFuture("Hello world");
    }

}
