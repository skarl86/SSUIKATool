package ssu.gui.controller;

/**
 * Created by NCri on 2015. 11. 11..
 */
public class IKAController{
    /**
     * 싱글톤 및 멀티 쓰레드 대비.
     */
    protected volatile static IKAController uniqueInstance;

    // 인스턴스가 있는지 확인하고 없으면 동기화된 블럭으로 진입.
    public static IKAController getInstance() {
        if (uniqueInstance == null) {                  // 처음에만 동기화.
            synchronized (IKAController.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new IKAController();  // 다시 한번 변수가 null인지 체크 후 인스턴스 생성.
                }
            }
        }
        return uniqueInstance;
    }
}
