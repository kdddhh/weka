package chap02;

import java.io.*;
import java.util.*;

import weka.classifiers.trees.RandomForest;
import weka.core.Instances;

public class SimpleWeka {
	
	public static void main(String[] args) throws Exception {
		int numfolds = 10;
		int numfold = 0;
		// 학습데이터가 부족할 때, 훈련데이터와 검증데이터를 분리하면 제대로 된 학습이 이루어질 수 없다.
		// 따라서, 데이터 셋 전체를 훈련데이터로 사용하고, 또한 검증데이터로 사용할 수 있다. 이를 폴드(fold)라고 함
		int seed = 1;
		
		Instances data = new Instances(new BufferedReader(new FileReader(
															"C:/projects/Weka-3-9-6/data/iris.arff")));
		
		Instances train = data.trainCV(numfolds, numfold, new Random(seed)); // 학습데이터
		Instances test = data.testCV(numfolds, numfold); // 검증데이터
		/*
		 * 데이터 분류
		 */
		
		RandomForest model = new RandomForest();
		
		train.setClassIndex(train.numAttributes() - 1);
		test.setClassIndex(train.numAttributes() - 1);
		/*
		 * 모델링
		 * 여기선 데이터 셋이 분류를 위한 모델이기 때문에 분류모델로 정의함
		 */
		
	}
	
}
