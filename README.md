# Fake-Review-Detection
Exploiting Behavioral Features to Detect Fake Reviews by Means of Contextual Features



# Preprocessing Project Required Jars
* common-lang3.jar
* commons-csv-1.4.jar
* javax.json.jar
* javax.json-api-1.0-sources.jar
* joda-time.jar
* jollyday.jar
* mysql-connector-java-5.1.43-bin.jar
* opencsv-4.0.jar
* poi-3.9.jar
* protobuf.jar
* sqlite-jdbc-3.7.2.jar
* stanford-corenlp-3.5.2.jar
* stanford-corenlp-3.5.2-models.jar
* xom.jar
## <h3> About proprocessing code
 the proprocessing code is written in Java. The downloaded datasets were in form of SQLite, that is why we initially statblish connection with database [com.yelp.database]. A customize engine [com.engine.*] is develop to extract features from text data. The code of overall number of feature extraction can be found in [com.yelp.rest.FeatureExtractor].
# Classifier code in Python
* Python verion 3.X.X
  * SkLearn 0.18.+
  * Pandas 0.20+
 
## <h3> Run Classifiers
 Pre-processing step will generate .CSV file. One can change the filename in given two files.
 <br/>python random_forest_rest.py
 <br/>python svm_res.py

# Detail Study and Experimentation Results
