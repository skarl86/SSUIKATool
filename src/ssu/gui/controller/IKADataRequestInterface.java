package ssu.gui.controller;

import java.util.List;


/**
 * Created by NCri on 2015. 11. 11..
 */
public interface IKADataRequestInterface {
    /**
     * 초기 데이터 요청시 필요한 인터페이스
     * @param dataType
     * @return 데이터 리스트
     */
    public List getInitData(IKAController.DataType dataType);
}
