package loader;

import annotation.Cache;

import java.util.Map;

import static annotation.CacheType.FILE;
import static annotation.CacheType.IN_MEMORY;

public interface Loader {

    @Cache(cacheType = FILE, fileNamePrefix = "loader", identityBy = {String.class})
    public Map<String,Integer> doSomething(String status);
}
