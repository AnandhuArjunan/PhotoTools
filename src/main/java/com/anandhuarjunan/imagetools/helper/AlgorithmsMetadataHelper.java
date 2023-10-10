package com.anandhuarjunan.imagetools.helper;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Predicate;

import org.apache.commons.csv.CSVRecord;

import com.anandhuarjunan.imagetools.model.AlgorithmTreeDataModel;
import com.anandhuarjunan.imagetools.utils.Utils;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class AlgorithmsMetadataHelper {

	public void initializeOperationsView(TreeView<AlgorithmTreeDataModel> treeView) throws IOException {
		treeView.getChildrenUnmodifiable().clear();
		Iterable<CSVRecord> iterable = Utils.readAlgorithmsDataCsv();
        TreeItem rootItem = new TreeItem("Operations");

		for (CSVRecord record : iterable) {
		    String algoId = record.get("ALGO_ID");
		    String algoName = record.get("ALGO_NAME");
		    String algoGroupId = record.get("ALGO_GROUP_ID");
		    String algoGroupName = record.get("ALGO_GROUP_NAME");
		    String algoCodePath = record.get("ALGO_CODE_PATH");
		    String algoCodeComplex = record.get("ALGO_CODE_COMPLEX");
		    
		    Predicate<TreeItem<AlgorithmTreeDataModel>> predicate = e->{
		    	return null != e && null!=e.getValue() && null != e.getValue().getAlgorithmGroupId() && e.getValue().getAlgorithmGroupId().equalsIgnoreCase(algoGroupId);
		    };
		    Optional<TreeItem<AlgorithmTreeDataModel>> optional = null;
		    TreeItem<AlgorithmTreeDataModel> newItem = null;
		    optional = rootItem.getChildren().stream().filter(predicate).findFirst();
		    if(optional.isPresent()) {
		    	newItem = optional.get();
		    }else {
		    	newItem = new TreeItem<AlgorithmTreeDataModel>();
		    	AlgorithmTreeDataModel algorithmTreeDataModel = new AlgorithmTreeDataModel();
		    	algorithmTreeDataModel.setAlgorithmName(algoGroupName);
		    	algorithmTreeDataModel.setAlgorithmGroupId(algoGroupId);
		    	newItem.setValue(algorithmTreeDataModel);
		    	rootItem.getChildren().add(newItem);
		    }
		    		    
	    	TreeItem<AlgorithmTreeDataModel> treeItemAlgo = new TreeItem<AlgorithmTreeDataModel>();
	    	AlgorithmTreeDataModel algorithmTreeDataModel = new AlgorithmTreeDataModel();
	    	algorithmTreeDataModel.setAlgorithmName(algoName);
	    	algorithmTreeDataModel.setAlgorithmCodeComplex(algoCodeComplex);
	    	algorithmTreeDataModel.setAlgorithmCodePath(algoCodePath);
	    	treeItemAlgo.setValue(algorithmTreeDataModel);
	    	newItem.getChildren().add(treeItemAlgo);
		   
		}
		treeView.setRoot(rootItem);
		treeView.setShowRoot(false);
		
	}
	
	
}
