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
package demo.application.controller;


import org.springframework.web.servlet.support.RequestContextUtils;
import java.lang.reflect.Field;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.persistence.Entity;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.HttpRequestHandler;

import java.lang.reflect.Method;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import io.beapi.api.utils.UriObject

class BeapiRequestHandler implements HttpRequestHandler {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(BeapiRequestHandler.class);

    ApplicationContext ctx;
    protected String uri
    public String controller
    public String action

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
        //logger.info("handleRequest(HttpServletRequest, HttpServletResponse) : {}")
        println("### BeapiRequestHandler...")

        ctx = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());

        this.uri = request.getRequestURI()
        ArrayList uriVars = uri.split('/')
        this.controller = uriVars[1]
        this.action = uriVars[2]

        Object output

        Class<?> classObj = this.getClass()
        try {
            Method method = classObj.getMethod(action, HttpServletRequest.class, HttpServletResponse.class);

            // invoke method
            if (Objects.nonNull(method)) {
                try {
                    output = method.invoke(this, request, response);
                } catch (IllegalArgumentException e) {
                    logger.warn("[ BAD URI ] : YOU ARE ATTEMPTING TO CALL AN ENDPOINT THAT DOES NOT EXIST. IF THIS IS AN ISSUE, CHECK THAT THE CONTROLLER/METHOD EXISTS AND THAT IT IS PROPERLY REPRESENTED IN THE IOSTATE FILE.");
                    throw new Exception("[BeapiController > handleRequest] : IllegalArgumentException - full stack trace follows :", e);
                } catch (IllegalAccessException e) {
                    logger.warn("[ BAD URI ] : YOU ARE ATTEMPTING TO CALL AN ENDPOINT THAT DOES NOT EXIST. IF THIS IS AN ISSUE, CHECK THAT THE CONTROLLER/METHOD EXISTS AND THAT IT IS PROPERLY REPRESENTED IN THE IOSTATE FILE.");
                    throw new Exception("[BeapiController > handleRequest] : IllegalAccessException - full stack trace follows :", e);
                }catch (java.lang.reflect.InvocationTargetException e){
                    // ignore
                };
            };

            if (output != null) {
                PrintWriter writer = response.getWriter();
                writer.write(output);
                writer.close()

                response.writer.flush()

            } else {
                logger.warn("[ NO OUTPUT ] : OUTPUT EXPECTED AND NONE RETURNED. IF THIS IS AN ISSUE, CHECK THAT THE CONTROLLER/METHOD IS RETURNING THE PARAMS AS REPRESENTED IN THE APPROPRIATE IOSTATE FILE UNDER FOR THIS/CONTROLLER/METHOD (UNDER 'RESPONSE') AS A LINKEDHASHMAP.");
            };
        } catch (SecurityException e) {
            throw new Exception("[BeapiController > handleRequest] : SecurityException - full stack trace follows :", e);
        } catch (NoSuchMethodException e) {
            logger.warn("[ BAD URI ] : YOU ARE ATTEMPTING TO CALL AN ENDPOINT THAT DOES NOT EXIST. IF THIS IS AN ISSUE, CHECK THAT THE CONTROLLER/METHOD EXISTS AND THAT IT IS PROPERLY REPRESENTED IN THE IOSTATE FILE.");
            throw new Exception("[BeapiController > handleRequest] : NoSuchMethodException - full stack trace follows :", e);
        };
    };



}

