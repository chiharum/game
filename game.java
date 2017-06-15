import java.io.*;

import java.util.Random;

public class game{

	static int rows;
	static boolean finishGame;
	static int[] possibility;
	static boolean[] hasPlayerGot;
	static boolean[] hasComputerGot;

	static InputStreamReader inputStreamReader;
	static BufferedReader bufferedReader;
	
	public static void main(String[] args) throws IOException{

		inputStreamReader = new InputStreamReader(System.in);
		bufferedReader = new BufferedReader(inputStreamReader);
		
		rows = 3;
		boolean isPlayerFirstAttacker = true;

		hasPlayerGot = new boolean[rows * rows];
		hasComputerGot = new boolean[rows * rows];
		// 1つ目から、(0,0)(0,1)…(rows,rows)

		for(int a = 0; a < rows; a++){
			hasPlayerGot[a] = false;
			hasComputerGot[a] = false;
		}

		possibility = new int[rows * rows];

		System.out.println("yes or no");
		String string = bufferedReader.readLine();

		System.out.println(getStatus());

		if (string.equals("yes")) {
			isPlayerFirstAttacker = true;
			requireAttack();
		}else if (string.equals("no")) {
			isPlayerFirstAttacker = false;
			decide();
		}else{
			System.out.println("restart");
		}
		System.out.println(getStatus());

		if(isPlayerFirstAttacker){
			while(!finishGame){
				if(!finishGame){
					decide();
					System.out.println(getStatus());
					finishGame = check();
					if(!finishGame){
						requireAttack();
						System.out.println(getStatus());
						finishGame = check();
					}
				}
			}
		}else{
			while(!finishGame){
				if(!finishGame){
					requireAttack();
					System.out.println(getStatus());
					finishGame = check();
					if(!finishGame){
						decide();
						System.out.println(getStatus());
						finishGame = check();
					}
				}
			}
		}
	}

	public static String getStatus (){

		String result = null;
		boolean isFirst = true;

		for (int x = 0; x < rows; x++){
			for (int y = 0; y < rows; y++) {

				int numberOfTexts = String.valueOf(rows * rows).length() + 1;

				if(hasPlayerGot[rows * x + y]){

					if(isFirst){
						result = "○";
						isFirst = false;
					}else{
						result = result + "○";//まる
					}

					for (int a = 0; a < (numberOfTexts - 1); a++) {
						result = result + " ";
					}

				} else if (hasComputerGot[rows * x + y]){

					if(isFirst){
						result = "×";
						isFirst = false;
					}else{
						result = result + "×";//ばつ
					}

					for (int a = 0; a < (numberOfTexts - 1); a++) {
						result = result + " ";
					}

				} else {

					int number = rows * x + y;

					if(isFirst){
						result = String.valueOf(number);//xを代入
						isFirst = false;
					}else{
						result = result + String.valueOf(number);//xを代入
					}

					int spaces = numberOfTexts - String.valueOf(number).length();
					for (int a = 0; a < spaces; a++) {
						result = result + " ";
					}
				}

				if (y == rows - 1 && x != rows - 1){
					result = result + "\n";
				}
			}
		}

		return result;
	}

	public static void decide(){

		System.out.println("Computer's turn");
		
		int oneComputerHasToTake = checkIfPlayerWillWin(0);

		if (oneComputerHasToTake != rows * rows) {
			hasComputerGot[oneComputerHasToTake] = true;
			System.out.println("Computer took " + oneComputerHasToTake + ".(1)");
		} else {
			oneComputerHasToTake = checkIfPlayerWillWin(1);

			if (oneComputerHasToTake != rows * rows) {
				hasComputerGot[oneComputerHasToTake] = true;
				System.out.println("Computer took " + oneComputerHasToTake + ".(2)");
			} else {
				//possibility計算
				int highest = 0;
				int theNumber = rows * rows;
				checkIfPlayerWillWin(3);
				for (int i = 0; i < rows * rows; i++) {
					if(highest < possibility[i]){
						possibility[i] = highest;
						theNumber = i;
					}
				}
				if(highest != 0){
					hasComputerGot[theNumber] = true;
					System.out.println("Computer took " + theNumber + ".(3)");
				}else{
					Random random = new Random();
					int number = random.nextInt(rows * rows);
					while(hasComputerGot[number] || hasPlayerGot[number]){
						number = random.nextInt(rows * rows);
					}
					hasComputerGot[number] = true;
					System.out.println("Computer took " + number + ".(4)");
				}
			}
		}
	}

	public static int checkIfPlayerWillWin(int type){

		//type=0ならcheckIfPlayerWillWin, type=1ならcheckIfThereIsChance, type=2ならcalculatePossibility

		boolean isCheckingLines = true;

		int oneComputerHasToTake = rows * rows;

		possibility = new int[rows * rows];

		for(int i = 0; i < 2; i++){

			for (int a = 0; a < rows; a++) {

				int numberOfPlayers = 0;
				int numberOfComputers = 0;
				int theNumber = rows;

				possibility = new int[rows * rows];
			
				for (int b = 0; b < rows; b++) {

					int number;

					if(isCheckingLines){
						number = a * rows + b;
					}else{
						number = a + b * rows;
					}

					if (hasPlayerGot[number]) {
						numberOfPlayers++;
					}else if(hasComputerGot[number]){
						numberOfComputers++;
					}else{
						theNumber = number;
					}
				}

				if (type == 0) {
					if (numberOfPlayers == rows - 1 && numberOfComputers == 0) {
						oneComputerHasToTake = theNumber;
					}
				}else if (type == 1) {
					if (numberOfComputers == rows - 1 && numberOfPlayers == 0) {
						oneComputerHasToTake = theNumber;
					}
				}else if (type == 2) {
					if(numberOfPlayers == 0){
						for (int c = 0; c < rows; c++) {
							if (isCheckingLines) {
								possibility[a * rows + c] = possibility[a * rows + c] + 1;
							}else {
								possibility[a + c * rows] = possibility[a + c * rows] + 1;
							}
						}
					}
				}
			}

			isCheckingLines = false;
		}

		for (int i = 0; i < 2; i++) {

			int numberOfPlayers = 0;
			int numberOfComputers = 0;
			int theNumber = rows;

			if(i == 0){
				
				for (int a = 0; a < rows; a++) {

					int number = (rows + 1) * a;
				
					if (hasPlayerGot[number]) {
						numberOfPlayers++;
					}else if(hasComputerGot[number]){
						numberOfComputers++;
					}else{
						theNumber = number;
					}
				}

				if (type == 0) {
					if (numberOfPlayers == rows - 1 && numberOfComputers == 0) {
						oneComputerHasToTake = theNumber;
					}
				}else if (type == 1) {
					if (numberOfComputers == rows - 1 && numberOfPlayers == 0) {
						oneComputerHasToTake = theNumber;
					}
				}else if (type == 3) {
					if(numberOfPlayers == 0){
						for (int c = 0; c < rows; c++) {
							possibility[(rows + 1) * c] = possibility[(rows + 1) * c] + 1;
						}
					}
				}

			}else{

				for (int a = 0; a < rows; a++) {
					
					int number = (rows - 1) * (a + 1);

					if (hasPlayerGot[number]) {
						numberOfPlayers++;
					}else if(hasComputerGot[number]){
						numberOfComputers++;
					}else{
						theNumber = number;
					}
				}

				if (type == 0) {
					if (numberOfPlayers == rows - 1 && numberOfComputers == 0) {
						oneComputerHasToTake = theNumber;
					}
				}else if (type == 1) {
					if (numberOfComputers == rows - 1 && numberOfPlayers == 0) {
						oneComputerHasToTake = theNumber;
					}
				}else if (type == 2) {
					if(numberOfPlayers == 0){
						for (int c = 0; c < rows; c++) {
							possibility[(rows - 1) * (c + 1)] = possibility[(rows - 1) * (c + 1)] + 1;
						}
					}
				}
			}
		}

		return oneComputerHasToTake;
	}

	public static boolean check(){

		//どちらかが勝った場合に勝った方を出力

		boolean result = false;

		boolean isCheckingLines = true;

		for (int i = 0; i < 2; i++) {
			
			for (int a = 0; a < rows; a++) {

				int numberOfPlayers = 0;
				int numberOfComputers = 0;
				
				for (int b = 0; b < rows; b++) {

					int number;

					if(isCheckingLines){
						number = a * rows + b;
					}else{
						number = a + b * rows;
					}

					if (hasPlayerGot[number]) {
						numberOfPlayers++;
					}else if(hasComputerGot[number]){
						numberOfComputers++;
					}
				}

				if (numberOfPlayers == rows) {
					System.out.println("You won.");
					result = true;
				}else if(numberOfComputers == rows){
					System.out.println("Computer won.");
					result = true;
				}
			}

			isCheckingLines = false;
		}

		//ななめ

		for (int i = 0; i < 2; i++) {

			int numberOfPlayers = 0;
			int numberOfComputers = 0;

			if(i == 0){
				
				for (int a = 0; a < rows; a++) {

					int number = (rows + 1) * a;
				
					if (hasPlayerGot[number]) {
						numberOfPlayers++;
					}else if(hasComputerGot[number]){
						numberOfComputers++;
					}
				}

				if (numberOfPlayers == rows) {
					System.out.println("You won.");
					result = true;
				}else if(numberOfComputers == rows){
					System.out.println("Computer won.");
					result = true;
				}
			}else{

				for (int a = 0; a < rows; a++) {
					
					int number = (rows - 1) * (a + 1);

					if (hasPlayerGot[number]) {
						numberOfPlayers++;
					}else if(hasComputerGot[number]){
						numberOfComputers++;
					}
				}

				if (numberOfPlayers == rows) {
					System.out.println("You won.");
					result = true;
				}else if(numberOfComputers == rows){
					System.out.println("Computer won.");
					result = true;
				}
			}
		}

		return result;
	}

	public static void requireAttack () throws IOException{

		System.out.println("Your turn. Choose a number.");

		String string = bufferedReader.readLine();
		int input = Integer.parseInt(string);

		if (hasPlayerGot[input] || hasComputerGot[input]) {
			System.out.println("False, Choose again.");
			requireAttack();
		}else{
			hasPlayerGot[input] = true;
		}
	}
}