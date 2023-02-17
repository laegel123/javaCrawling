package crawling.test.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
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
        String baseUrl = "https://search.shopping.naver.com/search/all?&sort=price_asc&query=";
        //String param = "에브리타임";

        String requestUrl = baseUrl + param;

        Map<String, String> resultMap = null;
        try {

            Document doc = null;
            doc = Jsoup.connect(requestUrl).get();

            Elements nameElements = doc.getElementsByAttributeValue("class", "basicList_title__VfX3c");
            Element firstNameElement = nameElements.get(0);

            Elements priceElements = doc.getElementsByAttributeValue("class", "price_num__S2p_v");
            Element firstPriceElement = priceElements.get(0);

            String productName = firstNameElement.select("a").text();
            String productPrice = firstPriceElement.text();

            resultMap = new HashMap<>();
            resultMap.put("productName", productName);
            resultMap.put("productPrice", productPrice);

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
