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
		
		// AddClassification ���� ���뿩�ο� ���� �����ͼ� ��ȯ
		if(!eraseOutlier) {
			// �� ������ ���� ���
			data = this.applyAddClassificationFilter(applyFilter, model, data);
		}
		else {
			data = this.eraseOutlier(model, data);
		}
		
		Instances train = data.trainCV(numFolds, numFold, new Random(seed));
		Instances test = data.testCV(numFolds, numFold);
		
		// class assigner������ ���� ���� �ε��� ����
		train.setClassIndex(train.numAttributes() - 1);
		test.setClassIndex(train.numAttributes() - 1);
		
		// cross validate ����
		Evaluation eval = new Evaluation(train);
		
		// �� ����
		model.buildClassifier(train);
		eval.crossValidateModel(model, train, numFolds, new Random(seed));// ��������� �н�
		
		eval.evaluateModel(model, test);  // ��
		
		// ��� ���
		this.printResultTitle(model, applyFilter, eraseOutlier);
		System.out.println(model.toString());
		System.out.println(eval.toSummaryString());
	}
	
	// AddClassification ���� ���� �Լ�
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
			filter.setClassifier(model); // ���͸� � �𵨿� ������ ���ΰ�
			filter.setOutputClassification(true);
			data.setClassIndex(data.numAttributes() - 1); // �Ӽ��� �ϳ� �߰� �Ǿ��� ������ ������ �ε����� �������־���Ѵ�.
			filter.setInputFormat(data); // � ������ ���� ����� ���ΰ�
			data = Filter.useFilter(data, filter); // �����Ϳ� ���͸� ����. ���ͷ� ���� ��ȯ�Ǿ��⶧���� �ٽ� �����Ϳ� ����
			
			System.out.println(" 3) data linearization");
			this.plot2DInstances(data, "3) data linearization", 2);
		}
		
		
		return data;
	}
	
	// �̻� ������ ����. ���⼱ �̻� ������ 8���� �����ϴ� �޼ҵ�� ����
	public Instances eraseOutlier(Classifier model, Instances data) throws Exception {
		if(!(model instanceof LinearRegression)) {
			return data;
		}
		
		ArrayList<Attribute> attr = new ArrayList<Attribute>(); // �Ӽ� ��ü��� �̷���� ����Ʈ ����
		attr.add(new Attribute("year"));
		attr.add(new Attribute("phone calls"));
		
		// ���ο� �ν��Ͻ��� ���鶧 �Ӽ� ��ü�� �־��� �� �ִ�.
		Instances eraseData = new Instances("EraseData", attr, 0);
		
		for(int i = 0; i < data.size(); i++) {
			Instance instance = data.get(i);
			int year = (int)instance.value(0); // ù��° �÷����� �����Ͷ�
			
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


















