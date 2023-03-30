package chap03.test;

import weka.classifiers.functions.LinearRegression;

public class LinearRegressionDefault {

	public static void main(String[] args) throws Exception {
		LinearRegression model = new LinearRegression();
		String fileName = "regression_outliers";
		ModelApply modelApply = new ModelApply(fileName, model);
		
		modelApply.plot2DInstances("outlier recognition", 0, 1);
		
		System.out.println("[ �� ���� & �� ��� ]");
		String result = modelApply.outlierWithCSV();
		System.out.println(model.toString());
		System.out.println(result);
	}

}
