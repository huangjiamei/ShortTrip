package com.example.controller;

/**
 * Created by damei on 18/1/16.
 */

import com.example.dao.*;
import com.example.dao.RequestBody;
import com.example.dao.ResponseBody;
import com.example.service.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController //告诉Spring以字符串的形式渲染结果，并直接返回给调用者
public class HelloController {
    final String urlPath = "http://59.110.30.187:8888/syncTasks";
    final String pkgNameQ = "com.qunar.travelplan";
    final String pkgNameY = "com.yaochufa.app";
    final String versionNameQ = "5.4.0";
    final String versionNameY = "5.9.0";
    ArrayList<AttractionInfo> locallist = new ArrayList<>(); //景点名，景点id，城市名（from去哪儿网）
    ArrayList<CityInfo> cityList = new ArrayList<>(); //城市信息列表（from要出发周边游）
    static String cities = "";
    ArrayList<SightInfo> sightList = new ArrayList<>(); //景点名，景点id（from要出发周边游）
    Map<String, String> map = new HashMap<>(); //当前所选城市的名称
    static String attractions = "";
    private final Logger logger = LoggerFactory.getLogger(this.getClass()); //根据此类名实例化一个日志记录器
    private VoiceBoxLocation voiceBoxLocation;
    @RequestMapping("/skills/short_trip")
    public ResponseBody Test(@org.springframework.web.bind.annotation.RequestBody RequestBody requestBody) {
        //getAttractionIntroduction("231");
        cityList = getCityList(); //推荐的城市列表
        for(int i=0; i<cityList.size(); i++) {
            cities = cities + cityList.get(i).getCityName();
        }
        //getRecommendedDestination("120100"); //要出发天津
        //getRecommendedDestination("299914"); //去哪儿网北京
        //getRecommendedDestination("110100"); //要出发北京

        ResponseBody responseBody = new ResponseBody(); //返回应答包体
        responseBody.setVersionid("1.0");
        responseBody.setSequence(requestBody.getSequence());
        responseBody.setTimestamp(new Date().getTime());
        HashMap<String, String> extend = new HashMap<>();
        extend.put("NO_REC", "0"); //取0时，让音箱响应用户的下一次拒识的输入
        responseBody.setExtend(extend);
        //若请求包体不为空
        if(requestBody!=null && !requestBody.getInput_text().equals("")) {
            logger.debug(requestBody.getInput_text()); //记录此次请求的内容
            //获取用户意图
            Directive directive = new Directive(); //播报的内容
            Map<String, String> slots = getIntent(requestBody.getInput_text());

            logger.debug(slots.get("operation"));
            if(slots.get("operation").equals("oneSentence")) {
                responseBody.setIs_end(true); //false表示本次会话还未完成
                DirectiveItems directiveItems = new DirectiveItems();
                directiveItems.setType("1"); //类型1为TTS播报内容，2为AUDIO的url连接
                //获取地理位置
                map.put("cityName", "");
                Gson gson = new Gson();
                String s = "";
                //logger.debug(requestBody.getExtend().get("POI"));
                if(!(requestBody.getExtend().get("POI") == null) && !requestBody.getExtend().get("POI").equals("")) {
                    ReqExtend VoiceBoxLocations = gson.fromJson("{\"POI\": " + requestBody.getExtend().get("POI") + "}",
                            new TypeToken<ReqExtend>() {
                            }.getType());
                    if (!requestBody.getExtend().isEmpty()) {
                        voiceBoxLocation = VoiceBoxLocations.getPOI().get(1);
                        String city = voiceBoxLocation.getCity();
                        map.put("cityName", city.substring(0, city.length() - 1));
                        String s1;
                        s1 = "好的，您现在的位置是" + city + "、";
                        locallist = getAttractionList(map.get("cityName"));
                        //先清空attractions
                        attractions = "";
                        for (int i = 0; i < locallist.size(); i++) {
                            attractions = attractions + locallist.get(i).getName() + "、";
                        }
                        //再调用"要出发"的接口查询景点
                        String cityCode = ""; //要查询的要出发周边游的cityCode
                        //遍历城市列表
                        for (int i = 0; i < cityList.size(); i++) {
                            if (cityList.get(i).getCityName().equals(map.get("cityName")))
                                cityCode = cityList.get(i).getCityCode();
                        }
                        sightList = getRecommendedDestination(cityCode); //用要出发周边游的cityId

                        for (int i = 0; i < sightList.size(); i++) {
                            if (!attractions.contains(sightList.get(i).getSightName()))
                                attractions = attractions + "、" + sightList.get(i).getSightName() + "、";
                        }

                        String s2 = "现有以下景点推荐：" + attractions;
                        s = s1 + s2;
                    } else
                        s = "好的，由于系统无法获取您当前所在的位置，请问您现在在哪个城市或者想查哪个城市呢";
                }
                else
                    s = "好的，由于系统无法获取您当前所在的位置，请问您现在在哪个城市或者想查哪个城市呢";
                logger.debug(s);

                directiveItems.setContent(s); //播报内容
                DirectiveItems[] directiveItemses = new DirectiveItems[1]; //播报的内容的条数，根据顺序播报
                directiveItemses[0] = directiveItems;
                directive.setDirective_items(directiveItemses);

            }

            else if(slots.get("operation").equals("open")) {

                responseBody.setIs_end(false); //false表示本次会话还未完成
                DirectiveItems directiveItems = new DirectiveItems();
                directiveItems.setType("1"); //类型1为TTS播报内容，2为AUDIO的url连接
                //获取地理位置
                map.put("cityName", "");
                Gson gson = new Gson();
                String s = "";
                if(!requestBody.getExtend().get("POI").equals("")) {
                    ReqExtend VoiceBoxLocations = gson.fromJson("{\"POI\": " + requestBody.getExtend().get("POI") + "}",
                            new TypeToken<ReqExtend>() {
                            }.getType());
                    //logger.debug(requestBody.getExtend().toString());
                    //logger.debug(requestBody.getExtend().get("POI"));


                    if (!requestBody.getExtend().isEmpty()) {
                        voiceBoxLocation = VoiceBoxLocations.getPOI().get(1);
                        String s1 = voiceBoxLocation.getCity();
                        //logger.debug(s1);
                        s = "好的，您现在的位置是" + s1 + "、";
                        map.put("cityName", s1.substring(0, s1.length() - 1));
                    } else
                        s = "好的，由于系统无法获取您当前所在的位置，请问您现在在哪个城市或者想查哪个城市呢";
                }
                else
                    s = "好的，由于系统无法获取您当前所在的位置，请问您现在在哪个城市或者想查哪个城市呢";

                directiveItems.setContent(s); //播报内容
                DirectiveItems[] directiveItemses = new DirectiveItems[1]; //播报的内容的条数，根据顺序播报
                directiveItemses[0] = directiveItems;
                directive.setDirective_items(directiveItemses);
            }
            else if(slots.get("operation").equals("findLocal") || slots.get("operation").equals("getLocal")) {

                if(slots.get("operation").equals("getLocal"))
                    map.put("cityName", slots.get("localCity"));

                    //查询本市景点
                    //调用getAttractionList接口
                    responseBody.setIs_end(false); //查询后不结束会话
                    DirectiveItems[] directiveItemses = new DirectiveItems[2];

                    DirectiveItems directiveItems = new DirectiveItems();
                    directiveItems.setType("1");
                    directiveItems.setContent(map.get("cityName") + "市内有以下景点推荐");
                    directiveItemses[0] = directiveItems;

                    DirectiveItems directiveItems1 = new DirectiveItems();

                    locallist = getAttractionList(map.get("cityName")); //问题所在
                    //logger.debug(map.get("cityName"));

                    //先清空attractions
                    attractions = "";
                    //logger.debug(attractions);
                    for (int i = 0; i < locallist.size(); i++) {
                        attractions = attractions + locallist.get(i).getName() + "、";
                        //logger.debug(locallist.get(i).getName());
                    }

                    //logger.debug(attractions);

                    //再调用"要出发"的接口查询景点
                    String cityCode = ""; //要查询的要出发周边游的cityCode
                    //遍历城市列表
                    for (int i = 0; i < cityList.size(); i++) {
                        if (cityList.get(i).getCityName().equals(map.get("cityName")))
                            cityCode = cityList.get(i).getCityCode();
                    }

                    //logger.debug(cityCode);

                    sightList = getRecommendedDestination(cityCode); //用要出发周边游的cityId

                    for (int i = 0; i < sightList.size(); i++) {
                        if (!attractions.contains(sightList.get(i).getSightName()))
                            attractions = attractions + "、" + sightList.get(i).getSightName() + "、";
                    }

                    directiveItems1.setContent(attractions);
                    directiveItems1.setType("1");
                    directiveItemses[1] = directiveItems1;
                    directive.setDirective_items(directiveItemses);

            }

            else if(attractions.contains(slots.get("operation"))) {
                //获取景点详细信息
                //调用getAttractionDetail接口
                responseBody.setIs_end(false);
                DirectiveItems[] directiveItemses = new DirectiveItems[1];

                DirectiveItems directiveItems = new DirectiveItems();
                directiveItems.setType("1");

                //根据第一轮的反馈结果获取景点id和城市id
                String cityId = "";
                String attractionId = "";
                for(int i=0; i<locallist.size(); i++) {
                    //若要查询的景点存在，则取出要查询景点的id和城市id，并把当前景点的名称放入map中
                    if(locallist.get(i).getName().equals(slots.get("operation"))){
                        attractionId = String.valueOf(locallist.get(i).getId());
                        cityId = String.valueOf(locallist.get(i).getCityId());
                        map.put("CityName", locallist.get(i).getName());
                    }
                }

                //再调用要出发周边游的getAttractionIntroduction接口
                String cityCode = "";
                for(int i=0; i<sightList.size(); i++) {
                    if(sightList.get(i).getSightName().equals(slots.get("operation"))){
                        cityCode = sightList.get(i).getSightId();
                        map.put("CityName", sightList.get(i).getSightName());
                    }
                }

                String detailAndIntroduction = "";
                String detail = "";
                String introduction = "";
                if(!cityId.equals("") && !attractionId.equals(""))
                    detail = getAttractionDetail(cityId, attractionId);
                if(!cityCode.equals(""))
                    introduction = getAttractionIntroduction(cityCode);
                detailAndIntroduction = detail + introduction;
                directiveItems.setContent(detailAndIntroduction);

                directiveItemses[0] = directiveItems;
                directive.setDirective_items(directiveItemses);
            }

            else if(slots.get("operation").equals("attention")) {
                responseBody.setIs_end(false);
                DirectiveItems[] directiveItemses = new DirectiveItems[1];
                DirectiveItems directiveItems = new DirectiveItems();
                directiveItems.setType("1");
                //根据第一轮的反馈结果获取景点id和城市id
                String cityId = "";
                String attractionId = "";
                for(int i=0; i<locallist.size(); i++) {
                    //查询当前所选择的景点的注意事项
                    if(locallist.get(i).getName().equals(map.get("CityName"))){
                        attractionId = String.valueOf(locallist.get(i).getId());
                        cityId = String.valueOf(locallist.get(i).getCityId());
                    }
                }
                if(!cityId.equals("") && !attractionId.equals(""))
                    directiveItems.setContent("该景点的注意事项如下：" + getAttention(cityId, attractionId));
                else
                    directiveItems.setContent("该景点暂无注意事项");
                directiveItemses[0] = directiveItems;
                directive.setDirective_items(directiveItemses);
            }
            else if(slots.get("operation").equals("search")) {
                String tag = slots.get("tag");
                responseBody.setIs_end(false);
                DirectiveItems[] directiveItemses = new DirectiveItems[2];

                DirectiveItems directiveItems = new DirectiveItems();
                directiveItems.setType("1");
                directiveItems.setContent(tag + "类有以下景点推荐");
                directiveItemses[0] = directiveItems;

                DirectiveItems directiveItems1 = new DirectiveItems();

                //先清空attractions
                attractions = "";
                locallist = searchAttraction(tag);
                for(int i=0; i<locallist.size(); i++) {
                    attractions = attractions + locallist.get(i).getName() + "、";
                }

                directiveItems1.setContent(attractions);
                directiveItems1.setType("1");
                directiveItemses[1] = directiveItems1;
                directive.setDirective_items(directiveItemses);
            }
            else if(slots.get("operation").equals("findAddress")) {
                responseBody.setIs_end(false);
                DirectiveItems[] directiveItemses = new DirectiveItems[1];
                DirectiveItems directiveItems = new DirectiveItems();
                directiveItems.setType("1");
                String address = "";
                String sightId = "";
                for(int i=0; i<sightList.size(); i++) {
                    if(sightList.get(i).getSightName().equals(map.get("CityName")))
                        sightId = sightList.get(i).getSightId();
                }
                if(!sightId.equals(""))
                    address = getAttractionAddress(sightId);
                else
                    address = "该景点暂无地址信息";
                directiveItems.setContent(address);
                directiveItemses[0] = directiveItems;
                directive.setDirective_items(directiveItemses);
            }
            else if(slots.get("operation").equals("stop")) {
                responseBody.setIs_end(true);
                DirectiveItems[] directiveItemses = new DirectiveItems[1];
                DirectiveItems directiveItems = new DirectiveItems();
                directiveItems.setType("1");
                directiveItems.setContent("再见");
                directiveItemses[0] = directiveItems;
                directive.setDirective_items(directiveItemses);
            }
            else{
                responseBody.setIs_end(true);
                DirectiveItems directiveItems = new DirectiveItems();
                directiveItems.setType("1");
                directiveItems.setContent("很抱歉，芭乐周边游暂不支持此功能，您可以这样说：让芭乐周边游帮我查一下附近有什么好玩的景点");
                DirectiveItems[] directiveItemses = new DirectiveItems[1];
                directiveItemses[0] = directiveItems;
                directive.setDirective_items(directiveItemses);
            }
            responseBody.setDirective(directive);
        }
        else
            logger.debug("请求内容为空");
        //logger.debug(responseBody.toString());
        return responseBody;
    }

    /*
    根据请求的内容提取用户意图
     */
    public Map<String, String> getIntent(String input_text) {
        Map<String, String> intent = new HashMap<>();
        input_text = input_text.replaceAll("\\pP", ""); //正则处理去掉句号

        final String oneSentence = "(让|请)?(打开|进入|启动)?(芭乐周边游)?(给|替|帮)?(我|俺)?(推荐)?(|查|搜|看)?(搜索)?(看看)?(查查)?(一)?(下)?" +
                "(附近)?(有)?(啥|什么)?(好玩)?(的)?(地方|景点)?";

        //final String open = "(打开|进入|启动).*芭乐周边游";

        //final String findLocal = "(芭乐周边游)?(给|替|帮)(我)?推荐(一)?(下)?(附近)?(有)?(啥|什么)?好玩的(地方)?";

        //final String getLocal = ".*(市)";
        final String findCity = "(芭乐周边游)?(给|帮|替)(我)?(想|要|在)?(去|查|搜索)?(一)?(下)?" +
                "(?<city>.+?)" + "(市)?(的)?(景点)?";

        final String attraction = input_text;

        final String attention = "(芭乐周边游)?(有)?(什么)?注意事项(吗)?";

        //final String search = "搜索";
        final String search = "(芭乐周边游)?(给|帮|替)?(我)?(搜|查)?(索)?(一)?(下)?" +
                "(?<sight>.+?)" + "类(的)?(景点)?";

        final String findAddress = "(芭乐周边游)?具体地址(是什么)?";

        final String stop = "(芭乐周边游)?(停|别说话|好了|够了)";

        if(input_text.matches(oneSentence))
            intent.put("operation", "oneSentence");


/*
        else if(input_text.matches(open))
            intent.put("operation", "open");

        else if(input_text.matches(findLocal))
            intent.put("operation", "findLocal");
*/

        /*
        else if(cities.contains(input_text)) {
            intent.put("operation", "getLocal");
            intent.put("localCity", input_text);
        }
        else if(cities.contains(input_text.substring(0, input_text.length()-1))) {
            intent.put("operation", "getLocal");
            intent.put("localCity", input_text.substring(0, input_text.length()-1));
        }
        */
        else if(input_text.matches(findCity)) {
            intent.put("operation", "getLocal");
            Pattern pattern = Pattern.compile(findCity);
            Matcher matcher = pattern.matcher(input_text);
            while (matcher.find()) {
                intent.put("localCity", matcher.group("city"));
            }
        }

        else if(attractions.contains(attraction)) {
            intent.put("operation", attraction);
        }

        else if(input_text.matches(attention))
            intent.put("operation", "attention");

        /*
        else if(input_text.contains(search)) {
            intent.put("operation", "search");
            intent.put("tag", input_text.substring(2));
        }
        */
        else if(input_text.matches(search)){
            intent.put("operation", "search");
            Pattern pattern = Pattern.compile(search);
            Matcher matcher = pattern.matcher(input_text);
            while (matcher.find()) {
                intent.put("tag", matcher.group("sight"));
            }
        }

        else if(input_text.matches(findAddress))
            intent.put("operation", "findAddress");

        else if(input_text.matches(stop))
            intent.put("operation", "stop");

        else
            intent.put("operation", "no");
        return intent;
    }

    /*
    提取位置信息
     */
    /*
    private PosInfo extractPoi(String string) {
        // TODO Auto-generated method stub
        //提取用户的位置信息
        JSONArray array;
        PosInfo result = new PosInfo();
        try {
            array = new JSONArray(string);
            //System.out.println(array.length());
            JSONObject com = (JSONObject) array.get(0);
            //System.out.println(com.get("province"));
            result.setLat((String)com.get("lat"));
            result.setLng((String)com.get("lng"));
            result.setProvince((String)com.get("province"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        };
        return result;
    }
    */

    /*
    调用去哪儿网getAttractionList接口
     */
    public ArrayList<AttractionInfo> getAttractionList(String cityName) {
        HttpRequest httpRequest = new HttpRequest();
        RequestContent requestContent = new RequestContent();
        requestContent.setPkgName(pkgNameQ);
        requestContent.setVersionName(versionNameQ);
        requestContent.setMethodName("getAttractionList");
        Map<String, String> map = new HashMap<>();
        map.put("cityName", cityName);
        //logger.debug(cityName);
        //将map对象转化为json
        Gson gson = new Gson();
        requestContent.setArgsJSONStr(gson.toJson(map));
        //再把整个对象转化成json
        String response = httpRequest.sendPost(urlPath, gson.toJson(requestContent));
        //logger.debug(response);
        //解析json字符串
        JSONObject jsonObject = new JSONObject(response);
        String returnJSONStr = jsonObject.getString("returnJSONStr"); //returnJSONStr是json字符串
        JSONObject jsonObject1 = new JSONObject(returnJSONStr);
        JSONArray jsonArray = jsonObject1.getJSONArray("result"); //result是json数组
        //遍历数组
        //查询出所有的景点后，将景点名，景点id，城市id存入locallist中
        //先将全部的list清空
        /*此方法不行
        for(int i=0; i<locallist.size(); i++) {
            logger.debug(locallist.get(i).getName());
            locallist.remove(i);
        }*/
        locallist = new ArrayList<>();
        //logger.debug(String.valueOf(locallist.size()));
        for(int i=0; i<jsonArray.length(); i++) {
            int cityId = jsonArray.getJSONObject(i).getInt("cityId");
            int attractionId = jsonArray.getJSONObject(i).getInt("id");
            String attractionName = jsonArray.getJSONObject(i).getString("name");
            AttractionInfo attractionInfo = new AttractionInfo();
            attractionInfo.setCityId(cityId);
            attractionInfo.setId(attractionId);
            attractionInfo.setName(attractionName);
            locallist.add(attractionInfo);
            //logger.debug(attractionName);
        }
        return locallist;
    }

    /*
    调用去哪儿网getAttractionDetail接口
     */
    public String getAttractionDetail(String cityId, String attractionId) {
        HttpRequest httpRequest = new HttpRequest();
        RequestContent requestContent = new RequestContent();
        requestContent.setPkgName(pkgNameQ);
        requestContent.setVersionName(versionNameQ);
        requestContent.setMethodName("getAttractionDetail");
        Map<String, String> map = new HashMap<>();
        map.put("cityId",cityId);
        map.put("attractionId", attractionId);
        Gson gson = new Gson();
        requestContent.setArgsJSONStr(gson.toJson(map));
        String response = httpRequest.sendPost(urlPath, gson.toJson(requestContent));

        JSONObject jsonObject = new JSONObject(response);
        String returnJSONStr = jsonObject.getString("returnJSONStr");
        //logger.debug(returnJSONStr);
        JSONObject jsonObject1 = new JSONObject(returnJSONStr);
        JSONObject result = jsonObject1.getJSONObject("result");
        String detail = result.getString("detail");
        //logger.debug(detail);
        return detail;
    }

    /*
    调用去哪儿网getAttention接口
     */
     public String getAttention(String cityId, String attractionId) {
         HttpRequest httpRequest = new HttpRequest();
         RequestContent requestContent = new RequestContent();
         requestContent.setPkgName(pkgNameQ);
         requestContent.setVersionName(versionNameQ);
         requestContent.setMethodName("getAttention");
         Map<String, String> map = new HashMap<>();
         map.put("cityId",cityId);
         map.put("attractionId", attractionId);
         Gson gson = new Gson();
         requestContent.setArgsJSONStr(gson.toJson(map));
         //logger.debug(gson.toJson(requestContent));
         String response = httpRequest.sendPost(urlPath, gson.toJson(requestContent));
         JSONObject jsonObject = new JSONObject(response);
         String returnJSONStr = jsonObject.getString("returnJSONStr");
         logger.debug(returnJSONStr);
         JSONObject jsonObject1 = new JSONObject(returnJSONStr);
         JSONObject result = jsonObject1.getJSONObject("result");
         String notice = "";
         if(result.length() != 2)
             notice = result.getString("notice");
         else
             notice = "该景点暂无注意事项";
         logger.debug(notice);
         return notice;
     }

     /*
     调用去哪儿网searchAttraction接口
      */
     public ArrayList<AttractionInfo> searchAttraction(String tag) {
         HttpRequest httpRequest = new HttpRequest();
         RequestContent requestContent = new RequestContent();
         requestContent.setPkgName(pkgNameQ);
         requestContent.setVersionName(versionNameQ);
         requestContent.setMethodName("searchAttraction");
         Map<String, String> map1 = new HashMap<>();
         //logger.debug(tag);
         map1.put("keyword", tag);
         map1.put("cityName", map.get("cityName"));
         //将map对象转化为json
         Gson gson = new Gson();
         requestContent.setArgsJSONStr(gson.toJson(map1));
         logger.debug(gson.toJson(map1));
         //再把整个对象转化成json
         String response = httpRequest.sendPost(urlPath, gson.toJson(requestContent));
         logger.debug(response);
         //解析json字符串
         JSONObject jsonObject = new JSONObject(response);
         String returnJSONStr = jsonObject.getString("returnJSONStr"); //returnJSONStr是json字符串
         JSONObject jsonObject1 = new JSONObject(returnJSONStr);
         JSONArray jsonArray = jsonObject1.getJSONArray("result"); //result是json数组
         //先将全部的list清空
         /*
         for(int i=0; i<locallist.size(); i++) {
             locallist.remove(i);
         }*/
         locallist = new ArrayList<>();
         //遍历数组
         for(int i=0; i<jsonArray.length(); i++) {
            int cityId = jsonArray.getJSONObject(i).getInt("cityId");
            int attractionId = jsonArray.getJSONObject(i).getInt("id");
            String attractionName = jsonArray.getJSONObject(i).getString("name");
            AttractionInfo attractionInfo = new AttractionInfo();
            attractionInfo.setCityId(cityId);
            attractionInfo.setId(attractionId);
            attractionInfo.setName(attractionName);
            if(!locallist.contains(attractionInfo))
                locallist.add(attractionInfo);
        }
        return locallist;
     }

     /*
     调用要出发周边游getCityList接口
      */
     public ArrayList<CityInfo> getCityList() {
         HttpRequest httpRequest = new HttpRequest();
         RequestContent requestContent = new RequestContent();
         requestContent.setPkgName(pkgNameY);
         requestContent.setVersionName(versionNameY);
         requestContent.setMethodName("getCityList");
         requestContent.setArgsJSONStr("\"\"");
         //再把整个对象转化成json
         Gson gson = new Gson();
         String response = httpRequest.sendPost(urlPath, gson.toJson(requestContent));
         //logger.debug(gson.toJson(requestContent));
         //logger.debug(response);
         //解析json字符串
         JSONObject jsonObject = new JSONObject(response);
         String returnJSONStr = jsonObject.getString("returnJSONStr"); //returnJSONStr是json字符串
         JSONObject jsonObject1 = new JSONObject(returnJSONStr);
         JSONArray jsonArray = jsonObject1.getJSONArray("result"); //result是json数组
         String citys = "";
         for(int i=0; i<jsonArray.length(); i++) {
             String cityCode = jsonArray.getJSONObject(i).getString("cityCode");
             String cityName = jsonArray.getJSONObject(i).getString("cityName");
             String cityNameAbbr = jsonArray.getJSONObject(i).getString("cityNameAbbr");
             String pinYinName = jsonArray.getJSONObject(i).getString("pinYinName");
             String provinceCode = jsonArray.getJSONObject(i).getString("provinceCode");
             CityInfo cityInfo = new CityInfo();
             cityInfo.setCityCode(cityCode);
             cityInfo.setCityName(cityName);
             cityInfo.setCityNameAbbr(cityNameAbbr);
             cityInfo.setPinyinName(pinYinName);
             cityInfo.setProvinceCode(provinceCode);
             cityList.add(cityInfo);
             //citys = citys + cityName+cityCode;
         }
         //logger.debug(citys);
         return cityList;
     }

     /*
     调用要出发周边游getRecommendedDestination接口
      */
     public ArrayList<SightInfo> getRecommendedDestination(String cityCode) {
         HttpRequest httpRequest = new HttpRequest();
         RequestContent requestContent = new RequestContent();
         requestContent.setPkgName(pkgNameY);
         requestContent.setVersionName(versionNameY);
         requestContent.setMethodName("getRecommendedDestination");
         Map<String, String> map = new HashMap<>();
         map.put("cityCode", cityCode);
         //将map对象转化为json
         Gson gson = new Gson();
         requestContent.setArgsJSONStr(gson.toJson(map));

         //再把整个对象转化成json
         String response = httpRequest.sendPost(urlPath, gson.toJson(requestContent));
         //logger.debug(gson.toJson(requestContent));
         //logger.debug(response);
         JSONObject jsonObject = new JSONObject(response);
         String returnJSONStr = jsonObject.getString("returnJSONStr"); //returnJSONStr是json字符串
         JSONObject jsonObject1 = new JSONObject(returnJSONStr);
         JSONArray jsonArray = jsonObject1.getJSONArray("result"); //result是json数组
         ArrayList<SightInfo> sights = new ArrayList<>();
         for(int i=0; i<jsonArray.length(); i++) {
             SightInfo sightInfo = new SightInfo();
             String sightName = jsonArray.getJSONObject(i).getString("name");
             String sightId = jsonArray.getJSONObject(i).getString("id");
             sightInfo.setSightName(sightName);
             sightInfo.setSightId(sightId);
             sights.add(sightInfo);
         }
         return sights;
     }

     /*
     调用要出发周边游的getAttractionIntroduction接口
      */
     public String getAttractionIntroduction(String sightId) {
         HttpRequest httpRequest = new HttpRequest();
         RequestContent requestContent = new RequestContent();
         requestContent.setPkgName(pkgNameY);
         requestContent.setVersionName(versionNameY);
         requestContent.setMethodName("getAttractionIntroduction");
         Map<String, String> map = new HashMap<>();
         map.put("id", sightId);
         //将map对象转化为json
         Gson gson = new Gson();
         requestContent.setArgsJSONStr(gson.toJson(map));
         String response = httpRequest.sendPost(urlPath, gson.toJson(requestContent));
         //logger.debug(response);
         JSONObject jsonObject = new JSONObject(response);
         String returnJSONStr = jsonObject.getString("returnJSONStr");
         JSONObject jsonObject1 = new JSONObject(returnJSONStr);
         JSONObject result = jsonObject1.getJSONObject("result");
         String intro = result.getString("intro");
         return intro;
     }

     /*
     调用要出发周边游getAttractionAddress
      */
     public String getAttractionAddress(String sightId) {
         HttpRequest httpRequest = new HttpRequest();
         RequestContent requestContent = new RequestContent();
         requestContent.setPkgName(pkgNameY);
         requestContent.setVersionName(versionNameY);
         requestContent.setMethodName("getAttractionAddress");
         Map<String, String> map = new HashMap<>();
         map.put("id", sightId);
         Gson gson = new Gson();
         requestContent.setArgsJSONStr(gson.toJson(map));
         String response = httpRequest.sendPost(urlPath, gson.toJson(requestContent));
         JSONObject jsonObject = new JSONObject(response);
         String returnJSONStr = jsonObject.getString("returnJSONStr");
         JSONObject jsonObject1 = new JSONObject(returnJSONStr);
         JSONObject result = jsonObject1.getJSONObject("result");
         String address = result.getString("address");
         return address;
     }


    //@RequestMapping(value = "/", method = RequestMethod.GET)
    /*
    @RequestMapping("/short_trip")
    public String say() {
        return "已为您打开芭乐周边游!";
    }

    @RequestMapping("/getAttractionList")
    public String getInfo(@RequestParam(value = "cityName", defaultValue = "北京") String cityName) {
        return cityName;
    }
    */

}
