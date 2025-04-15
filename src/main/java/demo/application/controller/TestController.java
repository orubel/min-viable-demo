package demo.application.controller;

import demo.application.controller.BeapiRequestHandler;
import org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.*;

@Controller("test")
public class TestController extends BeapiRequestHandler{

	public String show(HttpServletRequest request, HttpServletResponse response){
		System.out.println("test/show");
        System.out.println("test/show called");
		return "Hello world";
    }

}
