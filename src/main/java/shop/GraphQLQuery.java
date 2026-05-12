package shop;

/**
 * Interface générique représentant une requête GraphQL.
 *
 * @param <T> le type du résultat retourné après parsing
 */
public interface GraphQLQuery<T> {

  /** Retourne la requête GraphQL sous forme de string. */
  String getQuery();

  /** Transforme la réponse JSON brute en objet typé. */
  T parseResponse(String jsonResponse) throws Exception;
}
