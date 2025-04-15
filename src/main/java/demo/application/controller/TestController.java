package demo.application.controller;

import demo.application.controller.BeapiRequestHandler;
import org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.*;
import org.springframework.scheduling.annotation.Async;
import java.util.concurrent.CompletableFuture;

@Controller("test")

public class TestController extends BeapiRequestHandler{

	// async
	@Async("taskExecutor")
	public CompletableFuture<String> show(HttpServletRequest request, HttpServletResponse response){
		return CompletableFuture.completedFuture("Hello world");
    }

    // normal
	/*
	public String show(HttpServletRequest request, HttpServletResponse response){
		return "Hello world";
	}
	
	 */
}
