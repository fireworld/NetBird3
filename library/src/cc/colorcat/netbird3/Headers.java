package cc.colorcat.netbird3;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by cxx on 2017/2/23.
 * xx.ch@outlook.com
 */
public class Headers {
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_LENGTH = "Content-Length";
    private static volatile Headers emptyHeaders;

    public static Headers emptyHeaders() {
        if (emptyHeaders == null) {
            synchronized (Headers.class) {
                if (emptyHeaders == null) {
                    emptyHeaders = new Headers(Pair.EMPTY_PAIR);
                }
            }
        }
        return emptyHeaders;
    }

    public static Headers create(Map<String, List<String>> namesAndValues) {
        if (namesAndValues == null) {
            throw new NullPointerException("namesAndValues == null");
        }
        if (namesAndValues.isEmpty()) return Headers.emptyHeaders();
        int size = namesAndValues.size();
        List<String> names = new ArrayList<>(size);
        List<String> values = new ArrayList<>(size);
        for (Map.Entry<String, List<String>> entry : namesAndValues.entrySet()) {
            String k = entry.getKey();
            List<String> vs = entry.getValue();
            for (int i = 0, s = vs.size(); i < s; i++) {
                names.add(k);
                values.add(vs.get(i));
            }
        }
        return new Headers(names, values);
    }

    public static Headers create(List<String> names, List<String> values) {
        if (names == null) {
            throw new NullPointerException("names == null");
        }
        if (values == null) {
            throw new NullPointerException("values == null");
        }
        if (names.size() != values.size()) {
            throw new IllegalArgumentException("names.size() != values.size()");
        }
        if (names.isEmpty()) return Headers.emptyHeaders();
        return new Headers(new ArrayList<>(names), new ArrayList<>(values));
    }

    final Pair pair;

    private Headers(Pair pair) {
        this.pair = pair;
    }

    Headers(List<String> names, List<String> values) {
        pair = new Pair(Pair.NULL_BUT_CASE_SENSITIVE, names, values);
    }

    public String name(int index) {
        return pair.name(index);
    }

    public String value(int index) {
        return pair.value(index);
    }

    public List<String> names() {
        return pair.names();
    }

    public List<String> values() {
        return pair.values();
    }

    public String value(String name) {
        return pair.value(name);
    }

    public String value(String name, String defaultValue) {
        return pair.value(name, defaultValue);
    }

    public List<String> values(String name) {
        return pair.values(name);
    }

    public String contentType() {
        return pair.value(CONTENT_TYPE);
    }

    public long contentLength() {
        return quiteParse(pair.value(CONTENT_LENGTH), -1L);
    }

    public Charset charset() {
        return Utils.parseCharset(contentType());
    }

    public Set<String> nameSet() {
        return pair.nameSet();
    }

    public Map<String, List<String>> toMultimap() {
        return pair.toMultimap();
    }

    public int size() {
        return pair.size();
    }

    public boolean isEmpty() {
        return pair.isEmpty();
    }

    public boolean contains(String name) {
        return pair.contains(name);
    }

    public WritableHeaders newWritableHeaders() {
        return new WritableHeaders(new ArrayList<>(pair.names), new ArrayList<>(pair.values));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Headers headers = (Headers) o;

        return pair.equals(headers.pair);
    }

    @Override
    public int hashCode() {
        return 13 * pair.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + pair.toString() + '}';
    }

    private static long quiteParse(String value, long defValue) {
        if (value == null) return defValue;
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ignore) {
            return defValue;
        }
    }
}
