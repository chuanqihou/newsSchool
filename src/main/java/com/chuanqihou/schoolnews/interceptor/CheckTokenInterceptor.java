package com.chuanqihou.schoolnews.interceptor;

import com.chuanqihou.schoolnews.vo.ResultVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @auther 传奇后
 * @date 2022/11/12 1:34
 * @Description 登录token拦截器
 * @veersion 1.0
 */
@Component
public class CheckTokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从请求头中获取token参数
        String token = request.getHeader("token");

        //CORS 预检请求，服务器若接受该跨域请求，浏览器才继续发起正式请求。
        //判断是否为预检请求
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        //判断token是否为空
        if (token == null) {
            doResponse(response,new ResultVo(1002, "请登录！", null));
            return false;
        }

        //若不为空则验证token的合法性
        try {
            JwtParser parser = Jwts.parser();
            //加密key
            parser.setSigningKey("csmznews");
            //验证token
            Jws<Claims> claimsJws = parser.parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            doResponse(response,new ResultVo(1002, "登录过期，请重新登录！", null));
            return false;
        } catch (UnsupportedJwtException e) {
            doResponse(response,new ResultVo(1002, "token非法，请自重！", null));
            return false;
        } catch (Exception e) {
            doResponse(response,new ResultVo(1002, "请先登录！", null));
            return false;
        }
    }

    /**
     * 响应前端
     * @param response  响应
     * @param resultVo  数据
     */
    public static void doResponse(HttpServletResponse response,ResultVo resultVo) {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            String s = new ObjectMapper().writeValueAsString(resultVo);
            writer.print(s);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
