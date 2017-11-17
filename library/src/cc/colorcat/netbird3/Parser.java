package cc.colorcat.netbird3;


import java.io.IOException;

/**
 * A {@link Parser<T>} that provides a method to parse the {@link Response} into {@link NetworkData<T>}.
 * <p>
 * Created by cxx on 17-2-23.
 * xx.ch@outlook.com
 *
 * @see FileParser
 * @see StringParser
 */
public interface Parser<T> {
    /**
     * Parse the {@link Response} into {@link NetworkData<T>}.
     *
     * @param data The {@link Response} which will be parsed.
     * @return {@link NetworkData#newSuccess(T)} if success else {@link NetworkData#newFailure(int, String)}
     * @throws IOException if an I/O error occurs.
     * @see StateIOException
     */
    NetworkData<? extends T> parse(Response data) throws IOException;
}
