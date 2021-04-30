package tse;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BranchPrediction {

	public static void main(String[] args) {
		Integer data[] = new Integer[256];
		int sum = 0;
		for (int i = 0; i < 256; i++) {
			data[i] = i + 1;
		}
		long start = System.nanoTime();
		for (int i = 0; i < 256; i++) {
			if (data[i] >= 128)
				sum++;
		}
		Integer temp[] = data;
		System.out.println("-------sorted array---------");
		System.out.println(System.nanoTime() - start +"ns");
		System.out.println("sum = " + sum);
		
		
		System.out.println("-------unsorted array---------");
		List<Integer> intList = Arrays.asList(data);
		Collections.shuffle(intList);
		intList.toArray(data);
		Integer[] shuffledTemp = data;
		start = System.nanoTime();
		for (int i = 0; i < 256; i++) {
			if (data[i] >= 128)
				sum++;
		}
		System.out.println(System.nanoTime() - start +"ns");
		System.out.println("sum = " + sum);
		
		System.out.println("-------With bitwise---------");
		System.out.println("-------sorted array---------");
		data = temp;
		start = System.nanoTime();
		for (int i = 0; i < 256; i++) {
			int t = (data[i] - 128) >> 31;
			sum += ~t & data[i];
		}
		System.out.println(System.nanoTime() - start+"ns");
		System.out.println("sum = " + sum);
		
		
		System.out.println("-------unsorted array---------");
		data = shuffledTemp;
		start = System.nanoTime();
		for (int i = 0; i < 256; i++) {
			int t = (data[i] - 128) >> 31;
			sum += ~t & data[i];
		}
		System.out.println(System.nanoTime() - start +"ns");
		System.out.println("sum = " + sum);
	}

}
