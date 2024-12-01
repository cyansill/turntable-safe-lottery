import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

class Main {
	
	public static void main(String ...args) {
		AtomicLong pool = new AtomicLong(-100000L);
		final int[] weights = { 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 };
		final int[] times = { 10, 15, 25, 45, 5, 5, 5, 5 };

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				System.out.println("============ new round start ============");
				System.out.println("pool: " + pool);
				long[] bets = { 3000, 2000, 2000, 2000, 0, 6000, 6000, 6000 };
				for (int i = 0; i < 8; i += 1) {
					// bets[i] = ((int) (Math.random() * 50) + 1) * 100;
					pool.addAndGet(bets[i]);
					System.out.printf("the bet %d is %d\n", i, bets[i]);
				}
				// int[] res = safeLottery(pool, weights, times, bets);
				long[] res = safeLotteryWithSpecialRules(pool.get(), weights, times, bets);
				pool.addAndGet(-res[1]);
				System.out.println(res[0] + " " + res[1]);
				System.out.println("pool: " + pool);
				System.out.println("============ new round end ============");
			}
		}, 0, 1000);
		// long[] bets = { 3000, 2000, 2000, 2000, 0, 6000, 6000, 6000 };
		// long[] res = safeLotteryWithSpecialRules(pool.get(), weights, times, bets);
		// System.out.println(res[0] + " " + res[1]);
	}
	
	/**
	 * safe lottery (make sure the pool is always to be positive. or make sure the pool is always increasing when it is negative)
	 * @param pool the pool of money
	 * @param weights the probability weight of the items
	 * @param times the times of the bets
	 * @param bets the bets on each item
	 * @return a array typed long which contains the index of the item and the amount of money to be decreased to the pool
	 */
	private static long[] safeLottery(long pool, int[] weights, int[] times, long[] bets) {
    int rm = weights.length;
		long sc = 0;
		int ae = 0;
		int totalWeight = 0;
		long minSc = Long.MAX_VALUE;
		int minScAe = 0;
		int[] weightSum = new int[rm + 1];
		long[] scs = new long[rm];
		int[] aes = new int[rm];
    long[] res = new long[2];
		for (int i = 0; i < rm; i++) {
      weightSum[i + 1] = weightSum[i] + weights[i];
			aes[i] = i;
			scs[i] = times[i] * bets[i];
			totalWeight += weights[i];
    }
		do {
			System.out.println("rm: " + rm);
			int idx = Math.abs(Arrays.binarySearch(weightSum, (int) (Math.random() * weightSum[rm])));
			ae = aes[idx - 1];
			sc = scs[ae];
			if (sc < minSc) {
				minSc = sc;
				minScAe = ae;
			}
			if (sc > pool) { // remove item on index ae
				int prevTotalWeight = totalWeight;
				totalWeight -= weights[ae];
				for (int i = idx - 1; i < aes.length - 1; i += 1) { 
					aes[i] = aes[i + 1]; 
					if (i < rm - 1) {
						weightSum[i + 1] = weightSum[i + 2];
					}
				}
				weightSum[rm] = Integer.MAX_VALUE;
				for (int i = 0; i < rm; i += 1) {
					weightSum[i] = totalWeight * weightSum[i] / prevTotalWeight;
				}
				rm -= 1;
			}
		} while (rm > 0 && sc > pool);
		if (sc > pool) {
			ae = minScAe;
			sc = minSc;
		}
		res[0] = ae;
		res[1] = sc;
		return res;
	}

	/**
	 * safe lottery with special rules (make sure the pool is always to be positive. or make sure the pool is always increasing when it is negative)
	 * when result area equals to 8 or 9, the cost of pool is calculated by the sum of the bets of the first 4 or the last 4 items
	 * @param pool the pool of money
	 * @param weights the probability weight of the items
	 * @param times the times of the bets
	 * @param bets the bets on each item
	 * @return a array typed long which contains the index of the item and the amount of money to be decreased to the pool
	 */
	private static long[] safeLotteryWithSpecialRules(long pool, int[] weights, int[] times, long[] bets) {
    int rm = weights.length;
		long sc = 0;
		int ae = 0;
		int totalWeight = 0;
		int runCount = 0;
		long minSc = Long.MAX_VALUE;
		int minScAe = 0;
		int[] weightSum = new int[rm + 1];
		long[] scs = new long[rm];
		int[] aes = new int[rm];
    long[] res = new long[2];
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
			System.out.printf("the aes[%d] is %d\n", i, aes[i]);
    }
		
		do {
			runCount += 1;
			int randomN = (int) (Math.random() * weightSum[rm]); // 0 ~ totalWeight
			int _idx = Arrays.binarySearch(weightSum, randomN);
			int idx = _idx < 0 ? -_idx - 1 : _idx;
			System.out.printf("the randomN is %d, the idx is %d\n", randomN, idx);
			if (idx != 0) idx -= 1;
			ae = aes[idx]; 
			sc = scs[ae];
			System.out.printf("the ae is %d, the sc is %d \n", ae, sc);
			if (sc < minSc) {
				minSc = sc;
				minScAe = ae;
			}
			if (sc > pool) { // remove item on index ae
				int prevTotalWeight = totalWeight;
				totalWeight -= weights[ae];
				for (int i = idx; i < aes.length - 1; i += 1) { 
					aes[i] = aes[i + 1]; 
					if (i < rm - 1) {
						weightSum[i + 1] = weightSum[i + 2];
					}
				}
				weightSum[rm] = Integer.MAX_VALUE;
				for (int i = 0; i < rm; i += 1) {
					weightSum[i] = totalWeight * weightSum[i] / prevTotalWeight;
				}
				rm -= 1;
			}
		} while (rm > 0 && sc > pool);
		System.out.printf("the runCount is %d\n", runCount);
		if (sc > pool) {
			ae = minScAe;
			sc = minSc;
		}
		res[0] = ae;
		res[1] = sc;
		return res;	  
	}
	
}