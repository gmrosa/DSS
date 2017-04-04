package br.com.furb.cripto;

import java.io.File;
import java.nio.charset.Charset;

public class Constants {
    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir") + File.separator + "tratamsg" + File.separator;

    public static final File tempDir = new File(Constants.TEMP_DIR);
    public static final Charset DEFAULT_CHARSET = java.nio.charset.StandardCharsets.UTF_8;

    public static void main(String[] args) {
	System.out.println(DEFAULT_CHARSET.toString());
    }
}
