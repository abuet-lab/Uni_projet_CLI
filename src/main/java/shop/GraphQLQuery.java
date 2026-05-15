package shop;

public interface GraphQLQuery<T> {

  String getQuery();

  T parseResponse(String jsonResponse) throws Exception;
}
