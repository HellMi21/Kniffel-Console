/**
 * Kniffel in Java (basierend auf Kniffel in StarBasic)
 * Copyright (C) 2024 M.Hilmi
 * <p>
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation; either version 3 of the License, or (at your
 * option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program; if not, see
 * <http://www.gnu.org/licenses/>.
 */
//test

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.swing.text.Position;

public class Kniffel {
  private static Dices dices;
  private static Score scoreboard;
  private static Scanner scanner;


  private static final String VERSION = "v1.01";
  private static final String LANG_EN = "lang_en";

  public static void main(String[] args) {
    init();
  }

  static char readCharacter() {
    return new Scanner(System.in).next().charAt(0);
  }

  static int readInteger() {
    return new Scanner(System.in).nextInt();
  }

  public static void license() {
    displayLicense();
    init();
  }

  public static void displayLicense() {
    System.out.print(getLicenseText());
  }

  private static String getLicenseText() {
    return "\nKniffel in Java (basierend auf Kniffel in StarBasic)\n" +
        "Copyright (C) 2024 M.Hilmi\n\n" +
        "This program is free software; you can redistribute it and/or modify it under the terms " +
        "of the GNU General Public License as published by the Free Software Foundation; either version 3 of " +
        "the License, or (at your option) any later version.\n" +
        "This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; " +
        "without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. " +
        "See the GNU General Public License for more details.\n" +
        "You should have received a copy of the GNU General Public License along with this program; " +
        "if not, see <http://www.gnu.org/licenses/>.\n";
  }

  private static void showDetails() {
    System.out.println("\nKniffel in Java " + VERSION);
    System.out.println("Copyright (C) 2024 M.Hilmi");
    System.out.println("\nFür dieses Programm besteht KEINERLEI GARANTIE.");
    System.out.println(
        "Dies ist freie Software, die Sie unter bestimmten Bedingungen weitergeben dürfen.");
  }

  public static void init() {
    showDetails();
    System.out.print("L: Lizenz anzeigen  N: Neues Spiel  Q: Beenden  ");
    handleMenuChoice(readCharacter());
  }

  private static void handleMenuChoice(char menu) {
    switch (Character.toLowerCase(menu)) {
      case 'l':
        license();
        break;
      case 'n':
        newGame();
        break;
      case 'q':
        System.exit(0);
        break;
      default:
        System.out.println("Ungültige Eingabe\n");
        init();
        break;
    }
  }

  public static void newGame() {
    scoreboard = new Score();
    dices = new Dices();
    //scanner = new Scanner(System.in);
    System.out.println("Neues Spiel gestartet!\n");
    playGame();
  }

  private static void playGame() {
    while (true) {
      dices.resetRolls();

      while (dices.getRemainingRolls() > 0) {
        System.out.println("\nNoch " + dices.getRemainingRolls() + " Würfe übrig.");
        dices.rollDices();
        displayGame();

        System.out.print("W: Würfeln  B: Würfel behalten  E: Eintragen  Q: Beenden ");
        char action = readCharacter();

        switch (Character.toLowerCase(action)) {
          case 'w':
            if (dices.getRemainingRolls() == 0) {
              System.out.println("Keine Würfe mehr übrig!");
            } else {
              rollDices();
            }
            break;
          case 'b':
            if (dices.getRemainingRolls() == 0) {
              System.out.println("Keine Würfe mehr übrig!");
            } else {
              chooseDiceToKeep();
            }
            break;
          case 'e':
            registerScoreBoard();
            break;
          case 'n':
            confirmReset();
          case 'q':
            confirmQuit();
            return;
          default:
            System.out.println("Ungültige Eingabe.");
        }
        if (dices.getRemainingRolls() == 0) {
          System.out.println(
              "Du hast alle Würfe verwendet. Du musst nun eine Entscheidung treffen.");
          break;
        }
      }
      postRollAction();
    }
  }

  private static void postRollAction() {
    boolean scoreRegistered = false;

    while (!scoreRegistered) {
      System.out.print("E: Eintragen  Q: Beenden ");
      char action = readCharacter();

      switch (Character.toLowerCase(action)) {
        case 'e':
          registerScoreBoard();
          scoreRegistered = true;
          break;

        case 'q':
          confirmQuit();
          return;

        default:
          System.out.println("Ungültige Eingabe.");
      }
    }
    calculateScores();
    displayGame();
  }

  private static void confirmReset() {
    char menu;
    System.out.print("Fortfahren? Alle Punkte gehen dabei verloren. (j/n) ");
    menu = readCharacter();
    switch (Character.toLowerCase(menu)) {
      case 'j':
        newGame();
        break;
      case 'n':
        System.out.println("Spiel fortgesetzt.");
      default:
        System.out.println("Ungültige Eingabe.");

    }
  }

  private static void confirmQuit() {
    char menu;
    System.out.print("Willst du wirklich beenden? Alle Punkte gehen dabei verloren. (y/n) ");
    menu = readCharacter();
    switch (Character.toLowerCase(menu)) {
      case 'y':
        System.exit(0);
        break;
      case 'n':
        System.out.println("Spiel fortgesetzt.");
        break;
      default:
        System.out.println("Ungültige Eingabe.");
    }
  }

  private static void calculateScores() {
    scoreboard.calculateLowerSection();
    scoreboard.calculateUpperSection();
  }

  private static void displayGame() {
    scoreboard.printWinCard();
    dices.printDicesValues();
  }

  public static void rollDices() {
    dices = new Dices();
    dices.rollDices();
  }

  public static void chooseDiceToKeep() {
    System.out.println("Zu behaltener Würfel (1-5, oder 0 um nicht mehr zu behalten): ");
    int action = readInteger();
    if (action != 0) {
      dices.keepDice(action - 1);
    }
  }

  public static int checkMenuPosition() {
    int menuPosition = -1;
    boolean validInput = false;

    do {
      System.out.println("Geben Sie eine Feldnummer von 1 bis 13 ein!");
      System.out.print("Eintragung bei: ");

      try {
        menuPosition = readInteger();
        if (menuPosition >= 1 && menuPosition <= 13) {
          validInput = true;
        } else {
          System.out.println("Ungültige Eingabe. Bitte eine Zahl zwischen 1 und 13 eingeben: ");
          readInteger();
        }
      } catch (InputMismatchException e) {
        System.out.println("Ungültige Eingabe. Bitte eine gültige Zahl von 1 bis 13 eingeben: ");
        readInteger();
      }

    } while (!validInput);

    return menuPosition;
  }

  public static boolean checkFullHouse() {
    Map<Integer, Long> frequencyMap = Arrays.stream(dices.getDicesValues())
        .boxed()
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    return frequencyMap.size() == 2 && frequencyMap.containsValue(3L) &&
        frequencyMap.containsValue(2L);
  }

  private static boolean checkDreierPasch() {
    Map<Integer, Long> frequencyMap = Arrays.stream(dices.getDicesValues())
        .boxed()
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    return frequencyMap.values().stream().anyMatch(count -> count >= 3);
  }

  private static boolean checkViererPasch() {
    Map<Integer, Long> frequencyMap = Arrays.stream(dices.getDicesValues())
        .boxed()
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    return frequencyMap.values().stream().anyMatch(count -> count >= 4);
  }

  private static boolean checkKniffel() {
    Map<Integer, Long> frequencyMap = Arrays.stream(dices.getDicesValues())
        .boxed()
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    return frequencyMap.values().stream().anyMatch(count -> count == 5);
  }

  public static boolean checkSmallStreet() {
    return hasConsecutiveSequence(4);
  }

  public static boolean checkLongStreet() {
    return hasConsecutiveSequence(5);
  }

  private static boolean hasConsecutiveSequence(int length) {

    int[] distinctValues = IntStream.of(dices.getDicesValues()).distinct().toArray();

    int consecutiveCount = 1;
    for (int i = 1; i < distinctValues.length; i++) {
      if (distinctValues[i] == distinctValues[i - 1] + 1) {
        consecutiveCount++;
        if (consecutiveCount >= length) {
          return true;
        }
      } else {
        consecutiveCount = 1;
      }
    }
    return false;
  }

  private static void registerUpperHalf(int menuPosition) {
    int pipsValue = 0;
    for (int i = 0; i < 5; i++) {
      if (dices.getDicesValues()[i] == menuPosition) {
        pipsValue += menuPosition;
      }
    }
    scoreboard.setField(menuPosition, pipsValue);

  }

  public static void registerScoreBoard() {
    if (dices.getRemainingRolls() == 3) {
      return;
    }

    int fieldPosition = checkMenuPosition();

    if (scoreboard.isFilled(fieldPosition)) {
      System.out.print(fieldPosition + " schon besetzt.");
      registerScoreBoard();
      return;
    }

    if (fieldPosition < 7) {
      registerUpperHalf(fieldPosition);
      return;
    } else {
      dices.sortDiceArray();
    }
    switch (fieldPosition) {
      case 7:
        if (checkDreierPasch()) {
          scoreboard.setField(fieldPosition, dices.sumDicesPips());
        } else {
          System.out.println("Sie haben kein Dreierpasch!");
          registerScoreBoard();
        }
        break;

      case 8:
        if (checkViererPasch()) {
          scoreboard.setField(fieldPosition, dices.sumDicesPips());
        } else {
          System.out.println("Sie haben kein Viererpasch!");
          registerScoreBoard();
        }
        break;

      case 12:
        if (checkKniffel()) {
          scoreboard.setField(fieldPosition, 50);
        } else {
          System.out.println("Sie haben kein Kniffel!");
          registerScoreBoard();
        }
        break;

      case 9:
        if (checkFullHouse()) {
          scoreboard.setField(fieldPosition, 25);
        } else {
          System.out.println("Sie haben kein Full House!");
          registerScoreBoard();
        }
        break;
      case 10:
        if (checkSmallStreet()) {
          scoreboard.setField(fieldPosition, 30);
        } else {
          System.out.println("Sie haben kein Full House!");
          registerScoreBoard();
        }
        break;
      case 11:
        if (checkLongStreet()) {
          scoreboard.setField(fieldPosition, 40);
        } else {
          System.out.println("Sie haben kein Full House!");
          registerScoreBoard();
        }
        break;
      case 13:
        scoreboard.setField(fieldPosition, dices.sumDicesPips());
        break;
      default:
        System.out.println("Ungültige Position!");
        registerScoreBoard();
    }
  }
}