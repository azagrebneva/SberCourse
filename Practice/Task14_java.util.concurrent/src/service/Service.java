package service;

import annotation.Cache;

import java.util.Date;
import java.util.List;

import static annotation.CacheType.FILE;
import static annotation.CacheType.IN_MEMORY;

public interface Service {

    @Cache(cacheType = IN_MEMORY, fileNamePrefix = "data", zip = true, identityBy = {String.class, double.class})
    List<String> run(String item, double value, Date date);

    @Cache(cacheType = IN_MEMORY, zip = true, maxItemsAmountInList = 10)
    List<String> work(String item);
}
