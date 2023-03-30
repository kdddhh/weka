package chap03.test;

import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AddClassification;
import weka.gui.visualize.Plot2D;

public class ModelApply {
	
	private String fileName;
	private Classifier model;
	private Instances data;
	
	public ModelApply(String fileName, Classifier model) throws Exception{
		this.fileName = fileName;
		this.model = model;
		this.loadCSVData();
	}
	
	private void loadCSVData() throws Exception {
		CSVLoader loader = new CSVLoader();
		loader.setSource(new File("C:/projects/Weka-3-9-6/data/" + this.fileName + ".csv"));
		this.data = loader.getDataSet();
	}

	public String outlierWithCSV() throws Exception {
	int numFolds = 10;
	int numFold = 0;
	int seed = 1;
		Instances train = this.data.trainCV(numFolds, numFold, new Random(seed));
		Instances test = this.data.testCV(numFolds, numFold);
	// class assigner역할을 위한 정답 인덱스 설정
		train.setClassIndex(train.numAttributes() - 1);
		test.setClassIndex(train.numAttributes() - 1);
	// cross validate 설정
		Evaluation eval = new Evaluation(train);
	// 모델 실행
		this.model.buildClassifier(train);
		eval.crossValidateModel(this.model, train, numFolds, new Random(seed));// 여기까지가 학습
		eval.evaluateModel(this.model, test);  // 평가
	// 결과 출력
		return eval.toSummaryString(); // 평가 결과임
	}
	
	// AddClassification 필터 적용 함수
	public void applyAddClassificationFilter() throws Exception {
		AddClassification filter = new AddClassification();
		filter.setClassifier(this.model); // 필터를 어떤 모델에 적용할 것인가
		filter.setOutputClassification(true);
		this.data.setClassIndex(data.numAttributes() - 1); // 속성이 하나 추가 되었기 때문에 마지막 인덱스를 설정해주어야한다.
		filter.setInputFormat(data); // 어떤 데이터 셋을 사용할 것인가
		this.data = Filter.useFilter(this.data, filter); // 데이터에 필터를 적용. 필터로 인해 변환되었기때문에 다시 데이터에 저장
	}
	
	// 이상 데이터 삭제. 여기선 이상 데이터 8건을 삭제하는 메소드로 구현
	public void eraseOutlier(String[] attributes, int targetIdx, int start, int end) throws Exception {	
	ArrayList<Attribute> attrList = new ArrayList<Attribute>(); // 속성 개체들로 이루어진 리스트 생성
		for(String attribute : attributes) {
			attrList.add(new Attribute(attribute));
		}
	// 새로운 인스턴스를 만들때 속성 개체를 넣어줄 수 있다.
	Instances eraseData = new Instances("EraseData", attrList, 0);
		for(int i = 0; i < this.data.size(); i++) {
			Instance instance = this.data.get(i);
			int target = (int)instance.value(targetIdx); // 첫번째 컬럼값을 가져와라
	
			if(target < start || target > end) {
				eraseData.add(instance);
			}
		}
		this.data = eraseData;
	}
	
	public void plot2DInstances(String graphTitle, int xIndex, int yIndex) throws Exception {
	Plot2D panel = new Plot2D();
	panel.setInstances(data);
	panel.setXindex(xIndex);
	panel.setYindex(yIndex);
	panel.setCindex(this.data.numAttributes() - 1);
	
	JFrame frame = new JFrame(graphTitle);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.getContentPane().setLayout(new BorderLayout());
	frame.getContentPane().add(panel);
	frame.setSize(600, 400);
	frame.setLocationRelativeTo(null);
	frame.setVisible(true);
	}
	
}
