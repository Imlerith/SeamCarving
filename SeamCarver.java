import java.awt.Color;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

	private static final boolean HORIZONTAL = true;
	private static final boolean VERTICAL = false;

	private Picture picture;
	// array of lengths of the shortest paths
	private double[] distTo;
	// array of last edges on the shortest paths
	private int[][] edgeTo;

	// create a seam carver object based on the given picture
	public SeamCarver(Picture picture) {
		if (picture == null) {
			throw new IllegalArgumentException();
		}

		this.picture = new Picture(picture);

	}

	// current picture
	public Picture picture() {
		return new Picture(picture);
	}

	// width of current picture
	public int width() {
		return picture.width();
	}

	// height of current picture
	public int height() {
		return picture.height();
	}

	// energy of pixel at column x and row y
	public double energy(int x, int y) {
		if (x < 0 || y < 0 || x > width() - 1 || y > height() - 1) {
			throw new IllegalArgumentException();
		}

		// border pixels
		if (x == 0 || y == 0 || x == width() - 1 || y == height() - 1) {
			return 1000.0;
		}

		// get colors of the neighbor pixels used to compute the energy
		Color top = picture.get(x, y + 1);
		Color bottom = picture.get(x, y - 1);
		Color left = picture.get(x - 1, y);
		Color right = picture.get(x + 1, y);

		// return the pixel energy as the square root of the sum of gradients
		return Math.sqrt(squareGradient(top, bottom) + squareGradient(left, right));
	}

	// sequence of indices for horizontal seam
	public int[] findHorizontalSeam() {
		return findSeam(HORIZONTAL);
	}

	// sequence of indices for vertical seam
	public int[] findVerticalSeam() {
		return findSeam(VERTICAL);
	}

	// remove horizontal seam from current picture
	public void removeHorizontalSeam(int[] seam) {
		if (seam == null || height() <= 1 || seam.length != width()) {
			throw new IllegalArgumentException();
		}

		Picture new_picture = new Picture(width(), height() - 1);

		int prev_seam = seam[0];

		for (int x = 0; x < width(); x++) {
			if (Math.abs(seam[x] - prev_seam) > 1 || seam[x] < 0 || seam[x] >= height()) {
				throw new IllegalArgumentException();
			}
			prev_seam = seam[x];

			for (int y = 0; y < height(); y++) {
				if (seam[x] == y)
					continue;

				Color color = picture.get(x, y);
				new_picture.set(x, seam[x] > y ? y : y - 1, color);
			}
		}

		this.picture = new_picture;

	}

	// remove vertical seam from current picture
	public void removeVerticalSeam(int[] seam) {
		if (seam == null || width() <= 1 || seam.length != height()) {
			throw new IllegalArgumentException();
		}

		// create new picture with reduced width
		Picture new_picture = new Picture(width() - 1, height());

		int prev_seam = seam[0];

		// going down the picture
		for (int y = 0; y < height(); y++) {
			if (Math.abs(seam[y] - prev_seam) > 1 || seam[y] < 0 || seam[y] >= width()) {
				throw new IllegalArgumentException();
			}

			prev_seam = seam[y];

			for (int x = 0; x < width(); x++) {
				if (seam[y] == x)
					continue;

				Color color = picture.get(x, y);
				new_picture.set(seam[y] > x ? x : x - 1, y, color);
			}
		}

		this.picture = new_picture;
	}

	private double squareGradient(Color first, Color second) {
		return Math.pow(first.getRed() - second.getRed(), 2) + Math.pow(first.getGreen() - second.getGreen(), 2)
				+ Math.pow(first.getBlue() - second.getBlue(), 2);
	}

	private int[] findSeam(boolean direction) {
		distTo = (direction == VERTICAL) ? new double[width()] : new double[height()];
		edgeTo = new int[width()][height()];

		for (int i = 0; i < distTo.length; i++) {
			distTo[i] = 1000;
		}

		int maxI = (direction == VERTICAL) ? height() : width();
		int maxJ = (direction == VERTICAL) ? width() : height();

		// find minimum distances
		for (int i = 1; i < maxI; i++) {
			// save the array of distances into an auxiliary distances' array
			// and initialize distances to infinity
			double[] lastDistTo = distTo.clone();
			for (int k = 0; k < distTo.length; k++) {
				distTo[k] = Double.POSITIVE_INFINITY;
			}

			// calculate the energies along the dimension
			// along which the distances are being found
			// (e.g. horizontal for vertical seams)
			for (int j = 1; j < maxJ; j++) {
				int x = (direction == VERTICAL) ? j : i;
				int y = (direction == VERTICAL) ? i : j;

				double energy = energy(x, y);

				// update (relax) distances from 3 vertices in the previous row/column
				// to the current vertex (x, y)
				relax(j - 1, x, y, energy, lastDistTo, direction);
				relax(j, x, y, energy, lastDistTo, direction);
				relax(j + 1, x, y, energy, lastDistTo, direction);
			}
		}

		// find the minimum distance to the elements in the final row/column
		// and the corresponding index
		double minWeight = Double.POSITIVE_INFINITY;
		int min = 0;

		for (int i = 0; i < distTo.length; i++) {
			double weight = distTo[i];
			if (weight < minWeight) {
				min = i;
				minWeight = weight;
			}
		}

		// pre-allocate an array for the seam (e.g. with the length equal to the height
		// if we want to find a vertical seam
		int[] seam = (direction == VERTICAL) ? new int[height()] : new int[width()];

		// back-track the edges on the shortest path
		if (direction == VERTICAL) {
			for (int y = height() - 1; y >= 0; y--) {
				seam[y] = min;
				min = edgeTo[min][y];
			}
		} else {
			for (int x = width() - 1; x >= 0; x--) {
				seam[x] = min;
				min = edgeTo[x][min];
			}
		}

		return seam;

	}

	private void relax(int aboveVertex, int x, int y, double energy, double[] lastDistTo, boolean direction) {
		if (aboveVertex < 0 || aboveVertex >= lastDistTo.length) {
			return;
		}

		// distance from the previous vertex under consideration
		// to the vertex (x, y)
		double weight = lastDistTo[aboveVertex];

		int index = (direction == VERTICAL) ? x : y;

		// if we have found a shorter path, then
		// update both the distance and the edge on the shortest path
		if (distTo[index] > weight + energy) {
			distTo[index] = weight + energy;
			edgeTo[x][y] = aboveVertex;
		}
	}

	public Picture resizeTo(String mode, int dimension) {
		// resize the width: remove vertical seams
		if (mode.equals("width")) {
			while (this.width() > dimension) {
				System.out.println("Resizing... Currently at width " + this.width());
				int[] seam = this.findVerticalSeam();
				this.removeVerticalSeam(seam);
			}
		} else if (mode.equals("height")) {
		// resize the height: remove horizontal seams
			while (this.height() > dimension) {
				System.out.println("Resizing... Currently at height " + this.height());
				int[] seam = this.findHorizontalSeam();
				this.removeHorizontalSeam(seam);
			}
		} else {
			throw new IllegalArgumentException();
		}
		
		System.out.println();
		System.out.println("Resizing complete: now at width " + this.width() + ", height " + this.height());
		return this.picture();
	}

}
