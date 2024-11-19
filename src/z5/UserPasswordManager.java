package z5;

import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;

public class UserPasswordManager {

  public static void main(String[] args) {
    Map<String, String> users = new Hashtable<>();
    Scanner scanner = new Scanner(System.in);
    String username;
    String password;

    System.out.println(
      "Wprowadź nazwę użytkownika i hasło. Wpisz 'q' aby zakończyć."
    );

    while (true) {
      System.out.print("Nazwa użytkownika: ");
      username = scanner.nextLine();

      if (username.equals("q")) {
        break;
      }

      System.out.print("Hasło: ");
      password = scanner.nextLine();

      users.put(username, password);
      System.out.println("Para (nazwa użytkownika, hasło) została dodana.");
    }

    System.out.print("Podaj nazwę użytkownika, aby wyświetlić jego hasło: ");
    String searchUsername = scanner.nextLine();

    if (users.containsKey(searchUsername)) {
      System.out.println(
        "Hasło użytkownika '" +
        searchUsername +
        "' to: " +
        users.get(searchUsername)
      );
    } else {
      System.out.println("Nie znaleziono użytkownika o podanej nazwie.");
    }

    scanner.close();
  }
}
