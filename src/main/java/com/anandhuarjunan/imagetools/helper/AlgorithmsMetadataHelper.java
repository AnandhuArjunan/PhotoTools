package com.anandhuarjunan.imagetools.helper;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.csv.CSVRecord;

import com.anandhuarjunan.imagetools.model.AlgorithmTreeDataModel;
import com.anandhuarjunan.imagetools.model.Groups;
import com.anandhuarjunan.imagetools.utils.Utils;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class AlgorithmsMetadataHelper {
	
	public void initializeOperationsView(TreeView<Object> treeView) throws IOException{
		treeView.getChildrenUnmodifiable().clear();
        TreeItem<Object> rootItem = new TreeItem<>("Operations");
		List<CSVRecord> leafs = StreamSupport.stream(Utils.readAlgorithmsDataCsv().spliterator(), false).collect(Collectors.toList());

		Iterable<CSVRecord> algorithmsGroups = Utils.readAlgorithmsGroupDataCsv();
		
		for(CSVRecord ag : algorithmsGroups) {
			Groups groups = new Groups(ag.get("GROUP_ID"), ag.get("GROUP_NAME"), ag.get("GROUP_PARENT_ID"));
			TreeItem<Object> treeItem = new TreeItem<>();
			treeItem.setValue(groups);
			if(0 == Integer.parseInt(ag.get("GROUP_PARENT_ID"))) {
				rootItem.getChildren().add(treeItem);
			}else {
				TreeItem<Object>  treeItem2 = getTreeViewItemByParent(rootItem,groups);
				if(null != treeItem2 ) {
					treeItem2.getChildren().add(treeItem);
				}else {
					rootItem.getChildren().add(treeItem);
				}
			}
			
			List<CSVRecord> fleafs = leafs.stream().filter(e->e.get("ALGO_GROUP_ID").equalsIgnoreCase(groups.getGroupId())).collect(Collectors.toList());
			fleafs.forEach(leaf->{
				TreeItem<Object> leafItem = new TreeItem<>();
				leafItem.setValue(new AlgorithmTreeDataModel(leaf.get("ALGO_NAME"), leaf.get("ALGO_ID"), leaf.get("ALGO_GROUP_ID"), leaf.get("ALGO_CODE_PATH"), leaf.get("ALGO_CODE_COMPLEX")));
				treeItem.getChildren().add(leafItem);
			});
			
		}
		treeView.setRoot(rootItem);
		treeView.setShowRoot(false);
	}
	
	
	public static TreeItem<Object> getTreeViewItemByParent(TreeItem<Object> item, Groups value) {
	    if (item != null) {
	        if (item.getValue() instanceof Groups && ((Groups)item.getValue()).getGroupId().equals(value.getGroupParentId())) return item;
	        for (TreeItem<Object> child : item.getChildren()) {
	            TreeItem<Object> s = getTreeViewItemByParent(child, value);
	            if (s != null) {
	                return s;
	            }
	        }
	    }
	    return null;
	}

}
