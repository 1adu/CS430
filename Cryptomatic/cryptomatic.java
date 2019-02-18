// Alymbek Damir Uulu 

import java.util.HashMap;
import java.util.Scanner;
import java.io.*;

public class cryptomatic {
	public static void main(String args[]) throws IOException {
		HashMap<String, Integer> map = new HashMap<>();			// HashMap to keep Dictionary words
		Scanner file = new Scanner(new File("Dictionary.txt")); // Scanner for words.txt
		while (file.hasNext()) {	
			map.put(file.next().trim().toLowerCase(), 1);		// Insert words from dictionary into out HashMap
		}
		Scanner reader = new Scanner(System.in);				// New Scanner for user input
		String fileName = "";
		String key = "";
		String choice;
		String saveFile;
		String text;
		int i, j;
		String keyBF;

		while (true) {											// Menu 
			System.out.println("\nCryptomatic\n");
			System.out.print("a.) Encrypt/Decrypt: \n");
			System.out.print("b.) Brute force: \n");
			System.out.print("c.) Exit\n");
			System.out.print("\nEnter Your Menu Choice: ");
			choice = reader.next();
			switch (choice) {
			case "a":
				System.out.print("Please Enter The File Name: ");
				fileName = reader.next();
				System.out.print("Please Enter The two character key to ecnrypt/decrypt: ");
				key = reader.next();
				text = dencrypt(openFile(fileName), key);		//
				System.out.println("Enter a file name to save: ");
				saveFile = reader.next();
				if (!saveFile.endsWith(".txt"))					// If the user does not include .txt extension
					saveFile += ".txt";
				File fileToSave = new File(saveFile);
				while (fileToSave.exists()) {
					System.out.println("This file already exists. Enter a new file name: "); // We ask for another file name.
					saveFile = reader.next();
					if (!saveFile.endsWith(".txt"))
						saveFile += ".txt";
					fileToSave = new File(saveFile);
				}
				PrintWriter writer = new PrintWriter(new FileWriter(fileToSave));  // Save file.
				writer.println(text);
				writer.close();
				break;
			case "b":
				//BRUTE FORCE
				String decryptedText = "";
				String myText = "", finalText = "", finalKey = "";
				String[] splited;
				int keyMatches = 0, maxMatches = 0;
				System.out.print("Please Enter The File Name to brute force: ");
				fileName = reader.next();
				myText = openFile(fileName); 				// Opens the file.
				keyBF = "  ";
				for (i = 32; i < 127; i++)					
					for (j = 32; j < 127; j++) {			// Nested loop that applies each character from ASCII 32 - 126.
						keyMatches = 0;
						/* It was initially giving me two numbers however adding "" solved the issue */
						keyBF = (char) i + "" + (char) j;	// Save both character into a string
						if (Character.isLetter((char) (myText.charAt(0) ^ (char) i))) {  // We use XOR operator to see if the first character is a letter. If not we skip the rest.
							decryptedText = dencrypt(myText, keyBF);					 // If the first character is a letter we decrypt the whole text. 
							splited = decryptedText.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+"); // We separate words within the text and save them into an array. 
							for (int k = 0; k < splited.length; k++) {	
								if (map.containsKey(splited[k])) {		// We iterate through the words and each time our HashMap has the work we add +1 to our counter.
									keyMatches++;
								}
							}
							if (keyMatches > maxMatches) {   // If statement that compares number of key matches. 
								maxMatches = keyMatches;	 //  
								finalText = decryptedText;   // Stores the text itself with maximum word matches. 
								finalKey = keyBF;			 // Stores the key if we find the text with maximum word matches. 
							}
						}
					}
				System.out.println("The key is: " + finalKey + "\nThe encrypted file is saved as decryptedText.txt");
				
				/*Saving decrypted file text as a file*/
				fileToSave = new File("decryptedText.txt");
				writer = new PrintWriter(new FileWriter(fileToSave));  
				writer.println(finalText);
				writer.close();

				break;
			case "c":
				System.out.println("Exiting Program...");
				System.exit(0);
				break;
			default:
				System.out.println("This is not a valid Menu Option! Please Select Another");
				break;
			}
		}
	}

	private static String openFile(String fileName) throws FileNotFoundException { //Reads a file and returns it as a String 
		File f = new File(fileName);
		return new Scanner(f).useDelimiter("\\|").next(); 
	}

	private static String dencrypt(String text, String key) throws FileNotFoundException { //Reads encrypted/decrypted text and key.
		String returnBack = "";
		for (int i = 0; i < text.length(); i++) {							 
			char a = text.charAt(i);
			char b = key.charAt(i % 2);
			
			/* XOR operator returns a number thus we have to convert it to a character */
			returnBack += Character.toString((char)(a ^ b)); // Loop that applies XOR algorithm to each character.
		}
		return returnBack;
	}

}
