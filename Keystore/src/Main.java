import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;

public class Main {

    private static final String KEY_STORE_PASSWORD = "123456";
    private static final String alias = "mydomain";
    private static final String currentPath = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    private static final String fileName = "keystore.crt";
    private static final File keyFile = new File(currentPath + File.separator + fileName);

    public static void main(String[] args) throws Throwable {
	KeyStore ks = KeyStore.getInstance("JKS");

	try (InputStream readStream = new FileInputStream(keyFile)) {
	    ks.load(readStream, KEY_STORE_PASSWORD.toCharArray());
	    Certificate certificate = ks.getCertificate(alias);
	    System.out.println(certificate);
	    Date validate = ((X509Certificate) certificate).getNotAfter();

	    if (new Date().compareTo(validate) != 1) {
		System.out.println("valid");
	    }
	}
    }

}
