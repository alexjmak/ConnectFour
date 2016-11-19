//Alex Mak

import java.util.Scanner;

public class ConnectFour {
	static String[][] board = new String[6][7];

	static Scanner console = new Scanner(System.in);

	/*
	 * main-Starts up the program
	 * @param: String[] args
	 * @return: void
	 * Starts the main menu, and when the menu is done it
	 * closes the console
	 */
	public static void main(String[] args) {
		mainMenu();
		console.close();
	}

	/*
	 * mainMenu-Console Interface menu for the program
	 * @param: n/a
	 * @return: void
	 * Uses Scanner Console Input to let the player choose
	 * the mode they want. They choose by number.
	 */
	public static void mainMenu() {
		System.out.println("Connect Four");
		System.out.println();
		System.out.println("1: 2 player game");
		System.out.println("2: 1 player game vs dumb computer");
		System.out.println("3: 1 player game vs smart computer");
		System.out.println("4: Simulated game with computer playing itself");
		System.out.println("5: Quit");
		System.out.println();

		resetBoard();
		String input;
		while (true) {
			System.out.print("Choose a mode by number: ");
			input = console.nextLine();
			if (input.equals("1")) {
				humanHumanGame();
				break; //Leaves Loop
			} else if (input.equals("2")) {
				humanComputerGame(true);
				break;
			} else if (input.equals("3")) {
				humanComputerGame(false);
				break;
			} else if (input.equals("4")) {
				computerComputerGame();
				break;
			} else if (input.equals("5")) {
				System.out.println("Goodbye");
				break;
			} else {
				System.out.println("Invalid number");
			}
		}
	}

	/*
	 * humanHumanGame-2 human player mode
	 * @param: n/a
	 * @return: void
	 * Starts a game of Connect four between 2 human players
	 * Always checks for winners every round and handles adding
	 * input to the board. Also handles the print statements to
	 * console
	 */
	public static void humanHumanGame() {
		int[] input;
		int turn = 0; //In order to find out the next player
		int player = -1; //Set to -1 because player -1 isn't a thing, but 0 is
		while (checkForWinners() == null) {
			printBoard();
			player = turn % 2; //Every other turn switches players
			System.out.println("Player " + playerToToken(player) + "'s turn!");
			input = getHumanChoice();
			if (!(input == null)) { //Valid Input
				updateBoard(input, playerToToken(player));
			} else {
				System.out.println("Invalid Choice: Lose Turn");
			}
			turn++;
		}
		if (checkForWinners() == "tie") {
			System.out.println("Tie! Game over");
		} else {
			String token = playerToToken(player);
			System.out.println("Player " + token + " wins!");
			System.out.println(checkForWinners()); //Prints location of victory
		}
		System.out.println("\nPress enter to return to the main menu");
		console.nextLine(); //Wait for enter
		mainMenu();
	}

	/*
	 * humanComputerGame-Human and (dumb/smart) computer mode
	 * @param: boolean dumb: used to set the computer's difficulty to dumb/smart
	 * @return: void
	 * Starts a game of Connect four between a human and a computer.
	 * This method is able to start a dumb computer or a smart computer game
	 * Always checks for winners every round and handles adding
	 * input to the board. Also handles the print statements to
	 * console. The board doesn't print when it's the computer's turn.
	 */
	public static void humanComputerGame(boolean dumb) {
		int[] input;
		int turn = 0;
		int player = -1;

		while (checkForWinners() == null) {
			player = turn % 2;
			printBoard();
			System.out.println("Player " + playerToToken(player) + "'s turn!");
			if (player == 0) {
				input = getHumanChoice();
			} else { // Computer always goes second
				if (dumb) {
					input = getDumbComputerChoice();
				} else {
					input = getSmartComputerChoice(1); //Get smart computer's decision as player 1
					System.out.println("Player " + playerToToken(player) + " picked column " + input[1]);
				}
			}
			if (!(input == null)) { //Valid Input
				updateBoard(input, playerToToken(player));
			} else {
				System.out.println("Invalid Choice: Lose Turn");
			}
			turn++;
		}
		printBoard();
		if (checkForWinners() == "tie") {
			System.out.println("Tie! Game over");
		} else {
			String token = playerToToken(player);
			System.out.println("Player " + token + " wins!");
			System.out.println(checkForWinners());
			if (player == 0) { //If human wins
				System.out.println("Player " + playerToToken((player + 1) % 2) + ": " + getSmartComputerTaunt(4)); //Loser remark
			}
		}
		System.out.println("\nPress enter to return to the main menu");
		console.nextLine(); //Wait for enter
		mainMenu();
	}

	/*
	 * computerComputerGame-dumb and smart computer game
	 * @param: n/a
	 * @return: void
	 * Starts a game of Connect four between a dumb computer and a smart computer.
	 * Always checks for winners every round and handles adding
	 * input to the board. Also handles the print statements to
	 * console. The board prints every round in this game. The smart computer
	 * wins every round.
	 */
	public static void computerComputerGame() {
		int[] input;
		int turn = 0;
		int player = -1;
		while (checkForWinners() == null) {
			player = turn % 2;
			printBoard();
			System.out.println("Player " + playerToToken(player) + "'s turn!");
			if (player == 0) {
				input = getDumbComputerChoice();
			} else {
				input = getSmartComputerChoice(1);
			}
			System.out.println("Player " + playerToToken(player) + " picked (" + input[0] + ", " + input[1] + ")");
			updateBoard(input, playerToToken(player));
			turn++;
		}
		printBoard();
		if (checkForWinners() == "tie") {
			System.out.println("Tie! Game over");
		} else {
			String token = playerToToken(player);
			System.out.println("Player " + token + " wins!");
			System.out.println(checkForWinners());
			System.out.println("Player " + playerToToken((player + 1) % 2) + ": " + getSmartComputerTaunt(4)); //Loser remark
		}
		System.out.println("\nPress enter to return to the main menu");
		console.nextLine(); //Wait for enter
		mainMenu();
	}

	/*
	 * getHumanChoice-Get input from a human player
	 * @param: n/a
	 * @return: int[] coordinates
	 * Gets user input asking for which column the player wants to
	 * place a token in. Makes sure the input is actually a number.
	 * Looks at all the available spots and then finds an
	 * available spot that matches the column inputed and then
	 * makes sure it is a valid move (ex. not floating)
	 * Returns null if move is invalid, and a coordinate list
	 * {x, y} if it's a valid move
	 */
	public static int[] getHumanChoice() {
		System.out.print("Enter column: ");
		String input = console.nextLine();

		int row = -1; //This won't be redefined if it can't find any match in
		//available coordinates, so it will trigger coordinatesValid()
		//to return null
		int column;
		try { //Make sure we have an integer
			column = Integer.parseInt(input.trim());
		} catch (NumberFormatException e) {
			return null; //If not return null (invalid move)
		}

		for (int[] availableCoordinates : getAvailableSpots()) {
			if (availableCoordinates[1] == column) {
				row = availableCoordinates[0];
				break; //Get out of the loop
			}
		}

		int[] coordinates = {row, column};
		if (coordinatesValid(coordinates)) {
			return coordinates;
		}
		return null;
	}

	/*
	 * getDumbComputerChoice-Get input from a dumb computer
	 * @param: n/a
	 * @return: int[] input
	 * Gets a list of the available spots in the board and then
	 * randomly chooses a spot to place its token in.
	 */
	public static int[] getDumbComputerChoice() {
		int[][] availableSpots = getAvailableSpots();
		//Random number from 0 (inclusive) - amnt. of available spots (exclusive)
		int randomIndex = (int) (Math.random() * availableSpots.length);
		int[] input = availableSpots[randomIndex];
		return input;
	}

	/*
	 * getSmartComputerTaunt-Get a taunt from a computer
	 * @param: int type: the row number for the taunt nested array
	 * @return: String taunt: Something a smart computer would say
	 * After receiving a type (row number) in the parameter, the method then chooses a random taunt
	 * in that row and then returns it.
	 */
	public static String getSmartComputerTaunt(int type) {
		String[][] computerTaunt = {{"Well someone here needs better glasses...", "How did you not see that???", "Yay I win. Add that to the list of wins...",
										"That was only a warmup", "That's all you got?", "I love this game"},
									{"Don't even try", "I could see that comming 4 rounds ago", "Yikes", "Oooh I see it", "Hahaha", "Keep thinking"},						   
									{"Nice move... not", "Hmm...", "I see that comming...", "You're moves don't trick me!", "I can read your mind",
										"Wow that was a great move! Oh wait I was talking to myself", "Is that all you got?"},
									{"Hmmm...", "I know what you're up to", "Honestly, you can press the quit button right now", "Fun huh?",
										"Next round hurry up you're so slow"},
									{"I want a rematch!", "Thats impossible...", "Oh no you didn't!", "Ruddeeee", "I'll get you next time!", "Cheater! :(",
										"NO!!!", "Welp", "I see how it is..."}};

		int typeLength = computerTaunt[type].length;
		int randomTauntIndex = (int) (Math.random() * typeLength); //Random number from 0 (inclusive) -  length of row (exclusive)
		String taunt = computerTaunt[type][randomTauntIndex];
		return taunt;
	}
	/*
	 * getSmartComputerChoice-Get input from a smart computer
	 * @param: int player: not really necessary unless I want to have a smart computer
	 * 					  vs. smart computer game
	 * @return: int[] input
	 * Computes if the computer can win the next round or player can win next round
	 * Also tries to avoid other tricks like with 2 way wins
	 * Always double checks to make sure that its move won't let the next player win
	 * If smart computer isn't about to win/lose, it will either put its tokens near
	 * its own tokens or put tokens near the opponent
	 */
	public static int[] getSmartComputerChoice(int player) {
		int[] input;
		int[] canComputerWin = canPlayerWin(player);
		int[] canOtherPlayerWin = canPlayerWin((player + 1) % 2); //Other player
		int[] canOtherPlayerWin2Way = canPlayerWin2Way((player + 1) % 2);
		int[] canComputerWin2Way = canPlayerWin2Way(player);
		if (!(canComputerWin == null)) { //Computer can win next
			input = canComputerWin;
			System.out.println("Player " + playerToToken(player) + ": " + getSmartComputerTaunt(0));
		} else if (!(canOtherPlayerWin == null)) {
			input = canOtherPlayerWin;
			System.out.println("Player " + playerToToken(player) + ": " + getSmartComputerTaunt(1));
		} else if (!(canOtherPlayerWin2Way == null)) {
			input = canOtherPlayerWin2Way;
			if (!(check2InputOK(input, player))) { //Double checks it wont be vulnerable next round
				input = smartPlacement(player, player);
			}
			System.out.println("Player " + playerToToken(player) + ": " + getSmartComputerTaunt(2));
		} else if (!(canComputerWin2Way == null)) {
			input = canComputerWin2Way;
			if (!(check2InputOK(input, player))) {
				input = smartPlacement(player, player);
			}
			System.out.println("Player " + playerToToken(player) + ": " + getSmartComputerTaunt(3));
		} else {
			double defenseOrOffense = Math.random();
			if (defenseOrOffense > 0.75) {
				input = smartPlacement(player, player); //Puts tokens near itself (offense)
			} else {
				input = smartPlacement(player, (player + 1) % 2); //Puts tokens near opponent's tokens (defense)
			}
			System.out.println("Player " + playerToToken(player) + ": " + getSmartComputerTaunt(3));
		}
		return input;
	}

	/*
	 * smartPlacement-Picks a good spot for the smart computer to place a token in if
	 * 				 computer isn't going to win/lose a
	 * @param: int player: the current player, int placeNearPlayer: which player the current player
	 * 															   should be near
	 * @return: int[] input
	 * Looks through all the available spots in the board and then gets the frequency of a token (placeNearPlayer's token)
	 * Puts the player's token near the highest frequency of placeNearPlayer's token
	 * Double checks that this move won't endanger the computer
	 */
	public static int[] smartPlacement(int player, int placeNearPlayer) {
		int[] input;
		int[][] availableSpots = getAvailableSpots();
		int[] availableSpotPoints = new int[availableSpots.length]; //Make a list of all the frequencies
		//near an available spot
		//of placeNearPlayer
		for (int i = 0; i < availableSpots.length; i++) {
			int[] coordinates = availableSpots[i];
			int[][] cornerCoordinates = getCornerCoordinates(coordinates); //List of nearby coordinates
			for (int[] bCoordinates : cornerCoordinates) {
				if (bCoordinates == null) { // If nearby coordinate is invalid
					continue; //Go to the next loop
				}
				int row = bCoordinates[0];
				int column = bCoordinates[1];
				if (board[row][column] == playerToToken(placeNearPlayer)) {
					availableSpotPoints[i]++; //Add 1 to the frequency
				}
			}
		}
		int maxIndex = 0;
		int secondMaxIndex = 0; //If the first choice will endanger the computer
		int thirdMaxIndex = 0; //Just in case
		for (int i = 0; i < availableSpots.length; i++) {
			int maxNumber = Math.max(availableSpotPoints[maxIndex], availableSpotPoints[i]);
			if (maxNumber == availableSpotPoints[i]) {
				thirdMaxIndex = secondMaxIndex;
				secondMaxIndex = maxIndex;
				maxIndex = i;
			}
		}
		input = availableSpots[maxIndex];

		if (!(check2InputOK(input, player))) { //Checks if the input is stupid or not
			input = availableSpots[secondMaxIndex];
			if (!(check2InputOK(input, player))) { //Just in case
				input = availableSpots[thirdMaxIndex];
			}
		}
		return input;
	}

	/*
	 * check2InputOK-A double check function that checks if an input will not make the player lose the next round
	 * @param: int[] input: the coordinate input the smart computer wants to do, int player: the player to double
	 * 																						check against
	 * @return: boolean true: input is safe, boolean false: input is a bad move and next player can win next round
	 * Puts the input on the board and checks if the next player can win, if so return false.
	 * If next player can't win then return true.
	 */
	public static boolean check2InputOK(int[] input, int player) {
		updateBoard(input, playerToToken(player)); //Put input on board
		if (!(canPlayerWin((player + 1) % 2) == null)) { //Other player
			updateBoard(input, "."); //Make sure we clear the move
			return false; //Input not OK
		}
		updateBoard(input, ".");
		return true; //Input OK
	}

	/*
	 * getCornerCoordinates-Get surrounding coordinates of a coordinate
	 * @param: int[] coordinates: the coordinate to check around
	 * @return: int[][] boundaries: an array of surrounding coordinates
	 * Based on a coordinate, it gets the left, right, bottom, bottom left,
	 * bottom right, top, top left, and top right coordinates and
	 * puts it into an array. Sets a coordinate to null if it's invalid.
	 * (ex. {6, 0})
	 */
	public static int[][] getCornerCoordinates(int[] coordinates) {
		int row = coordinates[0];
		int column = coordinates[1];
		int[][] boundaries = new int[8][2];
		boundaries[0][0] = row;
		boundaries[0][1] = column - 1; //left
		boundaries[1][0] = row;
		boundaries[1][1] = column + 1; //right
		boundaries[2][0] = row + 1; 
		boundaries[2][1] = column;	   //bottom
		boundaries[3][0] = row + 1;
		boundaries[3][1] = column - 1; //bottom left
		boundaries[4][0] = row + 1;
		boundaries[4][1] = column + 1; //bottom right
		boundaries[5][0] = row - 1; 
		boundaries[5][1] = column;	   //top
		boundaries[6][0] = row - 1;
		boundaries[6][1] = column - 1; //top left
		boundaries[7][0] = row - 1;
		boundaries[7][1] = column + 1; //top right
		int[] bCoordinate;
		for (int i = 0; i < boundaries.length; i++) { //Checks coordinate for validity
			bCoordinate = boundaries[i];
			row = bCoordinate[0];
			column = bCoordinate[1];
			if (!(row >= 0 && column >= 0 && row < board.length && column < board[0].length)) {
				boundaries[i] = null;
			}
		}
		return boundaries;
	}

	/*
	 * canPlayerWin-Checks if the player can win next
	 * @param: int player: the player to check against
	 * @return: int[] coordinates: if the player is able to win the next round return that coordinate
	 * 								that will let the player win
	 * Puts a token in all available spots and checks for a winner
	 * Then, it removes the temporary token and returns the coordinate that
	 * will make the player win
	 */
	public static int[] canPlayerWin(int player) {
		String token = playerToToken(player);
		int[][] availableSpots = getAvailableSpots();
		for (int[] coordinates : availableSpots) { //We don't know where a player
			//is going to place a token, so
			//were going to put a token in every situation 
			updateBoard(coordinates, token);
			if (!(checkForWinners(player) == null)) { //Then checks for a possible win by the player
				updateBoard(coordinates, "."); //Make sure to clear the token
				return coordinates;
			}
			updateBoard(coordinates, ".");
		}
		return null;
	}

	/*
	 * canPlayerWin2Way-Checks if a player can win in two rounds
	 * @param: int player: the player to check for
	 * @return: int[] coordinates: if a player is able to win in two rounds, return the coordinate
	 * 							  that will cause it to win in two rounds
	 * Same algorithm as one way except that instead of checking for winners it will call the
	 * can player win method, thus making it a double 2 way check 
	 */
	public static int[] canPlayerWin2Way(int player) {
		String token = playerToToken(player);

		int[][] availableSpots = getAvailableSpots();
		for (int[] coordinates : availableSpots) {
			updateBoard(coordinates, token);
			if (!(canPlayerWin(player) == null)) { //The only difference from canPlayerWin()
				updateBoard(coordinates, ".");
				return coordinates;
			}
			updateBoard(coordinates, ".");
		}
		return null;
	}

	/*
	 * getAvailableSpots-Gets all valid spots/coordinates one can place a token in
	 * @param: n/a
	 * @return: int[][] availableSpots: the array of available coordinates
	 * Goes thought a loop of the board and counts all valid coordinates for the array
	 * Then goes through the same loop again after an array with the count has been
	 * created. Then, add these valid coordinates to the array
	 */
	public static int[][] getAvailableSpots() {
		int availableSpotsCount = 0; //Count for array
		for (int row = 0; row < board.length; row++) {
			for (int column = 0; column < board[row].length; column++) {
				int[] coordinates = {row, column};
				if (coordinatesValid(coordinates)) {
					availableSpotsCount++; //Add to count
				}
			}
		}
		int i = 0; //For the availableSpots array
		int[][] avaliableSpots = new int[availableSpotsCount][2]; //Use count for array
		for (int row = 0; row < board.length; row++) { //Same loop
			for (int column = 0; column < board[row].length; column++) {
				int[] coordinates = {row, column};
				if (coordinatesValid(coordinates)) {
					avaliableSpots[i] = coordinates; //Add to array
					i++;
				}
			}
		}
		return avaliableSpots;
	}

	/*
	 * printBoard-Prints the board
	 * @param: n/a
	 * @return: void
	 * Prints the board, formatting with spaces between values
	 * and printing a new line after each row
	 */
	public static void printBoard() {
		System.out.println("0 1 2 3 4 5 6");
		for (String row[] : board) {
			for (String value : row) {
				System.out.print(value + " ");
			}
			System.out.println();
		}
	}

	/*
	 * resetBoard-Clears the board
	 * @param: n/a
	 * @return: void
	 * Traverse through the board and set it to "." (empty)
	 */
	public static void resetBoard() {
		for (int row = 0; row < board.length; row++) {
			for (int column = 0; column < board[row].length; column++) {
				board[row][column] = ".";
			}
		}
	}

	/*
	 * coordinatesValid-If coordinate isn't already occupied and inside the bounds
	 * @param: int[] coordinates: the coordinate to check
	 * @return: boolean true: valid, boolean false: not valid
	 * Gets the row and column and then checks within the the bounds of the 
	 * array length. Bottom row will always work as long as it's not taken
	 */
	public static boolean coordinatesValid(int[] coordinates) {
		int row = coordinates[0];
		int column = coordinates[1];
		if (row >= 0 && row < board.length) {
			if (column >= 0 && column < board[0].length) {
				if (board[row][column].equals(".")) {
					if (row == board.length-1) {
						return true;
					} else if (!(board[row+1][column].equals("."))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/*
	 * checkForWinnersHorizontal-Checks for a horizontal win
	 * @param: String token: token (R or B) to check for a win
	 * @return: String location of win or null if no win 
	 * All possible horizontal wins have their first token in
	 * the first half columns, so we only check there and then
	 * since we're going left to right with the first token
	 * in the first half, their wouldn't be any OutOfBounds errors
	 * if we add 0, 1, 2, 3, 4 to the columns, so that's what this does. If it checks
	 * that its in a row with a counter, then it will return a win.
	 */
	public static String checkForWinnersHorizontal(String token) {
		int count;
		for (int row = 0; row < board.length; row++) {
			for (int column = 0; column < board[row].length; column++) {
				count = 0;
				if (board[row][column].equals(token)) {
					if (column <= board[row].length/2) { //All horizontal wins have first token in first half of columns
						for (int i = 0; i < 4; i++) {
							if (board[row][column + i].equals(token)) { //Check location to the right if equals same token
								count++;
								if (count == 4) {
									return "(" + row + "," + column + ") horizontal"; //Location of win
								} 
							} else {
								count = 0;
							}
						}
					}
				}
			}
		}
		return null;
	}

	/*
	 * checkForWinnersVertical-Checks for a vertical win
	 * @param: String token: token (R or B) to check for a win
	 * @return: String location of win or null if no win 
	 * All possible vertical wins have their first token in
	 * the first half rows, so we only check there and then
	 * since we're going left to right with the first token
	 * in the first half, there wouldn't be any OutOfBounds errors
	 * if we add 0, 1, 2, 3, 4 to the rows, so that's what this does. If it checks
	 * that its in a row with a counter, then it will return a win.
	 */
	public static String checkForWinnersVertical(String token) {
		int count;
		for (int row = 0; row < board.length; row++) {
			for (int column = 0; column < board[row].length; column++) {
				count = 0;
				if (board[row][column].equals(token)) {
					if (row < board.length/2) { //All vertical wins have first token in upper half
						for (int i = 0; i < 4; i++) {
							if (board[row + i][column].equals(token)) { //Check location below if equals same token
								count++;
								if (count == 4) {
									return "(" + row + "," + column + ") vertical"; //Location of win
								} 
							} else {
								count = 0;
							}
						}
					}
				}
			}
		}
		return null;
	}

	/*
	 * checkForWinnersDiagonal-Checks for a diagonal win
	 * @param: String token: token (R or B) to check for a win
	 * @return: String location of win or null if no win 
	 * All possible diagonal wins have their first token in the upper half
	 * rows. It also is either in the first half columns for diagonal right
	 * wins or in the last half columns for the diagonal left columns.
	 * Since there are different if methods for the first and last columns
	 * their wouldn't be any OutOfBounds errors
	 * if we add/subtract 0, 1, 2, 3, 4 to the rows and columns, so that's what this does. If it checks
	 * that its in a row with a counter, then it will return a win.
	 */
	public static String checkForWinnersDiagonal(String token) {
		int count;
		for (int row = 0; row < board.length; row++) {
			for (int column = 0; column < board[row].length; column++) {
				if (board[row][column].equals(token)) {
					if (row < board.length/2) { //All diagonal wins have first token in upper half
						count = 0;
						if (column <= board[row].length/2) { //Diagonal right wins on the first half of columns
							for (int i = 0; i < 4; i++) {
								if (board[row + i][column + i].equals(token)) { //Checks location to the bottom right if equals same token
									count++;
									if (count == 4) {
										return "(" + row + "," + column + ") diagonal right"; //Location of win
									}
								} else {
									count = 0;
								}
							}
						}
						count = 0;
						if (column >= board[row].length/2) { //Diagonal left wins on the last half of columns
							for (int i = 0; i < 4; i++) {
								if (board[row + i][column - i].equals(token)) { //Add the i to the rows and subtract i from columns
									count++;
									if (count == 4) {
										return "(" + row + "," + column + ") diagonal left"; //Location of win
									}
								} else {
									count = 0;
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

	/*
	 * checkForWinners-Checks for a win in all directions and all players
	 * @param: n/a
	 * @return: String winner: if win detected, null: no winner, String "tie": no winners/losers
	 * Checks for a tie first by seeing if all spots are taken. If so, declare a tie.
	 * Checks player 0 and checks player 1, returning right after a win is detected
	 */
	public static String checkForWinners() {
		if (getAvailableSpots().length == 0) { //Check for the board full/tie game
			return "tie";
		}
		String winner;
		winner = checkForWinners(0); //Check for player 0
		if (!(winner == null)) { //Return instantly if player found
			return winner;
		}
		winner = checkForWinners(1); //Check for player 1
		if (!(winner == null)) {
			return winner;
		}
		return null;
	}

	/*
	 * checkForWinners-Checks for a win in all directions and all players
	 * @param: int player: player to check win
	 * @return: String winner: if win detected, null: no winner
	 * Checks for horizontal, vertical, and diagonal wins by the the token
	 * But first, it converts the player number to its token. Returns right
	 * after a win is detected.
	 */
	public static String checkForWinners(int player) {
		String token = playerToToken(player);

		String winner;
		winner = checkForWinnersHorizontal(token);
		if (!(winner == null)) { //Return instantly if player found
			return winner;
		}
		winner = checkForWinnersVertical(token);
		if (!(winner == null)) {
			return winner;
		}
		winner = checkForWinnersDiagonal(token);
		if (!(winner == null)) {
			return winner;
		}
		return null;
	}

	/*
	 * updateBoard-Add a coordinate to a board with a given coordinate and the token
	 * @param: int[] coordinate: the place to add a token, String token: the token to add
	 * @return: void
	 * Separates the coordinate into two numbers: row and column then adds the token to the board
	 * by those numbers
	 */
	public static void updateBoard(int[] coordinate, String token) {
		int row = coordinate[0];
		int column = coordinate[1];
		board[row][column] = token;
	}

	/*
	 * playerToToken-Converts player number to a token
	 * @param: int player: the player to convert to
	 * @return: String token: the token based on the player number
	 * If player is 0, then token is "R"
	 * If player is 1, then token is "B"
	 * If invalid player, it returns null, else it returns the token.
	 */
	public static String playerToToken(int player) {
		String token;
		if (player == 0) {
			token = "R";
		} else if (player == 1) {
			token = "B";
		} else {
			token = null;
		}
		return token;
	}
}