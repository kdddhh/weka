package chap03;

public class SimpleLinearRegresion {

	public static void main(String[] args) {
		// 최소제곱법 공식
		int[] X = new int[] {2, 4, 6, 8};
		int[] Y = new int[] {81, 93, 91, 97};
		double avgX = 0, avgY = 0, a1 = 0, a2 = 0, a, b; 
			
		for(int i = 0; i < X.length; i++) {
			avgX += X[i];
			avgY += Y[i];
		}
				
		avgX /= X.length;
		avgY /= Y.length;
				
		for(int i = 0; i < X.length; i++) {
			a1 += (X[i] - avgX) * (Y[i] - avgY);
			a2 += Math.pow((X[i] - avgX), 2); //특정값의 제곱을 구하는 메소드. ()안에 대상 숫자와 지수를 넣어주면 됨
		}
				
		a = a1 /a2;
		b = avgY - (avgX * a);
				
		System.out.println("a: " + a + ", b : " + b + "\n");
			
		// 예측값 출력 코드
		for(int i = 0; i < X.length; i++) {
			System.out.printf("x: %2d, y_pred: %5.2f, y: %2d \n", X[i], a * X[i] + b, Y[i]);
		}

	}

}
