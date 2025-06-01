package pwa.project.one_piece.security;

import javax.net.ssl.*;
import java.security.cert.X509Certificate;

/**
 * <h1>
 *     Helper class for manipulating SSL verification
 * </h1>
 */
public class SslUtil {

    private static SSLSocketFactory defaultSocketFactory;
    private static HostnameVerifier defaultHostnameVerifier;

    /**
     * <h2>
     *     Method for disabling SSL verification
     * </h2>
     * <p>
     *     The onepiece.fandom.com webpage didn't like the app having SSL enabled when scraping.
     *     This method is called to disable SSL verification before scraping data from it.
     * </p>
     */
    public static void disableSslVerification() {
        try {
            // Save defaults
            defaultSocketFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
            defaultHostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();

            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                    }
            };

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <h2>
     *     Method for enabling SSL verification
     * </h2>
     * <p>
     *     This method enables the SSL verification after data has been scraped.
     * </p>
     */
    public static void reenableSslVerification() {
        if (defaultSocketFactory != null) {
            HttpsURLConnection.setDefaultSSLSocketFactory(defaultSocketFactory);
        }
        if (defaultHostnameVerifier != null) {
            HttpsURLConnection.setDefaultHostnameVerifier(defaultHostnameVerifier);
        }
    }
}
