package chap03.test;

import weka.classifiers.functions.LeastMedSq;

public class LeastMedSqDefault {

	public static void main(String[] args) throws Exception {
		LeastMedSq model = new LeastMedSq();
		String fileName = "regression_outliers";
		ModelApply modelApply = new ModelApply(fileName, model);
		
		modelApply.plot2DInstances("outlier recognition", 0, 1);
		
		System.out.println("[ �� ���� & �� ��� ]");
		String result = modelApply.outlierWithCSV();
		System.out.println(model.toString());
		System.out.println(result);


	}

}
