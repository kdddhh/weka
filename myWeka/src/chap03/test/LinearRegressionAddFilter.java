package chap03.test;

import weka.classifiers.functions.LinearRegression;

public class LinearRegressionAddFilter {

	public static void main(String[] args) throws Exception{
		ModelApply modelApply = new ModelApply();
		
		modelApply.setFileName("regression_outliers");
		modelApply.setModel(new LinearRegression());
		
		modelApply.outlierWithCSV(true, false);

	}

}
