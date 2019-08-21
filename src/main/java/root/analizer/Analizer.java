package root.analizer;

/**
 * Interfaace that specifies a analize of some types of files
 * @author Lebedev
 */
public interface Analizer {

    /**
     * Open document, scan items, calculate a sum of found items
     * @return a sum of
     */
    double analizeDocument();

}
