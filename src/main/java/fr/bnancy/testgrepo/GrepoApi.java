package fr.bnancy.testgrepo;

import okhttp3.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.io.IOException;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by bertrand on 8/31/16.
 */
@Component
public class GrepoApi {

    @Autowired
    HttpClient http;

    protected final Logger logger = Logger.getLogger(this.getClass());

    private String SERVER = "https://fr.grepolis.com";

    private HashMap<String, String> sessionCookies = new HashMap<>();

    private String csrfToken;

    private String gameUrl;

    private JsonObject gameData;

    @SuppressWarnings("UnusedAssignment")
    public String login(String pseudo, String pass, String world) {
        Request request = http.builder()
                .url("https://fr.grepolis.com")
                .build();
        try {
            Response response = http.client().newCall(request).execute();

            List<Cookie> cookies = http.client().cookieJar().loadForRequest(request.url());

            sessionCookies.put("PHPSESSID", cookies.stream()
                    .filter(x -> x.name().equals("PHPSESSID"))
                    .findFirst()
                    .get()
                    .value());

            sessionCookies.put("XSRF-TOKEN", cookies.stream()
                    .filter(x -> x.name().equals("XSRF-TOKEN"))
                    .findFirst()
                    .get()
                    .value());

            sessionCookies.put("portal_tid", generatePortalTidHeader());
            sessionCookies.put("portal_data", generatePortalDataHeader(sessionCookies.get("portal_tid")));
            sessionCookies.put("metricsUvId", generateMetricsIdHeader());

            String cookieHeader = buildCookieHeader();

            request = http.builder()
                    .url(SERVER + "/glps/login_check")
                    .addHeader("Cookie", cookieHeader)
                    .addHeader("X-XSRF-TOKEN", sessionCookies.get("XSRF-TOKEN"))
                    .addHeader("X-Requested-With", "XMLHttpRequest")
                    .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),
                            "login[userid]=" + pseudo + "&login[password]=" + pass + "&login[remember_me]=false"))
                    .build();

            response = http.client().newCall(request).execute();
            JsonObject responseData = Json.createReader(new StringReader(response.body().string())).readObject();

            // PHPSESSID change in this request
            sessionCookies.remove("PHPSESSID");

            sessionCookies.put("PHPSESSID", http.client().cookieJar().loadForRequest(request.url()).stream()
                    .filter(x -> x.name().equals("PHPSESSID"))
                    .findFirst()
                    .get()
                    .value());

            cookieHeader = buildCookieHeader();

            request = http.builder()
                    .url(SERVER + responseData.getString("url"))
                    .addHeader("Cookie", cookieHeader)
                    .addHeader("Referer", SERVER)
                    .addHeader("Upgrade-Insecure-Requests", "1")
                    .build();

            response = http.client().newCall(request).execute();

            if (response.code() != 302) {
                return "unexpected code";
            }

            request = http.builder()
                    .url(response.header("Location"))
                    .addHeader("Cookie", cookieHeader)
                    .addHeader("Referer", SERVER)
                    .build();

            response = http.client().newCall(request).execute();

            sessionCookies.put("pid", http.client().cookieJar().loadForRequest(request.url()).stream()
                    .filter(x -> x.name().equals("pid"))
                    .findFirst()
                    .get()
                    .value());

            SERVER = "https://fr0.grepolis.com";

            request = http.builder()
                    .url(SERVER + response.header("Location"))
                    .addHeader("Cookie", buildCookieHeader())
                    .build();

            response = http.client().newCall(request).execute();

            sessionCookies.put("cid", http.client().cookieJar().loadForRequest(request.url()).stream()
                    .filter(x -> x.name().equals("cid"))
                    .findFirst()
                    .get()
                    .value());

            request = http.builder()
                    .url(SERVER + "/start?action=login_to_game_world")
                    .addHeader("Referer", SERVER + "/start/index")
                    .addHeader("Cookie", buildCookieHeader())
                    .addHeader("Upgrade-Insecure-Request", "1")
                    .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),
                            "world=" + world + "&name=" + pseudo + "&password=" + pass))
                    .build();

            response = http.client().newCall(request).execute();

            sessionCookies.put("login_startup_time", http.client().cookieJar().loadForRequest(request.url()).stream()
                    .filter(x -> x.name().equals("login_startup_time"))
                    .findFirst()
                    .get()
                    .value());

            request = http.builder()
                    .url(response.header("Location"))
                    .addHeader("Cookie", buildCookieHeader())
                    .build();

            response = http.client().newCall(request).execute();

            sessionCookies.put("sid", http.client().cookieJar().loadForRequest(request.url()).stream()
                    .filter(x -> x.name().equals("sid"))
                    .findFirst()
                    .get()
                    .value());
            sessionCookies.put("logged_in", http.client().cookieJar().loadForRequest(request.url()).stream()
                    .filter(x -> x.name().equals("logged_in"))
                    .findFirst()
                    .get()
                    .value());

            SERVER = "https://" + response.request().url().host();

            request = http.builder()
                    .url(SERVER + response.header("Location"))
                    .addHeader("Cookie", buildCookieHeader())
                    .build();

            response = http.client().newCall(request).execute();

            gameUrl = SERVER + response.request().url().encodedPath() + response.request().url().encodedQuery();

            gameData = parseGameData(response.body().string());

            csrfToken = gameData.getString("csrfToken");

            sessionCookies.put("toid", String.valueOf(gameData.getInt("townId")));
            sessionCookies.put("ig_conv_last_site", SERVER + response.request().url().encodedPath());
            sessionCookies.remove("portal_data");
            sessionCookies.remove("pid");
            sessionCookies.remove("XSRF-TOKEN");
            sessionCookies.remove("PHPSESSID");

            response.close();

            return "success";

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }

    public JsonObject fetchData() throws IOException {

        Request request = http.builder()
                .url(SERVER + "/game/data?town_id=" + gameData.get("townId") + "&action=get&h=" + csrfToken)
                .addHeader("Cookie", buildCookieHeader())
                .addHeader("Origin", SERVER)
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("Referer", gameUrl)
                .post(RequestBody.create(
                        MediaType.parse("application/x-www-form-urlencoded"),
                        "json=%7B%22types%22%3A%5B%7B%22type%22%3A%22map%22%2C%22param%22%3A%7B%22x%22%3A6%2C%22y%22%3A" +
                                "7%7D%7D%2C%7B%22type%22%3A%22bar%22%7D%2C%7B%22type%22%3A%22backbone%22%7D%5D%2C%22" +
                                "town_id%22%3A" + gameData.get("townId") + "%2C%22nl_init%22%3Afalse%7D"
                ))
                .build();
        Response response = http.client().newCall(request).execute();
        String body = response.body().string();
        response.close();
        return Json.createReader(new StringReader(body)).readObject().getJsonObject("json").getJsonObject("backbone");
    }

    public JsonObject fetchCollection(String collection) throws IOException {

        Request request = http.builder()
                .url(SERVER + "/game/frontend_bridge?town_id=" + gameData.get("townId") + "&action=refetch&h=" + csrfToken
                        + "&json=%7b%22collections%22%3a%7b%22" + collection + "%22%3a%5b%5d%7d%2c%22town_id%22%3a" +
                        gameData.get("townId") + "%2c%22nl_init%22%3afalse%7d")
                .addHeader("Cookie", buildCookieHeader())
                .addHeader("Origin", SERVER)
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("Referer", gameUrl)
                .build();
        Response response = http.client().newCall(request).execute();
        String body = response.body().string();
        response.close();
        return Json.createReader(new StringReader(body)).readObject().getJsonObject("json").getJsonObject("collections").
                getJsonObject(collection).getJsonArray("data").getJsonObject(0).getJsonObject("d");
    }

    public JsonObject fetchCollections(String collections[]) throws IOException {
        JsonObjectBuilder request = Json.createObjectBuilder();
        JsonObjectBuilder jsonCollections = Json.createObjectBuilder();

        for (String collection : collections) {
            jsonCollections.add(collection, Json.createArrayBuilder().build());
        }


        request.add("town_id", gameData.getInt("townId"));
        request.add("nl_init", false);
        request.add("collections", jsonCollections.build());

        String payload = URLEncoder.encode(request.build().toString(), "UTF-8");

        Request request1 = http.builder()
                .url(SERVER + "/game/frontend_bridge?town_id=" + gameData.get("townId") + "&action=refetch&h=" + csrfToken
                        + "&json=" + payload)
                .addHeader("Cookie", buildCookieHeader())
                .addHeader("Origin", SERVER)
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("Referer", gameUrl)
                .build();
        Response response = http.client().newCall(request1).execute();
        String body = response.body().string();
        response.close();

        return Json.createReader(new StringReader(body)).readObject();
    }

    public void notifyRequest() throws IOException {
        Request request = http.builder()
                .url(SERVER + "/game/notify?town_id=" + gameData.get("townId") + "&action=refetch&h=" + csrfToken
                        + "&json=%7B%22no_sysmsg%22%3Afalse%2C%22town_id%22%3A" + gameData.get("townId")
                        + "%2C%22nl_init%22%3Atrue%7D&_=1472729121057")
                .addHeader("Cookie", buildCookieHeader())
                .addHeader("Origin", SERVER)
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("Referer", gameUrl)
                .build();
        http.client().newCall(request).execute().close();
    }


    private JsonObject parseGameData(String body) {

        int startIndex = body.indexOf("window.Game = ") + "window.Game = ".length();

        char tmp = body.charAt(startIndex);
        StringBuilder sb = new StringBuilder();

        while (tmp != '\n') {
            sb.append(tmp);
            startIndex++;
            tmp = body.charAt(startIndex);
        }

        sb.deleteCharAt(sb.length() - 1);

        return Json.createReader(new StringReader(sb.toString())).readObject();
    }


    private String generatePortalDataHeader(String portalTid) {
        return "portal_tid=" + portalTid;
    }

    private String generatePortalTidHeader() {
        Date oExpirationDate = new Date();
        oExpirationDate.setMonth(oExpirationDate.getMonth() + 24);
        StringBuilder sb = new StringBuilder()
                .append(new Date().getTime())
                .append('-')
                .append(Math.round(Math.random() * Math.pow(10, 5)));
        return sb.toString();
    }

    private String generateMetricsIdHeader() {
        return UUID.randomUUID().toString();
    }

    private String buildCookieHeader() {
        StringBuilder sb = new StringBuilder();
        for (String name : sessionCookies.keySet()) {
            sb.append(name + "=" + sessionCookies.get(name) + ";");
        }
        return sb.toString();
    }

    public JsonObject getGameData() {
        return gameData;
    }

}
