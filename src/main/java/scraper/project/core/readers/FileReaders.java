package scraper.project.core.readers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FileReaders {
    private static final String rootSourcesFolder = String.format("src%smain%sresources%s", "/", "/", "/");
    private static final String proxyFile = "proxy.properties";

    public static void main(String[] args) {
        Properties properties = loadProxy();
        properties.getProperty(String.valueOf(1));
    }
    public static Properties loadProxy() {
        Properties proxy = new Properties();
        try (InputStream input = new FileInputStream(rootSourcesFolder + proxyFile)){
            proxy.load(input);
            return proxy;
        } catch (IOException e) {
            throw new RuntimeException("Can't find file " + proxyFile + " in " + rootSourcesFolder, e);
        }
    }
}
