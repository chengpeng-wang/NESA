package com.splunk.mint;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Base64;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import com.splunk.mint.network.NetLogManager;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONObject;

public class MintJavascript {
    private static String mintBase64 = "dmFyIE1pbnQ9ZnVuY3Rpb24oKXtmdW5jdGlvbiB0KHQpe3JldHVybiJ1c2VyQWdlbnQiaW4gd2luZG93Lm5hdmlnYXRvcj8tMT09d2luZG93Lm5hdmlnYXRvci51c2VyQWdlbnQuaW5kZXhPZigiQW5kcm9pZCIpP3Q6Im9iamVjdCI9PXR5cGVvZiB0P0pTT04uc3RyaW5naWZ5KHQpOnZvaWQgMD09PXR8fG51bGw9PT10PyJ7fSI6SlNPTi5zdHJpbmdpZnkoe2Vycm9yOiJOb3QgYSB2YWxpZCBhcmd1bWVudC4gT25seSBKU09OIG9iamVjdCBhbGxvd2VkLiJ9KTpudWxsfXZhciBuPWZ1bmN0aW9uKHQsbil7dmFyIGk9dC5zdGFjay5tYXAoZnVuY3Rpb24odCl7cmV0dXJuIHQuZnVuYysiQCIrdC51cmwrIjoiK3QubGluZX0pLmpvaW4oIlxuIik7bWludEJyaWRnZS5qYXZhc2NyaXB0RXJyb3IodC5tZXNzYWdlLHQudXJsLHQuc3RhY2tbMF0ubGluZSxpLG4pfSxpPXdpbmRvdy5UcmFjZUtpdC5yZXBvcnQsZT1mdW5jdGlvbigpe3ZhciB0PVhNTEh0dHBSZXF1ZXN0LnByb3RvdHlwZS5vcGVuLG49WE1MSHR0cFJlcXVlc3QucHJvdG90eXBlLnNlbmQ7WE1MSHR0cFJlcXVlc3QucHJvdG90eXBlLm9wZW49ZnVuY3Rpb24obixpLGUsbyxyKXt0aGlzLl9tZXRob2Q9bix0aGlzLl91cmw9aSx0LmNhbGwodGhpcyxuLGksZSxvLHIpfSxYTUxIdHRwUmVxdWVzdC5wcm90b3R5cGUuc2VuZD1mdW5jdGlvbih0KXtmdW5jdGlvbiBpKCl7aWYoND09by5yZWFkeVN0YXRlKXt2YXIgdD0obmV3IERhdGUpLmdldFRpbWUoKSxuPXQtZSxpPXttZXRob2Q6cix1cmw6YSxsYXRlbmN5Om4udG9TdHJpbmcoKSxodHRwU3RhdHVzQ29kZTpvLnN0YXR1cy50b1N0cmluZygpLHJlc3BvbnNlRGF0YVNpemU6by5yZXNwb25zZVRleHQubGVuZ3RofTttaW50QnJpZGdlLmxvZ05ldHdvcmsoaS5tZXRob2QsaS51cmwsaS5sYXRlbmN5LGkuaHR0cFN0YXR1c0NvZGUsaS5yZXNwb25zZURhdGFTaXplKX19dmFyIGUsbz10aGlzLHI9dGhpcy5fbWV0aG9kLGE9dGhpcy5fdXJsO2U9KG5ldyBEYXRlKS5nZXRUaW1lKCksdGhpcy5hZGRFdmVudExpc3RlbmVyKCJyZWFkeXN0YXRlY2hhbmdlIixpLCExKSxuLmNhbGwodGhpcyx0KX19LG89ZnVuY3Rpb24oKXt7dmFyIHQ9d2luZG93LmxvY2F0aW9uLnBhdGhuYW1lLG49d2luZG93LmxvY2F0aW9uLmhvc3Q7d2luZG93LmxvY2F0aW9uLnByb3RvY29sfXNldFRpbWVvdXQoZnVuY3Rpb24oKXt2YXIgaT13aW5kb3cucGVyZm9ybWFuY2UudGltaW5nLmxvYWRFdmVudEVuZC13aW5kb3cucGVyZm9ybWFuY2UudGltaW5nLm5hdmlnYXRpb25TdGFydCxlPXdpbmRvdy5wZXJmb3JtYW5jZS50aW1pbmcuZG9tYWluTG9va3VwRW5kLXdpbmRvdy5wZXJmb3JtYW5jZS50aW1pbmcuZG9tYWluTG9va3VwU3RhcnQsbz13aW5kb3cucGVyZm9ybWFuY2UudGltaW5nLmRvbUNvbXBsZXRlLXdpbmRvdy5wZXJmb3JtYW5jZS50aW1pbmcuZG9tTG9hZGluZyxyPXdpbmRvdy5wZXJmb3JtYW5jZS50aW1pbmcucmVzcG9uc2VFbmQtd2luZG93LnBlcmZvcm1hbmNlLnRpbWluZy5yZXNwb25zZVN0YXJ0O21pbnRCcmlkZ2UubG9nVmlldyh0LGksZSxyLG8sbixudWxsKX0sMCl9O3JldHVybiBlKCksaS5zdWJzY3JpYmUoZnVuY3Rpb24odCl7dmFyIGk9InN0YWNrIj09PXQubW9kZT8idHJ1ZSI6ImZhbHNlIjtuKHQsaSl9KSx3aW5kb3cub25sb2FkPW8se2Vycm9yTG9nZ2VyOmksaW5pdEFuZFN0YXJ0U2Vzc2lvbjpmdW5jdGlvbih0KXttaW50QnJpZGdlLmluaXRBbmRTdGFydFNlc3Npb24odCl9LGxvZ0V2ZW50OmZ1bmN0aW9uKG4saSl7bWludEJyaWRnZS5sb2dFdmVudChuLHQoaSkpfSxsZWF2ZUJyZWFkY3J1bWI6ZnVuY3Rpb24odCl7bWludEJyaWRnZS5sZWF2ZUJyZWFkY3J1bWIodCl9LHRyYW5zYWN0aW9uU3RhcnQ6ZnVuY3Rpb24obixpKXttaW50QnJpZGdlLnRyYW5zYWN0aW9uU3RhcnQobix0KGkpKX0sdHJhbnNhY3Rpb25TdG9wOmZ1bmN0aW9uKG4saSl7bWludEJyaWRnZS50cmFuc2FjdGlvblN0b3Aobix0KGkpKX0sdHJhbnNhY3Rpb25DYW5jZWw6ZnVuY3Rpb24obixpLGUpe21pbnRCcmlkZ2UudHJhbnNhY3Rpb25DYW5jZWwobixpLHQoZSkpfSxhZGRFeHRyYURhdGE6ZnVuY3Rpb24odCxuKXttaW50QnJpZGdlLmFkZEV4dHJhRGF0YSh0LG4pfSxjbGVhckV4dHJhRGF0YTpmdW5jdGlvbigpe21pbnRCcmlkZ2UuY2xlYXJFeHRyYURhdGEoKX0sZmx1c2g6ZnVuY3Rpb24oKXtyZXR1cm4gbWludEJyaWRnZS5mbHVzaCgpfSxzdGFydFNlc3Npb246ZnVuY3Rpb24oKXttaW50QnJpZGdlLnN0YXJ0U2Vzc2lvbigpfSxjbG9zZVNlc3Npb246ZnVuY3Rpb24oKXttaW50QnJpZGdlLmNsb3NlU2Vzc2lvbigpfSxsb2dWaWV3OmZ1bmN0aW9uKG4saSl7bWludEJyaWRnZS5sb2dWaWV3KG4sbnVsbCxudWxsLG51bGwsbnVsbCxudWxsLHQoaSkpfSxzZXRVc2VySWRlbnRpZmllcjpmdW5jdGlvbih0KXttaW50QnJpZGdlLnNldFVzZXJJZGVudGlmaWVyKHQpfX19KCk7";
    private static String tracekitBase64 = "IWZ1bmN0aW9uKG4sZSl7ZnVuY3Rpb24gdChuLGUpe3JldHVybiBPYmplY3QucHJvdG90eXBlLmhhc093blByb3BlcnR5LmNhbGwobixlKX1mdW5jdGlvbiByKG4pe3JldHVybiBlPT09bn12YXIgbD17fSx1PW4uVHJhY2VLaXQsaT1bXS5zbGljZSxjPSI/IjtsLm5vQ29uZmxpY3Q9ZnVuY3Rpb24oKXtyZXR1cm4gbi5UcmFjZUtpdD11LGx9LGwud3JhcD1mdW5jdGlvbihuKXtmdW5jdGlvbiBlKCl7dHJ5e3JldHVybiBuLmFwcGx5KHRoaXMsYXJndW1lbnRzKX1jYXRjaChlKXt0aHJvdyBsLnJlcG9ydChlKSxlfX1yZXR1cm4gZX0sbC5yZXBvcnQ9ZnVuY3Rpb24oKXtmdW5jdGlvbiBlKG4pe2EoKSxwLnB1c2gobil9ZnVuY3Rpb24gcihuKXtmb3IodmFyIGU9cC5sZW5ndGgtMTtlPj0wOy0tZSlwW2VdPT09biYmcC5zcGxpY2UoZSwxKX1mdW5jdGlvbiB1KG4sZSl7dmFyIHI9bnVsbDtpZighZXx8bC5jb2xsZWN0V2luZG93RXJyb3JzKXtmb3IodmFyIHUgaW4gcClpZih0KHAsdSkpdHJ5e3BbdV0uYXBwbHkobnVsbCxbbl0uY29uY2F0KGkuY2FsbChhcmd1bWVudHMsMikpKX1jYXRjaChjKXtyPWN9aWYocil0aHJvdyByfX1mdW5jdGlvbiBjKG4sZSx0KXt2YXIgcj1udWxsO2lmKGcpbC5jb21wdXRlU3RhY2tUcmFjZS5hdWdtZW50U3RhY2tUcmFjZVdpdGhJbml0aWFsRWxlbWVudChnLGUsdCxuKSxyPWcsZz1udWxsLG09bnVsbDtlbHNle3ZhciBpPXt1cmw6ZSxsaW5lOnR9O2kuZnVuYz1sLmNvbXB1dGVTdGFja1RyYWNlLmd1ZXNzRnVuY3Rpb25OYW1lKGkudXJsLGkubGluZSksaS5jb250ZXh0PWwuY29tcHV0ZVN0YWNrVHJhY2UuZ2F0aGVyQ29udGV4dChpLnVybCxpLmxpbmUpLHI9e21vZGU6Im9uZXJyb3IiLG1lc3NhZ2U6bix1cmw6ZG9jdW1lbnQubG9jYXRpb24uaHJlZixzdGFjazpbaV0sdXNlcmFnZW50Om5hdmlnYXRvci51c2VyQWdlbnR9fXJldHVybiB1KHIsImZyb20gd2luZG93Lm9uZXJyb3IiKSxzP3MuYXBwbHkodGhpcyxhcmd1bWVudHMpOiExfWZ1bmN0aW9uIGEoKXtmIT09ITAmJihzPW4ub25lcnJvcixuLm9uZXJyb3I9YyxmPSEwKX1mdW5jdGlvbiBvKGUpe3ZhciB0PWkuY2FsbChhcmd1bWVudHMsMSk7aWYoZyl7aWYobT09PWUpcmV0dXJuO3ZhciByPWc7Zz1udWxsLG09bnVsbCx1LmFwcGx5KG51bGwsW3IsbnVsbF0uY29uY2F0KHQpKX12YXIgYz1sLmNvbXB1dGVTdGFja1RyYWNlKGUpO3Rocm93IGc9YyxtPWUsbi5zZXRUaW1lb3V0KGZ1bmN0aW9uKCl7bT09PWUmJihnPW51bGwsbT1udWxsLHUuYXBwbHkobnVsbCxbYyxudWxsXS5jb25jYXQodCkpKX0sYy5pbmNvbXBsZXRlPzJlMzowKSxlfXZhciBzLGYscD1bXSxtPW51bGwsZz1udWxsO3JldHVybiBvLnN1YnNjcmliZT1lLG8udW5zdWJzY3JpYmU9cixvfSgpLGwuY29tcHV0ZVN0YWNrVHJhY2U9ZnVuY3Rpb24oKXtmdW5jdGlvbiBlKGUpe2lmKCFsLnJlbW90ZUZldGNoaW5nKXJldHVybiIiO3RyeXt2YXIgdD1mdW5jdGlvbigpe3RyeXtyZXR1cm4gbmV3IG4uWE1MSHR0cFJlcXVlc3R9Y2F0Y2goZSl7cmV0dXJuIG5ldyBuLkFjdGl2ZVhPYmplY3QoIk1pY3Jvc29mdC5YTUxIVFRQIil9fSxyPXQoKTtyZXR1cm4gci5vcGVuKCJHRVQiLGUsITEpLHIuc2VuZCgiIiksci5yZXNwb25zZVRleHR9Y2F0Y2godSl7cmV0dXJuIiJ9fWZ1bmN0aW9uIHUobil7aWYoIXQoUyxuKSl7dmFyIHI9IiI7LTEhPT1uLmluZGV4T2YoZG9jdW1lbnQuZG9tYWluKSYmKHI9ZShuKSksU1tuXT1yP3Iuc3BsaXQoIlxuIik6W119cmV0dXJuIFNbbl19ZnVuY3Rpb24gaShuLGUpe3ZhciB0LGw9L2Z1bmN0aW9uIChbXihdKilcKChbXildKilcKS8saT0vWyciXT8oWzAtOUEtWmEteiRfXSspWyciXT9ccypbOj1dXHMqKGZ1bmN0aW9ufGV2YWx8bmV3IEZ1bmN0aW9uKS8sYT0iIixvPTEwLHM9dShuKTtpZighcy5sZW5ndGgpcmV0dXJuIGM7Zm9yKHZhciBmPTA7bz5mOysrZilpZihhPXNbZS1mXSthLCFyKGEpKXtpZih0PWkuZXhlYyhhKSlyZXR1cm4gdFsxXTtpZih0PWwuZXhlYyhhKSlyZXR1cm4gdFsxXX1yZXR1cm4gY31mdW5jdGlvbiBhKG4sZSl7dmFyIHQ9dShuKTtpZighdC5sZW5ndGgpcmV0dXJuIG51bGw7dmFyIGk9W10sYz1NYXRoLmZsb29yKGwubGluZXNPZkNvbnRleHQvMiksYT1jK2wubGluZXNPZkNvbnRleHQlMixvPU1hdGgubWF4KDAsZS1jLTEpLHM9TWF0aC5taW4odC5sZW5ndGgsZSthLTEpO2UtPTE7Zm9yKHZhciBmPW87cz5mOysrZilyKHRbZl0pfHxpLnB1c2godFtmXSk7cmV0dXJuIGkubGVuZ3RoPjA/aTpudWxsfWZ1bmN0aW9uIG8obil7cmV0dXJuIG4ucmVwbGFjZSgvW1wtXFtcXXt9KCkqKz8uLFxcXF4kfCNdL2csIlxcJCYiKX1mdW5jdGlvbiBzKG4pe3JldHVybiBvKG4pLnJlcGxhY2UoIjwiLCIoPzo8fCZsdDspIikucmVwbGFjZSgiPiIsIig/Oj58Jmd0OykiKS5yZXBsYWNlKCImIiwiKD86JnwmYW1wOykiKS5yZXBsYWNlKCciJywnKD86InwmcXVvdDspJykucmVwbGFjZSgvXHMrL2csIlxccysiKX1mdW5jdGlvbiBmKG4sZSl7Zm9yKHZhciB0LHIsbD0wLGk9ZS5sZW5ndGg7aT5sOysrbClpZigodD11KGVbbF0pKS5sZW5ndGgmJih0PXQuam9pbigiXG4iKSxyPW4uZXhlYyh0KSkpcmV0dXJue3VybDplW2xdLGxpbmU6dC5zdWJzdHJpbmcoMCxyLmluZGV4KS5zcGxpdCgiXG4iKS5sZW5ndGgsY29sdW1uOnIuaW5kZXgtdC5sYXN0SW5kZXhPZigiXG4iLHIuaW5kZXgpLTF9O3JldHVybiBudWxsfWZ1bmN0aW9uIHAobixlLHQpe3ZhciByLGw9dShlKSxpPVJlZ0V4cCgiXFxiIitvKG4pKyJcXGIiKTtyZXR1cm4gdC09MSxsJiZsLmxlbmd0aD50JiYocj1pLmV4ZWMobFt0XSkpP3IuaW5kZXg6bnVsbH1mdW5jdGlvbiBtKGUpe2Zvcih2YXIgdCxyLGwsdSxpPVtuLmxvY2F0aW9uLmhyZWZdLGM9ZG9jdW1lbnQuZ2V0RWxlbWVudHNCeVRhZ05hbWUoInNjcmlwdCIpLGE9IiIrZSxwPS9eZnVuY3Rpb24oPzpccysoW1x3JF0rKSk/XHMqXCgoW1x3XHMsXSopXClccypce1xzKihcU1tcc1xTXSpcUylccypcfVxzKiQvLG09L15mdW5jdGlvbiBvbihbXHckXSspXHMqXChldmVudFwpXHMqXHtccyooXFNbXHNcU10qXFMpXHMqXH1ccyokLyxnPTA7ZzxjLmxlbmd0aDsrK2cpe3ZhciBoPWNbZ107aC5zcmMmJmkucHVzaChoLnNyYyl9aWYobD1wLmV4ZWMoYSkpe3ZhciB2PWxbMV0/IlxccysiK2xbMV06IiIseD1sWzJdLnNwbGl0KCIsIikuam9pbigiXFxzKixcXHMqIik7dD1vKGxbM10pLnJlcGxhY2UoLzskLywiOz8iKSxyPVJlZ0V4cCgiZnVuY3Rpb24iK3YrIlxccypcXChcXHMqIit4KyJcXHMqXFwpXFxzKntcXHMqIit0KyJcXHMqfSIpfWVsc2Ugcj1SZWdFeHAobyhhKS5yZXBsYWNlKC9ccysvZywiXFxzKyIpKTtpZih1PWYocixpKSlyZXR1cm4gdTtpZihsPW0uZXhlYyhhKSl7dmFyIGQ9bFsxXTtpZih0PXMobFsyXSkscj1SZWdFeHAoIm9uIitkKyI9W1xcJ1wiXVxccyoiK3QrIlxccypbXFwnXCJdIiwiaSIpLHU9ZihyLGlbMF0pKXJldHVybiB1O2lmKHI9UmVnRXhwKHQpLHU9ZihyLGkpKXJldHVybiB1fXJldHVybiBudWxsfWZ1bmN0aW9uIGcobil7aWYoIW4uc3RhY2spcmV0dXJuIG51bGw7Zm9yKHZhciBlLHQscj0vXlxzKmF0ICg/OigoPzpcW29iamVjdCBvYmplY3RcXSk/XFMrKD86IFxbYXMgXFMrXF0pPykgKT9cKD8oKD86ZmlsZXxodHRwfGh0dHBzKTouKj8pOihcZCspKD86OihcZCspKT9cKT9ccyokL2ksbD0vXlxzKihcUyopKD86XCgoLio/KVwpKT9AKCg/OmZpbGV8aHR0cHxodHRwcykuKj8pOihcZCspKD86OihcZCspKT9ccyokL2ksdT1uLnN0YWNrLnNwbGl0KCJcbiIpLG89W10scz0vXiguKikgaXMgdW5kZWZpbmVkJC8uZXhlYyhuLm1lc3NhZ2UpLGY9MCxtPXUubGVuZ3RoO20+ZjsrK2Ype2lmKGU9bC5leGVjKHVbZl0pKXQ9e3VybDplWzNdLGZ1bmM6ZVsxXXx8YyxhcmdzOmVbMl0/ZVsyXS5zcGxpdCgiLCIpOiIiLGxpbmU6K2VbNF0sY29sdW1uOmVbNV0/K2VbNV06bnVsbH07ZWxzZXtpZighKGU9ci5leGVjKHVbZl0pKSljb250aW51ZTt0PXt1cmw6ZVsyXSxmdW5jOmVbMV18fGMsbGluZTorZVszXSxjb2x1bW46ZVs0XT8rZVs0XTpudWxsfX0hdC5mdW5jJiZ0LmxpbmUmJih0LmZ1bmM9aSh0LnVybCx0LmxpbmUpKSx0LmxpbmUmJih0LmNvbnRleHQ9YSh0LnVybCx0LmxpbmUpKSxvLnB1c2godCl9cmV0dXJuIG9bMF0mJm9bMF0ubGluZSYmIW9bMF0uY29sdW1uJiZzJiYob1swXS5jb2x1bW49cChzWzFdLG9bMF0udXJsLG9bMF0ubGluZSkpLG8ubGVuZ3RoP3ttb2RlOiJzdGFjayIsbmFtZTpuLm5hbWUsbWVzc2FnZTpuLm1lc3NhZ2UsdXJsOmRvY3VtZW50LmxvY2F0aW9uLmhyZWYsc3RhY2s6byx1c2VyYWdlbnQ6bmF2aWdhdG9yLnVzZXJBZ2VudH06bnVsbH1mdW5jdGlvbiBoKG4pe2Zvcih2YXIgZSx0PW4uc3RhY2t0cmFjZSxyPS8gbGluZSAoXGQrKSwgY29sdW1uIChcZCspIGluICg/Ojxhbm9ueW1vdXMgZnVuY3Rpb246IChbXj5dKyk+fChbXlwpXSspKVwoKC4qKVwpIGluICguKik6XHMqJC9pLGw9dC5zcGxpdCgiXG4iKSx1PVtdLGM9MCxvPWwubGVuZ3RoO28+YztjKz0yKWlmKGU9ci5leGVjKGxbY10pKXt2YXIgcz17bGluZTorZVsxXSxjb2x1bW46K2VbMl0sZnVuYzplWzNdfHxlWzRdLGFyZ3M6ZVs1XT9lWzVdLnNwbGl0KCIsIik6W10sdXJsOmVbNl19O2lmKCFzLmZ1bmMmJnMubGluZSYmKHMuZnVuYz1pKHMudXJsLHMubGluZSkpLHMubGluZSl0cnl7cy5jb250ZXh0PWEocy51cmwscy5saW5lKX1jYXRjaChmKXt9cy5jb250ZXh0fHwocy5jb250ZXh0PVtsW2MrMV1dKSx1LnB1c2gocyl9cmV0dXJuIHUubGVuZ3RoP3ttb2RlOiJzdGFja3RyYWNlIixuYW1lOm4ubmFtZSxtZXNzYWdlOm4ubWVzc2FnZSx1cmw6ZG9jdW1lbnQubG9jYXRpb24uaHJlZixzdGFjazp1LHVzZXJhZ2VudDpuYXZpZ2F0b3IudXNlckFnZW50fTpudWxsfWZ1bmN0aW9uIHYoZSl7dmFyIHI9ZS5tZXNzYWdlLnNwbGl0KCJcbiIpO2lmKHIubGVuZ3RoPDQpcmV0dXJuIG51bGw7dmFyIGwsYyxvLHAsbT0vXlxzKkxpbmUgKFxkKykgb2YgbGlua2VkIHNjcmlwdCAoKD86ZmlsZXxodHRwfGh0dHBzKVxTKykoPzo6IGluIGZ1bmN0aW9uIChcUyspKT9ccyokL2ksZz0vXlxzKkxpbmUgKFxkKykgb2YgaW5saW5lIyhcZCspIHNjcmlwdCBpbiAoKD86ZmlsZXxodHRwfGh0dHBzKVxTKykoPzo6IGluIGZ1bmN0aW9uIChcUyspKT9ccyokL2ksaD0vXlxzKkxpbmUgKFxkKykgb2YgZnVuY3Rpb24gc2NyaXB0XHMqJC9pLHY9W10seD1kb2N1bWVudC5nZXRFbGVtZW50c0J5VGFnTmFtZSgic2NyaXB0IiksZD1bXTtmb3IoYyBpbiB4KXQoeCxjKSYmIXhbY10uc3JjJiZkLnB1c2goeFtjXSk7Zm9yKGM9MixvPXIubGVuZ3RoO28+YztjKz0yKXt2YXIgaz1udWxsO2lmKGw9bS5leGVjKHJbY10pKWs9e3VybDpsWzJdLGZ1bmM6bFszXSxsaW5lOitsWzFdfTtlbHNlIGlmKGw9Zy5leGVjKHJbY10pKXtrPXt1cmw6bFszXSxmdW5jOmxbNF19O3ZhciB3PStsWzFdLHk9ZFtsWzJdLTFdO2lmKHkmJihwPXUoay51cmwpKSl7cD1wLmpvaW4oIlxuIik7dmFyIFM9cC5pbmRleE9mKHkuaW5uZXJUZXh0KTswPlN8fChrLmxpbmU9dytwLnN1YnN0cmluZygwLFMpLnNwbGl0KCJcbiIpLmxlbmd0aCl9fWVsc2UgaWYobD1oLmV4ZWMocltjXSkpe3ZhciBUPW4ubG9jYXRpb24uaHJlZi5yZXBsYWNlKC8jLiokLywiIiksJD1sWzFdLEU9UmVnRXhwKHMocltjKzFdKSk7cD1mKEUsW1RdKSxrPXt1cmw6VCxsaW5lOnA/cC5saW5lOiQsZnVuYzoiIn19aWYoayl7ay5mdW5jfHwoay5mdW5jPWkoay51cmwsay5saW5lKSk7dmFyIEY9YShrLnVybCxrLmxpbmUpLGI9Rj9GW01hdGguZmxvb3IoRi5sZW5ndGgvMildOm51bGw7ay5jb250ZXh0PUYmJmIucmVwbGFjZSgvXlxzKi8sIiIpPT09cltjKzFdLnJlcGxhY2UoL15ccyovLCIiKT9GOltyW2MrMV1dLHYucHVzaChrKX19cmV0dXJuIHYubGVuZ3RoP3ttb2RlOiJtdWx0aWxpbmUiLG5hbWU6ZS5uYW1lLG1lc3NhZ2U6clswXSx1cmw6ZG9jdW1lbnQubG9jYXRpb24uaHJlZixzdGFjazp2LHVzZXJhZ2VudDpuYXZpZ2F0b3IudXNlckFnZW50fTpudWxsfWZ1bmN0aW9uIHgobixlLHQscil7dmFyIGw9e3VybDplLGxpbmU6dH07aWYobC51cmwmJmwubGluZSl7bi5pbmNvbXBsZXRlPSExLGwuZnVuY3x8KGwuZnVuYz1pKGwudXJsLGwubGluZSkpLGwuY29udGV4dHx8KGwuY29udGV4dD1hKGwudXJsLGwubGluZSkpO3ZhciB1PS8gJyhbXiddKyknIC8uZXhlYyhyKTtpZih1JiYobC5jb2x1bW49cCh1WzFdLGwudXJsLGwubGluZSkpLG4uc3RhY2subGVuZ3RoPjAmJm4uc3RhY2tbMF0udXJsPT09bC51cmwpe2lmKG4uc3RhY2tbMF0ubGluZT09PWwubGluZSlyZXR1cm4hMTtpZighbi5zdGFja1swXS5saW5lJiZuLnN0YWNrWzBdLmZ1bmM9PT1sLmZ1bmMpcmV0dXJuIG4uc3RhY2tbMF0ubGluZT1sLmxpbmUsbi5zdGFja1swXS5jb250ZXh0PWwuY29udGV4dCwhMX1yZXR1cm4gbi5zdGFjay51bnNoaWZ0KGwpLG4ucGFydGlhbD0hMCwhMH1yZXR1cm4gbi5pbmNvbXBsZXRlPSEwLCExfWZ1bmN0aW9uIGQobixlKXtmb3IodmFyIHQscix1LGE9L2Z1bmN0aW9uXHMrKFtfJGEtekEtWlx4QTAtXHVGRkZGXVtfJGEtekEtWjAtOVx4QTAtXHVGRkZGXSopP1xzKlwoL2ksbz1bXSxzPXt9LGY9ITEsZz1kLmNhbGxlcjtnJiYhZjtnPWcuY2FsbGVyKWlmKGchPT1rJiZnIT09bC5yZXBvcnQpe2lmKHI9e3VybDpudWxsLGZ1bmM6YyxsaW5lOm51bGwsY29sdW1uOm51bGx9LGcubmFtZT9yLmZ1bmM9Zy5uYW1lOih0PWEuZXhlYygiIitnKSkmJihyLmZ1bmM9dFsxXSksdT1tKGcpKXtyLnVybD11LnVybCxyLmxpbmU9dS5saW5lLHIuZnVuYz09PWMmJihyLmZ1bmM9aShyLnVybCxyLmxpbmUpKTt2YXIgaD0vICcoW14nXSspJyAvLmV4ZWMobi5tZXNzYWdlfHxuLmRlc2NyaXB0aW9uKTtoJiYoci5jb2x1bW49cChoWzFdLHUudXJsLHUubGluZSkpfXNbIiIrZ10/Zj0hMDpzWyIiK2ddPSEwLG8ucHVzaChyKX1lJiZvLnNwbGljZSgwLGUpO3ZhciB2PXttb2RlOiJjYWxsZXJzIixuYW1lOm4ubmFtZSxtZXNzYWdlOm4ubWVzc2FnZSx1cmw6ZG9jdW1lbnQubG9jYXRpb24uaHJlZixzdGFjazpvLHVzZXJhZ2VudDpuYXZpZ2F0b3IudXNlckFnZW50fTtyZXR1cm4geCh2LG4uc291cmNlVVJMfHxuLmZpbGVOYW1lLG4ubGluZXx8bi5saW5lTnVtYmVyLG4ubWVzc2FnZXx8bi5kZXNjcmlwdGlvbiksdn1mdW5jdGlvbiBrKG4sZSl7dmFyIHQ9bnVsbDtlPW51bGw9PWU/MDorZTt0cnl7aWYodD1oKG4pKXJldHVybiB0fWNhdGNoKHIpe2lmKHkpdGhyb3cgcn10cnl7aWYodD1nKG4pKXJldHVybiB0fWNhdGNoKHIpe2lmKHkpdGhyb3cgcn10cnl7aWYodD12KG4pKXJldHVybiB0fWNhdGNoKHIpe2lmKHkpdGhyb3cgcn10cnl7aWYodD1kKG4sZSsxKSlyZXR1cm4gdH1jYXRjaChyKXtpZih5KXRocm93IHJ9cmV0dXJue21vZGU6ImZhaWxlZCJ9fWZ1bmN0aW9uIHcobil7bj0obnVsbD09bj8wOituKSsxO3RyeXt0aHJvdyBFcnJvcigpfWNhdGNoKGUpe3JldHVybiBrKGUsbisxKX19dmFyIHk9ITEsUz17fTtyZXR1cm4gay5hdWdtZW50U3RhY2tUcmFjZVdpdGhJbml0aWFsRWxlbWVudD14LGsuZ3Vlc3NGdW5jdGlvbk5hbWU9aSxrLmdhdGhlckNvbnRleHQ9YSxrLm9mQ2FsbGVyPXcsa30oKSxmdW5jdGlvbigpe3ZhciBlPWZ1bmN0aW9uKGUpe3ZhciB0PW5bZV07bltlXT1mdW5jdGlvbigpe3ZhciBuPWkuY2FsbChhcmd1bWVudHMpLGU9blswXTtyZXR1cm4iZnVuY3Rpb24iPT10eXBlb2YgZSYmKG5bMF09bC53cmFwKGUpKSx0LmFwcGx5P3QuYXBwbHkodGhpcyxuKTp0KG5bMF0sblsxXSl9fTtlKCJzZXRUaW1lb3V0IiksZSgic2V0SW50ZXJ2YWwiKX0oKSxsLnJlbW90ZUZldGNoaW5nfHwobC5yZW1vdGVGZXRjaGluZz0hMCksbC5jb2xsZWN0V2luZG93RXJyb3JzfHwobC5jb2xsZWN0V2luZG93RXJyb3JzPSEwKSwoIWwubGluZXNPZkNvbnRleHR8fGwubGluZXNPZkNvbnRleHQ8MSkmJihsLmxpbmVzT2ZDb250ZXh0PTExKSxuLlRyYWNlS2l0PWx9KHdpbmRvdyk7";
    private Context context;
    /* access modifiers changed from: private */
    public HashMap<String, Object> jExtraData = new HashMap(1);
    private WebView webview;

    @SuppressLint({"NewApi"})
    public static String loadMintJavascript() {
        StringBuilder sb = new StringBuilder();
        sb.append("javascript:");
        try {
            sb.append(new String(Base64.decode(tracekitBase64, 0), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            sb.append(new String(Base64.decode(mintBase64, 0), "UTF-8"));
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }
        return sb.toString();
    }

    public MintJavascript(Context context, WebView webView) {
        this.context = context;
        this.webview = webView;
        this.jExtraData.put("webview", Boolean.valueOf(true));
    }

    @JavascriptInterface
    public void javascriptError(String message, String file, String line, String stacktrace, String handled) {
        final String str = handled;
        final String str2 = message;
        final String str3 = file;
        final String str4 = line;
        final String str5 = stacktrace;
        new Thread(new Runnable() {
            public void run() {
                EnumExceptionType excType = EnumExceptionType.UNHANDLED;
                if (str != null && str.toLowerCase().contains("true")) {
                    excType = EnumExceptionType.HANDLED;
                }
                new ActionError(EnumActionType.error, str2, str3, str4, str5, MintJavascript.this.jExtraData, excType).save(new DataSaver());
            }
        }).start();
    }

    @JavascriptInterface
    public void logNetwork(String method, String url, String latency, String httpStatusCode, String responseDataSize) {
        NetLogManager.getInstance().logNetworkRequest(url, method, System.currentTimeMillis() - Long.valueOf(latency).longValue(), System.currentTimeMillis(), Integer.valueOf(httpStatusCode).intValue(), 0, Long.valueOf(responseDataSize).longValue(), "", this.jExtraData);
    }

    @JavascriptInterface
    public void initAndStartSession(String apikey) {
        Mint.initAndStartSession(this.context, apikey);
    }

    @JavascriptInterface
    public void addExtraData(String key, String value) {
        Mint.addExtraData(key, value);
    }

    @JavascriptInterface
    public void clearExtraData() {
        Mint.clearExtraData();
    }

    @JavascriptInterface
    public void closeSession() {
        Mint.closeSession(this.context);
    }

    @JavascriptInterface
    public void flush() {
        Mint.flush();
    }

    @JavascriptInterface
    public void leaveBreadcrumb(String breadcrumb) {
        Mint.leaveBreadcrumb(breadcrumb);
    }

    @JavascriptInterface
    public void logEvent(String event, String jsonExtra) {
        HashMap<String, Object> extraData = JsonStringToExtraData(jsonExtra);
        extraData.putAll(this.jExtraData);
        Mint.logEvent(event, MintLogLevel.Info, extraData);
    }

    @JavascriptInterface
    public void removeExtraData(String key) {
        Mint.removeExtraData(key);
    }

    @JavascriptInterface
    public void setLogging(int lines, String filter) {
        Mint.setLogging(lines, filter);
    }

    @JavascriptInterface
    public void setUserIdentifier(String userIdentifier) {
        Mint.setUserIdentifier(userIdentifier);
    }

    @JavascriptInterface
    public void startSession() {
        Mint.startSession(this.context);
    }

    @JavascriptInterface
    public void transactionStart(String name, String jsonExtra) {
        HashMap<String, Object> extraData = JsonStringToExtraData(jsonExtra);
        extraData.putAll(this.jExtraData);
        Mint.transactionStart(name, extraData);
    }

    @JavascriptInterface
    public void transactionStop(String name, String jsonExtra) {
        HashMap<String, Object> extraData = JsonStringToExtraData(jsonExtra);
        extraData.putAll(this.jExtraData);
        Mint.transactionStop(name, extraData);
    }

    @JavascriptInterface
    public void transactionCancel(String name, String reason, String jsonExtra) {
        HashMap<String, Object> extraData = JsonStringToExtraData(jsonExtra);
        extraData.putAll(this.jExtraData);
        Mint.transactionCancel(name, reason, extraData);
    }

    @JavascriptInterface
    public void logView(String currentView, String loadTime, String domainLookupTime, String serverTime, String domProcessingTime, String host, String jsonExtra) {
        Integer loadTimeInt;
        Integer domainLookupTimeInt;
        Integer serverTimeInt;
        Integer domProcessingTimeInt;
        HashMap<String, Object> extraData = new HashMap(2);
        extraData.put("webview", Boolean.valueOf(true));
        extraData.putAll(JsonStringToExtraData(jsonExtra));
        if (loadTime == null) {
            loadTimeInt = null;
        } else {
            loadTimeInt = Integer.valueOf(loadTime);
        }
        if (domainLookupTime == null) {
            domainLookupTimeInt = null;
        } else {
            domainLookupTimeInt = Integer.valueOf(domainLookupTime);
        }
        if (serverTime == null) {
            serverTimeInt = null;
        } else {
            serverTimeInt = Integer.valueOf(serverTime);
        }
        if (domProcessingTime == null) {
            domProcessingTimeInt = null;
        } else {
            domProcessingTimeInt = Integer.valueOf(domProcessingTime);
        }
        ActionView.logView(currentView, domainLookupTimeInt, domProcessingTimeInt, serverTimeInt, host, loadTimeInt, extraData);
    }

    private static synchronized HashMap<String, Object> JsonStringToExtraData(String jsontext) {
        HashMap<String, Object> extraData;
        synchronized (MintJavascript.class) {
            extraData = new HashMap(2);
            if (!(jsontext == null || jsontext.length() <= 4 || jsontext.equals("undefined"))) {
                try {
                    JSONObject jObj = new JSONObject(jsontext);
                    Iterator<String> keysItr = jObj.keys();
                    while (keysItr.hasNext()) {
                        String key = (String) keysItr.next();
                        extraData.put(key, jObj.get(key));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return extraData;
    }
}
