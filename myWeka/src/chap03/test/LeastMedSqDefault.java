package chap03.test;

import weka.classifiers.functions.LeastMedSq;

public class LeastMedSqDefault {

	public static void main(String[] args) throws Exception {
ModelApply modelApply = new ModelApply();
		
		modelApply.setFileName("regression_outliers");
		modelApply.setModel(new LeastMedSq());
		
		modelApply.outlierWithCSV(false, false);

	}

}
