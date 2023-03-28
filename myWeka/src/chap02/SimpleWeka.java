package chap02;

import java.io.*;
import java.util.*;

import weka.classifiers.trees.RandomForest;
import weka.core.Instances;

public class SimpleWeka {
	
	public static void main(String[] args) throws Exception {
		int numfolds = 10;
		int numfold = 0;
		// �н������Ͱ� ������ ��, �Ʒõ����Ϳ� ���������͸� �и��ϸ� ����� �� �н��� �̷���� �� ����.
		// ����, ������ �� ��ü�� �Ʒõ����ͷ� ����ϰ�, ���� ���������ͷ� ����� �� �ִ�. �̸� ����(fold)��� ��
		int seed = 1;
		
		Instances data = new Instances(new BufferedReader(new FileReader(
															"C:/projects/Weka-3-9-6/data/iris.arff")));
		
		Instances train = data.trainCV(numfolds, numfold, new Random(seed)); // �н�������
		Instances test = data.testCV(numfolds, numfold); // ����������
		/*
		 * ������ �з�
		 */
		
		RandomForest model = new RandomForest();
		
		train.setClassIndex(train.numAttributes() - 1);
		test.setClassIndex(train.numAttributes() - 1);
		/*
		 * �𵨸�
		 * ���⼱ ������ ���� �з��� ���� ���̱� ������ �з��𵨷� ������
		 */
		
	}
	
}
