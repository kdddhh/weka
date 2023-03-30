package chap03;

import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LeastMedSq;
import weka.classifiers.functions.LinearRegression;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AddClassification;
import weka.gui.visualize.Plot2D;

public class W5_L1_OutlierWithCSV {

	public static void main(String[] args) throws Exception {
		W5_L1_OutlierWithCSV obj = new W5_L1_OutlierWithCSV();
		String fileName = "regression_outliers";
		System.out.println(fileName + " : ");
		
		// obj.outlierWithCSV(fileName, new LinearRegression(), false, false);
		// obj.outlierWithCSV(fileName, new LinearRegression(), true, false);
		// obj.outlierWithCSV(fileName, new LeastMedSq(), false, false);
		obj.outlierWithCSV(fileName, new LinearRegression(), false, true);
	}
	
	public void outlierWithCSV(String fileName, Classifier model, boolean applyFilter, 
																			boolean eraseOutlier) throws Exception {
		int numFolds = 10;
		int numFold = 0;
		int seed = 1;
		
		CSVLoader loader = new CSVLoader();
		loader.setSource(new File("C:/projects/Weka-3-9-6/data/" + fileName + ".csv"));
		Instances data = loader.getDataSet();
		
		// AddClassification 필터 적용여부에 따른 데이터셋 반환
		if(!eraseOutlier) {
			// 값 삭제가 없는 경우
			data = this.applyAddClassificationFilter(applyFilter, model, data);
		}
		else {
			data = this.eraseOutlier(model, data);
		}
		
		Instances train = data.trainCV(numFolds, numFold, new Random(seed));
		Instances test = data.testCV(numFolds, numFold);
		
		// class assigner역할을 위한 정답 인덱스 설정
		train.setClassIndex(train.numAttributes() - 1);
		test.setClassIndex(train.numAttributes() - 1);
		
		// cross validate 설정
		Evaluation eval = new Evaluation(train);
		
		// 모델 실행
		model.buildClassifier(train);
		eval.crossValidateModel(model, train, numFolds, new Random(seed));// 여기까지가 학습
		
		eval.evaluateModel(model, test);  // 평가
		
		// 결과 출력
		this.printResultTitle(model, applyFilter, eraseOutlier);
		System.out.println(model.toString());
		System.out.println(eval.toSummaryString());
	}
	
	// AddClassification 필터 적용 함수
	public Instances applyAddClassificationFilter(boolean applyFilter, Classifier model, 
																				Instances data) throws Exception{
		if(model instanceof LeastMedSq) {
			System.out.println(" 5) data LeastMedSq");
			this.plot2DInstances(data, "5) data LeastMedSq", 1);
			
			return data;
		}
		
		if(!applyFilter) {
			System.out.println(" 1) outlier recognition");
			this.plot2DInstances(data, "1) outlier recognition", 1);
		}
		else {
			AddClassification filter = new AddClassification();
			filter.setClassifier(model); // 필터를 어떤 모델에 적용할 것인가
			filter.setOutputClassification(true);
			data.setClassIndex(data.numAttributes() - 1); // 속성이 하나 추가 되었기 때문에 마지막 인덱스를 설정해주어야한다.
			filter.setInputFormat(data); // 어떤 데이터 셋을 사용할 것인가
			data = Filter.useFilter(data, filter); // 데이터에 필터를 적용. 필터로 인해 변환되었기때문에 다시 데이터에 저장
			
			System.out.println(" 3) data linearization");
			this.plot2DInstances(data, "3) data linearization", 2);
		}
		
		
		return data;
	}
	
	// 이상 데이터 삭제. 여기선 이상 데이터 8건을 삭제하는 메소드로 구현
	public Instances eraseOutlier(Classifier model, Instances data) throws Exception {
		if(!(model instanceof LinearRegression)) {
			return data;
		}
		
		ArrayList<Attribute> attr = new ArrayList<Attribute>(); // 속성 개체들로 이루어진 리스트 생성
		attr.add(new Attribute("year"));
		attr.add(new Attribute("phone calls"));
		
		// 새로운 인스턴스를 만들때 속성 개체를 넣어줄 수 있다.
		Instances eraseData = new Instances("EraseData", attr, 0);
		
		for(int i = 0; i < data.size(); i++) {
			Instance instance = data.get(i);
			int year = (int)instance.value(0); // 첫번째 컬럼값을 가져와라
			
			if(year >= 63 && year <= 70) {
				System.out.println(i + ", year = " + year + "erased !");
			}
			else {
				eraseData.add(instance);
			}
		}
		
		System.out.println(" 7) eraseData");
		this.plot2DInstances(eraseData, "7) erasedData", 1);
		
		return eraseData;
	}
	
	public void printResultTitle(Classifier model, boolean applyFilter, boolean eraseOutlier) {
		if(!eraseOutlier) {
			if(!(model instanceof LeastMedSq)) {
				System.out.println(" " + 
							((!applyFilter) ? "2) Correlation coefficient" : "4) LinearRegression Model Fomular"));
			}
			else {
				System.out.println(" 6) data LeastMedSq");
			}
		}
		else {
			System.out.println(" 7) eraseData");
		}
	}
	
	public void plot2DInstances(Instances data, String graphTitle, int yIndex) throws Exception {
		Plot2D panel = new Plot2D();
		panel.setInstances(data);
		panel.setXindex(0);
		panel.setYindex(yIndex);
		panel.setCindex(data.numAttributes() - 1);
		
		JFrame frame = new JFrame(graphTitle);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(panel);
		frame.setSize(600, 400);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		System.out.println("See the " + graphTitle + " Plot");
	}
}


















