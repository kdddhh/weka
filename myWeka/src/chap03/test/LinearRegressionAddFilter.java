package chap03.test;

import weka.classifiers.functions.LinearRegression;

public class LinearRegressionAddFilter {

	public static void main(String[] args) throws Exception{
		LinearRegression model = new LinearRegression();
		String fileName = "regression_outliers";
		ModelApply modelApply = new ModelApply(fileName, model);
		modelApply.applyAddClassificationFilter();
		
		modelApply.plot2DInstances("outlier recognition", 0, 2);
		// 필터가 적용되면 속성이 추가되기 때문에 yIndex도 변경해주어야된다
		
		System.out.println("[ 모델 정보 & 평가 결과 ]");
		String result = modelApply.outlierWithCSV();
		System.out.println(model.toString());
		System.out.println(result);

	}

}
