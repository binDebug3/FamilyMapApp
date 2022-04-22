package Handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;

/**
 * Handler super class
 */
public class Handler implements HttpHandler {
    /**
     * Default constructor
     */
    public Handler() {
        super();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }

    /**
     * Read the inputstream into a String
     * @param is inputStream to read
     * @return String version
     * @throws IOException if there is a problem reading the string
     */
    protected String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    /**
     * Write the String to an OutputStream
     * @param str String to write from
     * @param os OutpuStream object to write to
     * @throws IOException if there is a problem writing the stream
     */
    protected static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
