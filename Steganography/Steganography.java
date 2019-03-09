import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Steganography {

	public static void main(String[] args) throws IOException {

		Scanner in = new Scanner(System.in);

		/*
		 *  User Option Menu to embed/extract hidden message within out WAV file.
		 */
		while (true) { // Menu
			System.out.println("\nSteganography\n");
			System.out.print("a.) Encode file in wav: \n");
			System.out.print("b.) Decode \n");
			System.out.print("c.) Exit\n");
			System.out.print("\nEnter Your Menu Choice: ");
			String choice = in.next();
			switch (choice) {
			case "a":
				System.out.println("Please enter the filename of wav file: ");
				String filename = in.next();
				System.out.println("Please enter the filename of the file that you want to hide: ");
				String filenameHide = in.next();
				System.out.println("Please enter the filename for WAV with hidden message");
				String newFileNameHide = in.next();
				encode(filename, filenameHide, newFileNameHide);
				break;
			case "b":
				
				System.out.println("Please enter the filename that you want to decode: ");
				String fileDecode = in.next();
				System.out.println("Please enter the new filename with no extension for hidden file: ");
				String newFileName = in.next();
				
				decode(fileDecode, newFileName);
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

	/* 
	 * Encode method is a message embedding function.
	 * This method was written on the basis of LSB algorithm.
	 */
	private static void encode(String filename, String filenameHide, String newFileNameHide) throws IOException {

		byte[] bytesOfFile = Files.readAllBytes(Paths.get(filename));
		byte[] bytesOfMssg = Files.readAllBytes(Paths.get(filenameHide));
		byte[] reserveFile = bytesOfFile;

		/*
		 * We are checking if we can store user provided secret message.
		 * If not, we stop executing the method. 
		 */
		if ((double)bytesOfMssg.length / bytesOfFile.length > 0.12) {
			System.out.println("File you are trying to hide is too large!");
			return;
		}
		
		/*
		 * We get the hidden file's extension and store in extension String variable
		 */
		String ext[] = filenameHide.split("\\.");
		String extension = ext[1];

		/*
		 * We will keep our binary code in the following StringBuilder variables.
		 */
		StringBuilder extBits = new StringBuilder();
		StringBuilder mssgBits = new StringBuilder();
		
		/*
		 * mssgLength is a String variable with bytesOfMssg length. 
		 */
		String mssgLength = Long.toBinaryString(bytesOfMssg.length & 0xffffffffL | 0x100000000L).substring(1);

		/*
		 * For loops below will save the binary codes into the StringBuilder variable.
		 */
		for (int i = 0; i < extension.length(); i++) {
			int val = extension.charAt(i);

			for (int j = 0; j < 8; j++) {
				extBits.append((val & 128) == 0 ? 0 : 1);
				val <<= 1;
			}
		}

		for (byte b : bytesOfMssg) {
			int val = b;
			for (int j = 0; j < 8; j++) {
				mssgBits.append((val & 128) == 0 ? 0 : 1);
				val <<= 1;
			}
		}

		int j;

		/*
		 * We start at i = 44, because it is the location where the WAV's data frame starts.
		 * This loop is for embedding hidden message's extension.
		 */
		int iter = 44;

		for (j = 0; j < extBits.length(); j++) {
			if (reserveFile[j + 44] % 2 != 0 && extBits.charAt(j) == '0') {
				reserveFile[j + 44]++;
			} else if (reserveFile[j + 44] % 2 == 0 && extBits.charAt(j) != '0')
				reserveFile[j + 44]--;
		}

		/*
		 * After embedding the extension the value of iter should be 44 + 24(3 spaced extension: txt, pdf, jpg, and etc).
		 */
		iter += j;

		/*
		 *  Here we will hide the length of the hidden file so we know here to stop.
		 *  We store the length of the message in a 32-bit binary code.
		 */
		for (int i = 0; i < mssgLength.length(); iter++, i++) {
			if (reserveFile[iter] % 2 != 0 && mssgLength.charAt(i) == '0')
				reserveFile[iter]++;
			else if (reserveFile[iter] % 2 == 0 && mssgLength.charAt(i) != '0')
				reserveFile[iter]--;
		}	
		iter++;
	
		/*
		 *  Here we will hide the file itself.
		 */
		for (j = 0; iter < 100 + mssgBits.length(); iter++, j++) {
			if (reserveFile[iter] % 2 != 0 && mssgBits.charAt(j) == '0')
				reserveFile[iter]++;
			else if (reserveFile[iter] % 2 == 0 && mssgBits.charAt(j) != '0')
				reserveFile[iter]--;
		}

		/*
		 * We check if new filename contains ".wav" extension.
		 */
		if (!newFileNameHide.contains(".wav"))
			newFileNameHide += ".wav";
		
		/*
		 *  Save the file. We add stega at the beggining of the filename.
		 */
		Files.write(Paths.get(newFileNameHide), reserveFile);

	}

	/*
	 * Hidden message extracting method.
	 * User provides the filename of the WAV file with secret message.
	 * User also provides the new name for the extracted message (file).
	 * This method extracts least significant bits from the WAV with hidded file. 
	 */
	private static void decode(String filename, String newFileName) throws IOException {

		/*
		 *  Read bytes of the WAV file with hidden file.
		 */
		byte[] bytesOfFile = Files.readAllBytes(Paths.get(filename));

		/*
		 * 	iter variable is at 44 because of the WAV data frame. 
		 *  This loop extracts the extension of the hidden file: txt, jpg, and etc.
		 */
		StringBuilder extension = new StringBuilder();
		int iter = 44;
		for (; iter < 68; iter++) {
			if (Math.abs(bytesOfFile[iter]) % 2 == 0)
				extension.append("0");
			else
				extension.append("1");
		}
		
		/*
		 *  After extracting the hidden file extension we save it into the realExt String variable.
		 */
		String ext = extension.toString();
		String realExt = (char) Integer.parseInt(ext.substring(0, 8), 2) + ""
				+ (char) Integer.parseInt(ext.substring(8, 16), 2) + ""
				+ (char) Integer.parseInt(ext.substring(16, 24), 2);

		/*
		 *  This loop extracts hidden file's size. 
		 *  We know that we stored the size in 32-bit binary code. 
		 */
		StringBuilder size = new StringBuilder();
		for (iter = 68; iter < 100; iter++) {
			// System.out.print(Math.abs(bytesOfFile[iter]) % 2);
			if (Math.abs(bytesOfFile[iter]) % 2 == 0)
				size.append("0");
			else
				size.append("1");
		}

		/*
		 * We create an empty byte[] array that we will use to store the hidden message's bytes. 
		 */
		byte[] realHiddenF = new byte[Integer.parseInt(size.toString(), 2)];

		/*
		 *  Size of the hidden file
		 */
		int realSize = Integer.parseInt(size.toString(), 2) * 8;
		StringBuilder sizeF = new StringBuilder();
		int limit = 100;

		int i = 1, j = 0;

		/*
		 * Loop that extracts hidden message.
		 * We save the bits in a sizeF StringBuilder.
		 * Whenever the StringBuilder's length is 8 we convert the binary code into a byte that we store in realHiddenF byte[] array.
		 */
		for (iter = 100; iter < limit + realSize + 8; iter++, i++) {
			sizeF.append(Math.abs(bytesOfFile[iter]) % 2);
			if (i == 9) {
				realHiddenF[j] = (byte) Integer.parseInt(sizeF.toString(), 2);
				j++;
				sizeF = new StringBuilder();
				i = 1;
			}
		}
		
		/*
		 * After loop stops we save the hidden file with user given name.
		 */
		Files.write(Paths.get(newFileName +"."+ realExt), realHiddenF);
	}
}
