from sklearn.ensemble import RandomForestClassifier
from sklearn import svm
import pandas as pd
import numpy as np
import datetime
import sys

cols = ["usefulCount","coolCount","funnyCount","friendCount","tipCount","reviewCount","avg_posting_rate","average_content_similarityBM","reviewer_deviattion","membership_length","rev_duration","capital_diversity"]
colsRes = ["flagged"]
vals = [0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]

print(" Please donot close the window till experiments ends..")
orig_stdout = sys.stdout
f = open('out_randomForest_imp.txt', 'a')
sys.stdout = f
print(len(cols)," Features   BM   6000 " )
print(datetime.datetime.now())
maximumPrecision = 0.0
maximumRecall = 0.0
maximumAccuracy = 0.0
maximumF = 0.0

nestimator= [6]
maxDepth = [91]
minsplit = [9]
minsampleLeaf = [2]

for cnestimator in nestimator:
    for cvalue in maxDepth:
        for cmaxsplit in minsplit:
            for cmaxsampleLeaf in minsampleLeaf:
                iterationPrecision = 0.0
                iterationRecall = 0.0
                iterationF = 0.0
                iterationAccuracy = 0.0
                print(" , , , ")
                print(", n_estimator=",cnestimator," max_depth=",cvalue,",min_sample_split=",cmaxsplit,",min_sample_leaf=",cmaxsampleLeaf)
                print(" , precision, recall, F1, accuracy")
                sys.stdout.flush()
                for i in range(1,11):
                    filename = "All_Reviews - BM_"+str(i)+".csv"
                    dataset = pd.read_csv(filename)
                    # print(dataset.head())
                    length_dataset = len(dataset)
                    #print("dataset length ",length_dataset)
                    chunck = int(length_dataset/10)
                    #print("chunk ",chunck)

                    train = dataset[0:(length_dataset-chunck)]
                    test = dataset[(length_dataset-chunck):length_dataset]

                    trainArr = train.as_matrix(cols) #training array
                    trainRes = train.as_matrix(colsRes) # training results
                    size = len(trainRes)
                    rf = RandomForestClassifier(n_estimators=cnestimator, max_depth=cvalue, min_samples_split=cmaxsplit,min_samples_leaf=cmaxsampleLeaf) # initialize


                    rf.fit(trainArr, trainRes.ravel()) # fit the data to the algorithm


                    testArr = test.as_matrix(cols)
                    results = rf.predict(testArr)
                    loop = len(results)
                    count =0
                    #print("Len of test ",len(test))
                    totalYes = 0
                    TP = 0
                    predictedYes = 0
                    for i in range(0,loop):
                        if test['flagged'][i+(length_dataset-chunck)] == 'Y':
                            totalYes+=1
                        if results[i] == 'Y':
                            predictedYes +=1
                        if(results[i]== test['flagged'][i+(length_dataset-chunck)]):
                            if results[i] == 'Y':
                                TP+=1
                            count = count+1


                    #print("count = ",count)
                    #print("total = ",loop)
                    importances = rf.feature_importances_
                    std = np.std([tree.feature_importances_ for tree in rf.estimators_],axis=0)
                    indices = np.argsort(importances)[::-1]

                    # Print the feature ranking
                    print("Feature ranking:")

                    for fe in range(trainArr.shape[1]):
                        print(cols[fe]," %f" % (importances[indices[fe]]))
                        vals[fe] = vals[fe]+ importances[indices[fe]]

                    recall = (TP/totalYes)
                    precision = (TP/(predictedYes))
                    f1 = 2*( (precision*recall) /(precision+recall)  )
                    accuracy = (count / loop) * 100.0
                    print("precision,", precision, ", recall,", recall,", F1,",f1," , accuracy,",accuracy)
                    iterationPrecision += precision
                    iterationRecall += recall
                    iterationAccuracy += accuracy
                    iterationF += f1
                    sys.stdout.flush()
                avgPrecision = (iterationPrecision / 10)
                avgRecall = (iterationRecall / 10)
                avgAccuracy = (iterationAccuracy / 10)
                avgF = (iterationF*10)
                if avgAccuracy > maximumAccuracy:
                    maximumAccuracy = avgAccuracy
                if avgPrecision > maximumPrecision:
                    maximumPrecision = avgPrecision
                if avgRecall > maximumRecall:
                    maximumRecall = avgRecall
                if avgF > maximumF:
                    maximumF = avgF
                print(" TOTAL, ", iterationPrecision, ",", iterationRecall, ",",iterationF," ,", iterationAccuracy)
                print(datetime.datetime.now())

print(" MAXIMUM, ",maximumPrecision, ",", maximumRecall, ",",maximumF," ,", maximumAccuracy)
for v in vals:
    print(v)
sys.stdout = orig_stdout
f.close()
