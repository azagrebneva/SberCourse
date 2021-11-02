package sber.course.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sber.course.cache.CachingResult;
import sber.course.calculator.Calculator;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Задание 20.1. Написать WEB-приложение c сервлетом|сервлетным фильтром,
 * который осуществляет получение содержимого удалённого ресурса и возвращает
 * его в своём ответе (GET запрос).
 * Ссылка на ресурс передаётся в параметре url исходного запроса.
 * Сервлет должен кэшировать результаты успешных запросов (код ответа 2??),
 * и в случае повторного запроса возвращать сохранённый результат.
 * В ответе должен быть дополнительный заголовок с информацией о дате
 * занесения его в кэш (дате фактического получения ответа с удалённого сервера).
 * Используйте WeakHashMap в качестве структуры кэша, где URL ключ,
 * а значение - код ответа + заголовки + содержимое.
 */

@Controller
@RequestMapping("/calculator")
public class CachingController {

    private final Calculator calculator;

    private Map<String, CachingResult> results = new WeakHashMap<>();

    @Autowired
    public CachingController(Calculator calculator) {
        this.calculator = calculator;
    }

    // http://localhost:8080/calculator/factorial?number=3
    @GetMapping("factorial")
    @ResponseBody
    public String factorial(@RequestParam(value = "number",
            required = false, defaultValue = "1") int number,
                            HttpServletRequest request){

        CachingResult value = null;
        String url = request.getRequestURI() + request.getQueryString();

        if(!results.containsKey(url)){
            long result = calculator.factorial(number);
            value = new CachingResult(new Date(), number, result);
            results.put(url, value);
        } else {
            value = results.get(url);
        }

        return value.toString();
    }
}
