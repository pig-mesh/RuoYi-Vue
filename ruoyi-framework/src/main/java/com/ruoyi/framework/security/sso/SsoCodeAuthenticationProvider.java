package com.ruoyi.framework.security.sso;

/**
 * @author lengleng
 * @date 2022/3/28
 */

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.sign.Base64;
import com.ruoyi.common.utils.spring.SpringUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


/**
 * SSO登陆鉴权 Provider，要求实现 AuthenticationProvider 接口
 */
public class SsoCodeAuthenticationProvider implements AuthenticationProvider {
    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SsoCodeAuthenticationToken authenticationToken = (SsoCodeAuthenticationToken) authentication;

        String code = (String) authenticationToken.getPrincipal();

        String body = getAccessToken(code);

        String token = JSONObject.parseObject(body).getString("access_token");

        String ssoUser = getSsoUser(token);
        JSONObject sysUser = JSONObject.parseObject(ssoUser).getJSONObject("data").getJSONObject("sysUser");


        // 根据code 换username
        UserDetails userDetails = userDetailsService.loadUserByUsername(sysUser.getString("username"));

        // 此时鉴权成功后，应当重新 new 一个拥有鉴权的 authenticationResult 返回
        SsoCodeAuthenticationToken authenticationResult = new SsoCodeAuthenticationToken(userDetails, userDetails.getAuthorities());

        authenticationResult.setDetails(authenticationToken.getDetails());

        return authenticationResult;
    }


    @Override
    public boolean supports(Class<?> authentication) {
        // 判断 authentication 是不是 SsoCodeAuthenticationToken 的子类或子接口
        return SsoCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public String getAccessToken(String code) {
        HttpHeaders headers = buildRequestHeader();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        try {
            Environment environment = SpringUtils.getBean(Environment.class);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("grant_type", "authorization_code");
            map.add("scope", environment.getProperty("sso.scope"));
            map.add("code", code);

            String callback = environment.getProperty("sso.callback-url");
            String auth = environment.getProperty("sso.gateway-server");
            map.add("redirect_uri", callback);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            ResponseEntity<String> response = new RestTemplate().postForEntity(auth + "/auth/oauth2/token", request, String.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private HttpHeaders buildRequestHeader() {
        Environment environment = SpringUtils.getBean(Environment.class);
        String clientId = environment.getProperty("sso.client-id");
        String clientSecret = environment.getProperty("sso.client-secret");

        final String basicAuthorization = String.format("%s:%s", clientId, clientSecret);
        HttpHeaders headers = new HttpHeaders();

        String encodeToString = Base64.encode(basicAuthorization.getBytes());
        headers.add(HttpHeaders.AUTHORIZATION, "Basic " + encodeToString);
        return headers;
    }

    private String getSsoUser(String token) {
        Environment environment = SpringUtils.getBean(Environment.class);
        String auth = environment.getProperty("sso.gateway-server");
        HttpResponse execute = HttpRequest.get(auth + "/admin/user/info")
                .header("Authorization","Bearer "+token)
                .execute();
        return execute.body();

    }


}
