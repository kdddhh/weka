package chap02;

import java.io.*;
import java.util.Random;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.*;

public class SimpleWeka {
	
	public static void main(String args[]) throws Exception{
		int numfolds = 10;
		int numfold = 0;
		int seed = 1;
		Instances data = new Instances(
				       new BufferedReader(
				       new FileReader("C:/projects/Weka-3-9-6/weka-main/weka-main/data/iris.arff")));
		Instances train = data.trainCV(numfolds, numfold, new Random(seed));
		Instances test  = data.testCV (numfolds, numfold);
		
		RandomForest model=new RandomForest();

		// 2) class assigner
		train.setClassIndex(train.numAttributes()-1);
		test.setClassIndex(test.numAttributes()-1);
		
		// 3) cross validate setting  
		Evaluation eval=new Evaluation(train);

		eval.crossValidateModel(model, train, numfolds, new Random(seed));

		// 4) random forest run 
		model.buildClassifier(train);
		
		// 5) evaluate
		eval.evaluateModel(model, test);
		
		// 6) print Result text
		System.out.println(model);                  // model info
		System.out.println(eval.toSummaryString()); // === Evaluation result ===
		System.out.println(eval.toMatrixString());  // === Confusion Matrix === 
	}

}
