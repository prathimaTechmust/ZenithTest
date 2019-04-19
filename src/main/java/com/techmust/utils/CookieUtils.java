package com.techmust.utils;

import org.springframework.util.SerializationUtils;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Optional;

public class CookieUtils
{

    public static Optional<Cookie> getCookie(HttpServletRequest oRequest, String strCookieName)
    {
        Cookie[] arrCookies = oRequest.getCookies();

        if (arrCookies != null && arrCookies.length > 0) 
        {
            for (Cookie oCookie : arrCookies) 
                if (oCookie.getName().equals(strCookieName)) 
                    return Optional.of(oCookie);
        }
        return Optional.empty();
    }

    public static void addCookie(HttpServletResponse oResponse, String strCookieName, String strValue, int nMaxAge) 
    {
        Cookie oCookie = new Cookie(strCookieName, strValue);
        oCookie.setPath("/");
        oCookie.setHttpOnly(true);
        oCookie.setMaxAge(nMaxAge);
        oResponse.addCookie(oCookie);
    }

    public static void deleteCookie(HttpServletRequest oRequest, HttpServletResponse oResponse, String strCookieName) 
    {
        Cookie[] arrCookies = oRequest.getCookies();
        if (arrCookies != null && arrCookies.length > 0) 
        {
            for (Cookie oCookie: arrCookies) 
            {
                if (oCookie.getName().equals(strCookieName)) 
                {
                	oCookie.setValue("");
                	oCookie.setPath("/");
                	oCookie.setMaxAge(0);
                	oResponse.addCookie(oCookie);
                }
            }
        }
    }

    public static String serialize(Object oObject) 
    {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(oObject));
    }

    public static <T> T deserialize(Cookie oCookie, Class<T> tCls) 
    {
        return tCls.cast(SerializationUtils.deserialize(
                        Base64.getUrlDecoder().decode(oCookie.getValue())));
    }

}
