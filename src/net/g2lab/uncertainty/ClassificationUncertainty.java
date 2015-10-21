package net.g2lab.uncertainty;


public class ClassificationUncertainty {

	private int[] clusterMeans;
	private int[][] pixels;
	private int[] uncertainty;

	public ClassificationUncertainty(int[] clusterMeans, int[][] pixels) {
		this.clusterMeans = clusterMeans;
		this.pixels = pixels;
		computeUncertainty();
	}
	
	/** Uncertainty values as integer in [0, 100]
	 * @return the uncertainty
	 */
	public int[] getUncertainty() {
		return uncertainty;
	}

	public void computeUncertainty() {
		double[] distances = new double[pixels.length];
		double maxDistance = 0;
		for (int i = 0; i < distances.length; i++) {
			distances[i] = computeDistance(pixels[i], clusterMeans);
			if (distances[i] > maxDistance) {
				maxDistance = distances[i]; 
			}
		}
		this.uncertainty = new int[distances.length];
		for (int i = 0; i < distances.length; i++) {
			uncertainty[i] = (int) (100 * distances[i] / maxDistance);
		}
	}

	private double computeDistance(int[] vector0, int[] vector1) {
		double sum = 0;
		for (int i = 0; i < vector0.length; i++) {
			sum += (vector0[i] - vector1[i]) + (vector0[i] - vector1[i]);
		}
		return Math.sqrt(sum);
	}

}
