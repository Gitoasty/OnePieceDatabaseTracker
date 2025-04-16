package pwa.project.one_piece.security;

import javax.net.ssl.*;
import java.security.cert.X509Certificate;

public class SslUtil {

    private static SSLSocketFactory defaultSocketFactory;
    private static HostnameVerifier defaultHostnameVerifier;

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

    public static void reenableSslVerification() {
        if (defaultSocketFactory != null) {
            HttpsURLConnection.setDefaultSSLSocketFactory(defaultSocketFactory);
        }
        if (defaultHostnameVerifier != null) {
            HttpsURLConnection.setDefaultHostnameVerifier(defaultHostnameVerifier);
        }
    }
}
