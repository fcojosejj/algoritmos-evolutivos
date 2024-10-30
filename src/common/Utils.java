package common;

/**
 * Clase con métodos útiles para uso general.
 */
public class Utils {

    /**
     * Convierte la primera letra de la cadena especificada en mayúsculas.
     *
     * @param original la cadena original.
     * @return la cadena con la primera letra en mayúsculas.
     */
    public static String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }
}
