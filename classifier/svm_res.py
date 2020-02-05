from sklearn.ensemble import RandomForestClassifier
from sklearn import svm
import pandas as pd
import numpy as np
import datetime
import sys

mylist = [0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1.0,1.1,1.2,1.3,1.4,1.5,1.6,1.7,1.8,1.9,2.0,2.1,2.2,2.3,2.4,2.5,2.6,2.7,2.8,2.9,3.0]
cols = ["usefulCount","coolCount","funnyCount","friendCount","tipCount","reviewCount","avg_posting_rate","average_content_similarityBM","reviewer_deviattion","membership_length","rev_duration","capital_diversity"]
colsRes = ["flagged"]

print(" Please donot close the window till experiments ends..")
orig_stdout = sys.stdout
f = open('out_svm.txt', 'a')
sys.stdout = f
print(len(cols)," Features   BM   6000 SVM" )
print(datetime.datetime.now())
MaxP = 0.0
MaxR = 0.0
MaxF = 0.0 
MaxA = 0.0

for cvalue in mylist:
    print("C=",cvalue)
    sumP = 0.0
    sumR = 0.0
    sumF = 0.0
    sumA = 0.0
    for i in range(1,11):
        filename = "All_Reviews - BM_"+str(i)+".csv"
        dataset = pd.read_csv(filename)
        # print(dataset.head())
        length_dataset = len(dataset)
        # print("dataset length ",length_dataset)
        chunck = int(length_dataset/10)
        # print("chunk ",chunck)

        train = dataset[0:(length_dataset-chunck)]
        test = dataset[(length_dataset-chunck):length_dataset]

        trainArr = train.as_matrix(cols) #training array
        trainRes = train.as_matrix(colsRes) # training results
        size = len(trainRes)

        rf = svm.SVC(kernel="linear", C=cvalue, gamma=0.2)  # initialize

        rf.fit(trainArr, trainRes.ravel())  # fit the data to the algorithm

        testArr = test.as_matrix(cols)
        results = rf.predict(testArr)
        loop = len(results)
        count = 0
        # print("Len of test ",len(test))
        totalYes = 0
        TP = 0
        predictedYes = 0
        for i in range(0, loop):
            if test['flagged'][i + (length_dataset - chunck)] == 'Y':
                totalYes += 1
            if results[i] == 'Y':
                predictedYes += 1
            if (results[i] == test['flagged'][i + (length_dataset - chunck)]):
                if results[i] == 'Y':
                    TP += 1
                count = count + 1

        # print("count = ",count)
        # print("total = ",loop)
        recall = (TP / totalYes)
        precision = (TP / (predictedYes))
        f1 = 2 * ((precision * recall) / (precision + recall))
        accuracy = (count / loop) * 100.0
        print("precision,", precision, ", recall ,", recall, ",F1,", f1, ",accuracy,", accuracy)
        sumA+=accuracy
        sumF+=f1
        sumR+=recall
        sumP+=precision
    print(" ,",(sumP/10)," , ,",(sumR/10)," , ,",(sumF*10)," , , ",(sumA/10))
    if(sumP>MaxP):
        MaxP = sumP
    if(sumR>MaxR):
        MaxR = sumR
    if(sumF>MaxF):
        MaxF = sumF
    if(sumA>MaxA):
        MaxA = sumA

# test['predictions'] = results
# test.head()
print("MAXIMUM,",(MaxP/10),",",(MaxR/10),",",(MaxF*10),",",(MaxA/10))
print(datetime.datetime.now())
sys.stdout = orig_stdout
f.close()