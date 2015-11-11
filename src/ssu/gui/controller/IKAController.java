package ssu.gui.controller;

import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NCri on 2015. 11. 11..
 */
public class IKAController implements IKADataRequestInterface{
    private Stage _primaryStage;

    private static int PATIENT_LIST = 0;
    private static int PATIENT_DEFAULT_INFOMATION = 1;
    private static int PATIENT_DETAIL_INFOMATION = 2;
    private static int PATIENT_DIAGNOSIS = 4;

    public static enum DataType {
        PATIENT_LIST,
        PATIENT_DEFAULT_INFOMATION,
        PATIENT_DETAIL_INFOMATION,
        PATIENT_DIAGNOSIS
    }

    public IKAController(Stage primaryStage){
        _primaryStage = primaryStage;
    }

    public List getInitData(IKAController.DataType dataType){
        List dataList = new ArrayList();

        return dataList;
    }
}
