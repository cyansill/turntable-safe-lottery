import java.util.Arrays;

class Main {
	
	public static void main(String ...args) {
		long pool = 8000L;
		int[] weights = { 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 };
		int[] times = { 10, 15, 25, 45, 5, 5, 5, 5 };
		int[] bets = { 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000 };

		// int[] res = safeLottery(pool, weights, times, bets);
		int[] res = safeLotteryWithSpecialRules(pool, weights, times, bets);
		System.out.println(res[0] + " " + res[1]);
	}
	
	// base(without special rules)
	private static int[] safeLottery(long pool, int[] weights, int[] times, int[] bets) {
    int rm = weights.length;
		int sc = 0;
		int ae = 0;
		int totalWeight = 0;
		int[] weightSum = new int[rm + 1];
		int[] scs = new int[rm];
		int[] aes = new int[rm];
    int[] res = new int[2];
		for (int i = 0; i < rm; i++) {
      weightSum[i + 1] = weightSum[i] + weights[i];
			aes[i] = i;
			scs[i] = times[i] * bets[i];
			totalWeight += weights[i];
    }
		do {
			System.out.println("rm: " + rm);
			int idx = Math.abs(Arrays.binarySearch(weightSum, (int) (Math.random() * weightSum[rm - 1])));
			System.out.println("idx: " + idx);
			ae = aes[idx - 1];
			sc = scs[ae];
			if (sc > pool) { // remove item on index ae
				int prevTotalWeight = totalWeight;
				totalWeight -= weights[ae];
				for (int i = ae; i < rm - 1; i += 1) {
					aes[i] = aes[i + 1];
					scs[i] = scs[i + 1];
					if (i != rm - 1) {
						weightSum[i + 1] = weightSum[i + 2];
					}
				}
				weightSum[rm] = 0;
				for (int i = 0; i < rm; i += 1) {
					weightSum[i] = totalWeight * weightSum[i] / prevTotalWeight;
				}
				rm -= 1;
			}
		} while (rm > 0 && sc > pool);
		res[0] = ae;
		res[1] = sc;
		return res;
	}

	private static int[] safeLotteryWithSpecialRules(long pool, int[] weights, int[] times, int[] bets) {
    int rm = weights.length;
		int sc = 0;
		int ae = 0;
		int totalWeight = 0;
		int[] weightSum = new int[rm + 1];
		int[] scs = new int[rm];
		int[] aes = new int[rm];
    int[] res = new int[2];
		for (int i = 0; i < rm; i++) {
      weightSum[i + 1] = weightSum[i] + weights[i];
			aes[i] = i;
			if (i < 8) {
				scs[i] = times[i] * bets[i];
			} else if (i == 8) {
				scs[i] = 0;
				for (int j = 0; j < 4; j += 1) {
					scs[i] += times[j] * bets[j];
				}
			} else if (i == 9) {
				scs[i] = 0;
				for (int j = 4; j < 8; j += 1) {
					scs[i] += times[j] * bets[j];
				}
			}
			totalWeight += weights[i];
    }
		do {
			System.out.println("rm: " + rm);
			int idx = Math.abs(Arrays.binarySearch(weightSum, (int) (Math.random() * weightSum[rm - 1])));
			System.out.println("idx: " + idx);
			ae = aes[idx - 1];
			sc = scs[ae];
			if (sc > pool) { // remove item on index ae
				int prevTotalWeight = totalWeight;
				totalWeight -= weights[ae];
				for (int i = ae; i < rm - 1; i += 1) {
					aes[i] = aes[i + 1];
					scs[i] = scs[i + 1];
					if (i != rm - 1) {
						weightSum[i + 1] = weightSum[i + 2];
					}
				}
				weightSum[rm] = 0;
				for (int i = 0; i < rm; i += 1) {
					weightSum[i] = totalWeight * weightSum[i] / prevTotalWeight;
				}
				rm -= 1;
			}
		} while (rm > 0 && sc > pool);
		res[0] = ae;
		res[1] = sc;
		return res;	  
	}
	
}