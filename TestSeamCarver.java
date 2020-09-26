import java.util.Scanner;

import edu.princeton.cs.algs4.Picture;

public class TestSeamCarver {

	static Scanner scanner = new Scanner(System.in);

	public static String getDimension(String input) {
		input = input.toLowerCase();

		if (input.equals("(h)") || input.equals("h") || input.equals("(h)eight") || input.equals("height")) {
			return "height";
		}

		if (input.equals("(w)") || input.equals("w") || input.equals("(w)idth") || input.equals("width")) {
			return "width";
		}

		throw new IllegalArgumentException("Invalid argument, choose either " + "height or width");
	}

	public static void resize(SeamCarver seamCarver, String direction) {
		if (direction == "height") {
			System.out.println("Enter new height:");
		} else if (direction == "width") {
			System.out.println("Enter new width:");
		} else {
			throw new IllegalArgumentException();
		}

		// read the height/width to resize to
		int input = Integer.parseInt(scanner.nextLine());

		// resize the image to the user-given height or width
		Picture pic = seamCarver.resizeTo(direction, input);

		// save and display the image
		pic.save("pic_resized.jpg");
		pic.show();
	}

	public static void main(String args[]) {
		System.out.println("Are you decreasing the picture's (h)eight or (w)idth?");
		System.out.println("Choose one:");
		String input = scanner.nextLine();
		String mode = getDimension(input);

		System.out.println();

		System.out.println("Enter the path to the image to resize:");
		input = scanner.nextLine();
		Picture inputImg = new Picture(input);
		SeamCarver seamCarver = new SeamCarver(inputImg);
		resize(seamCarver, mode);

	}

}
