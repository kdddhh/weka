package chap03.test;

import weka.classifiers.functions.LinearRegression;

public class LinearRegressionEraseOutlier {

	public static void main(String[] args) throws Exception {
		LinearRegression model = new LinearRegression();
		String fileName = "regression_outliers";
		ModelApply modelApply = new ModelApply(fileName, model);
		modelApply.eraseOutlier(new String[]{"year", "phone calls"}, 0, 63, 70);
		
		modelApply.plot2DInstances("outlier recognition", 0, 1);
		
		System.out.println("[ 모델 정보 & 평가 결과 ]");
		String result = modelApply.outlierWithCSV();
		System.out.println(model.toString());
		System.out.println(result);


	}

}
