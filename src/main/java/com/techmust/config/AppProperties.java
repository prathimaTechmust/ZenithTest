package com.techmust.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "app")
public class AppProperties 
{
    private final Auth m_oAuth = new Auth();
    private final OAuth2 m_oOauth2 = new OAuth2();

    public static class Auth 
    {
        private String m_strTokenSecret;
        private long m_nTokenExpirationMsec;

        public String getTokenSecret() 
        {
            return m_strTokenSecret;
        }

        public void setTokenSecret(String strTokenSecret) 
        {
            this.m_strTokenSecret = strTokenSecret;
        }

        public long getTokenExpirationMsec() 
        {
            return m_nTokenExpirationMsec;
        }

        public void setTokenExpirationMsec(long nTokenExpirationMsec) 
        {
            this.m_nTokenExpirationMsec = nTokenExpirationMsec;
        }
    }

    public static final class OAuth2
    {
        private List<String> m_arrAuthorizedRedirectUris = new ArrayList<>();

        public List<String> getAuthorizedRedirectUris() 
        {
            return m_arrAuthorizedRedirectUris;
        }

        public OAuth2 authorizedRedirectUris(List<String> arrAuthorizedRedirectUris) 
        {
            this.m_arrAuthorizedRedirectUris = arrAuthorizedRedirectUris;
            return this;
        }
    }

    public Auth getAuth() 
    {
        return m_oAuth;
    }

    public OAuth2 getOauth2() 
    {
        return m_oOauth2;
    }
}
