package xbee.udootest;

import android.app.Activity;
import android.content.Context;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import me.palazzetti.adktoolkit.AdkManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import weka.classifiers.Classifier;
import weka.classifiers.lazy.IBk;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import wlsvm.WLSVM;






public class MainActivity extends Activity {

//	private static final String TAG = "UDOO_AndroidADKFULL";	 

    private AdkManager mAdkManager;

    private ToggleButton buttonLED;
    private TextView distance;
    private TextView pulse;
    private TextView position;

    private AdkReadTask mAdkReadTask;
    private WLSVM svmCls;
    private String svmModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Create the classifier - serialize it if have to.
         * Set the sepal and petal value added, etc.
         * Listen to the button click
         */
        try {
            svmCls = (WLSVM) weka.core.SerializationHelper.read(svmModel);
        } catch (java.lang.Exception e) {
            svmCls = new WLSVM();
        }
        svmModel = svmCls.toString();
        trainUsingData();
    }
    public void trainUsingData() {
        File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File f = new File(root, "iris_train.arff");
        BufferedReader inputReader;
        try {
            inputReader = new BufferedReader(new FileReader(f));
            Instances data = new Instances(inputReader);
            data.setClassIndex(data.numAttributes() - 1);
            svmCls.buildClassifier(data);

        } catch (java.lang.Exception e) {

        }
    }
    public void train() {
        // train
        Attribute Attribute1 = new Attribute("sepallength");
        Attribute Attribute2 = new Attribute("sepalwidth");
        Attribute Attribute3 = new Attribute("petallength");
        Attribute Attribute4 = new Attribute("petalwidth");
        // Declare the class attribute along with its values (nominal)
        FastVector fvClassVal = new FastVector(3);
        fvClassVal.addElement("Iris-setosa");
        fvClassVal.addElement("Iris-versicolor");
        fvClassVal.addElement("Iris-virginica");
        Attribute ClassAttribute = new Attribute("class", fvClassVal);
        // Declare the feature vector template
        FastVector fvWekaAttributes = new FastVector(5);
        fvWekaAttributes.addElement(Attribute1);
        fvWekaAttributes.addElement(Attribute2);
        fvWekaAttributes.addElement(Attribute3);
        fvWekaAttributes.addElement(Attribute4);
        fvWekaAttributes.addElement(ClassAttribute);
        String slValue = ((EditText)findViewById(R.id.SepalLength)).toString();
        String swValue = ((EditText)findViewById(R.id.SepalWidth)).toString();
        String plValue = ((EditText)findViewById(R.id.PetalLength)).toString();
        String pwValue = ((EditText)findViewById(R.id.PetalWidth)).toString();
        // Creating testing instances object with name "TestingInstance"// using the feature vector templatewe declared above//and with initial capacity of 1
        Instances testingSet = new Instances("TestingInstance", fvWekaAttributes, 1);
        // Setting the column containing class labels:
        testingSet.setClassIndex(testingSet.numAttributes() - 1);
        // Create and fill an instance, and add it to the testingSetInstance i
        Instance iExample = new Instance(testingSet.numAttributes());
        iExample.setValue((Attribute) fvWekaAttributes.elementAt(0), slValue);
        iExample.setValue((Attribute) fvWekaAttributes.elementAt(1), swValue);
        iExample.setValue((Attribute) fvWekaAttributes.elementAt(2), plValue);
        iExample.setValue((Attribute) fvWekaAttributes.elementAt(3), pwValue);
        iExample.setValue((Attribute) fvWekaAttributes.elementAt(4), "Iris-setosa"); // dummy

        testingSet.add(iExample);
        try {
            svmCls.buildClassifier(testingSet);
        } catch (java.lang.Exception e) {

        }
    }
    public void classify() {
        Attribute Attribute1 = new Attribute("sepallength");
        Attribute Attribute2 = new Attribute("sepalwidth");
        Attribute Attribute3 = new Attribute("petallength");
        Attribute Attribute4 = new Attribute("petalwidth");
        // Declare the class attribute along with its values (nominal)
        FastVector fvClassVal = new FastVector(3);
        fvClassVal.addElement("Iris-setosa");
        fvClassVal.addElement("Iris-versicolor");
        fvClassVal.addElement("Iris-virginica");
        Attribute ClassAttribute = new Attribute("class", fvClassVal);
        // Declare the feature vector template
        FastVector fvWekaAttributes = new FastVector(5);
        fvWekaAttributes.addElement(Attribute1);
        fvWekaAttributes.addElement(Attribute2);
        fvWekaAttributes.addElement(Attribute3);
        fvWekaAttributes.addElement(Attribute4);
        fvWekaAttributes.addElement(ClassAttribute);
        String slValue = ((EditText)findViewById(R.id.SepalLength)).toString();
        String swValue = ((EditText)findViewById(R.id.SepalWidth)).toString();
        String plValue = ((EditText)findViewById(R.id.PetalLength)).toString();
        String pwValue = ((EditText)findViewById(R.id.PetalWidth)).toString();
        // Creating testing instances object with name "TestingInstance"// using the feature vector templatewe declared above//and with initial capacity of 1
        Instances testingSet = new Instances("TestingInstance", fvWekaAttributes, 1);
        // Setting the column containing class labels:
        testingSet.setClassIndex(testingSet.numAttributes() - 1);
        // Create and fill an instance, and add it to the testingSetInstance i
        Instance iExample = new Instance(testingSet.numAttributes());
        iExample.setValue((Attribute) fvWekaAttributes.elementAt(0), slValue);
        iExample.setValue((Attribute) fvWekaAttributes.elementAt(1), swValue);
        iExample.setValue((Attribute) fvWekaAttributes.elementAt(2), plValue);
        iExample.setValue((Attribute) fvWekaAttributes.elementAt(3), pwValue);
        iExample.setValue((Attribute) fvWekaAttributes.elementAt(4), "Iris-setosa"); // dummy
        testingSet.add(iExample);

        svmCls.ge
    }
    public void serialize() {
        try {
            weka.core.SerializationHelper.write(svmModel, svmCls);
        } catch (java.lang.Exception e) {

        }
    }
    @Override
    public void onResume() {
        super.onResume();
        mAdkManager.open();

        mAdkReadTask = new AdkReadTask();
        mAdkReadTask.execute();
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdkManager.close();

        mAdkReadTask.pause();
        mAdkReadTask = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mAdkManager.getUsbReceiver());
    }

    // ToggleButton method - send message to SAM3X
    public void blinkLED(View v){
        if (buttonLED.isChecked()) {
            // writeSerial() allows you to write a single char or a String object.
            mAdkManager.writeSerial("1");
        } else {
            mAdkManager.writeSerial("0");
        }
    }

    /*
     * We put the readSerial() method in an AsyncTask to run the
     * continuous read task out of the UI main thread
     */
    private class AdkReadTask extends AsyncTask<Void, String, Void> {

        private boolean running = true;

        public void pause(){
            running = false;
        }

        protected Void doInBackground(Void... params) {
//	    	Log.i("ADK demo bi", "start adkreadtask");
            while(running) {
                publishProgress(mAdkManager.readSerial()) ;
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {

            float pulseRate= (int)progress[0].charAt(0);
            float oxygenLvl= (int)progress[0].charAt(1);
            float pos= (int)progress[0].charAt(2);
            int max = 255;
            if (pulseRate>max) pulseRate=max;
            if (oxygenLvl>max) oxygenLvl=max;
            if (pos>max) pos=max;

//            DecimalFormat df = new DecimalFormat("#.#");
            distance.setText(pulseRate + " (bpm)");
            pulse.setText(oxygenLvl + " (pct)");
            position.setText(pos + "");
        }
    }



}
