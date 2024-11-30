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
				long[] bets = { 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000 };
				for (int i = 0; i < 8; i += 1) {
					bets[i] = ((int) (Math.random() * 50) + 1) * 100;
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
	}
	
	// base(without special rules)
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
			int idx = Math.abs(Arrays.binarySearch(weightSum, (int) (Math.random() * weightSum[rm - 1])));
			System.out.println("idx: " + idx);
			ae = aes[idx - 1];
			sc = scs[ae];
			if (sc < minSc) {
				minSc = sc;
				minScAe = ae;
			}
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

	private static long[] safeLotteryWithSpecialRules(long pool, int[] weights, int[] times, long[] bets) {
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
			for(int i = 0; i < weightSum.length; i += 1) {
				System.out.println("weightSum[" + i + "]: " + weightSum[i]);
			}
			int randomN = (int) (Math.random() * weightSum[rm - 1] + 1);
			System.out.println("randomN: " + randomN);
			int idx = Math.abs(Arrays.binarySearch(weightSum, randomN));
			System.out.println("idx: " + idx);
			ae = aes[idx - 1];
			sc = scs[ae];
			if (sc < minSc) {
				minSc = sc;
				minScAe = ae;
			}
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
	
}