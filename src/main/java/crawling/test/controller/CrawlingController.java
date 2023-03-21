package crawling.test.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CrawlingController {

    @GetMapping("/crawling/{param}")
    public Result crawlingTest(@PathVariable("param") String param) {
        String baseUrl = "https://search.shopping.naver.com/search/all?&fo=true&sort=price_asc&query=";
        String requestUrl = baseUrl + param;

        Map<String, String> resultMap = null;
        try {
            Connection.Response response = Jsoup.connect(requestUrl)
                    .method(Connection.Method.GET)
                    .timeout(1000)
                    .execute();
            Document doc = response.parse();

            Element firstNameElement = doc.select("div.basicList_title__VfX3c").first();
            Element firstPriceElement = doc.select("span.price_num__S2p_v").first();
            Element firstShopElement = doc.select("a.basicList_mall__BC5Xu").first();

            String productName = "";
            String productLink = "";
            String productPrice = "";
            String productShop = "";
            if (firstNameElement != null) {
                productName = firstNameElement.select("a").text();
                productLink = firstNameElement.select("a").attr("href");
            }
            if (firstPriceElement != null) {
                productPrice = firstPriceElement.text();
            }

            if (firstShopElement != null) {
                productShop = firstShopElement.text().equals("")  ? firstShopElement.select("img").attr("alt") : firstShopElement.text();
            }

            resultMap = new HashMap<>();
            resultMap.put("productName", productName);
            resultMap.put("productPrice", productPrice);
            resultMap.put("productLink", productLink);
            resultMap.put("productShop", productShop);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Result(resultMap);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }
}
