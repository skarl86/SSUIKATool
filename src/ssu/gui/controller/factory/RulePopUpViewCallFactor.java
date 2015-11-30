package ssu.gui.controller.factory;

import javafx.scene.control.TableView;
import ssu.gui.controller.IKARulePopUpViewController;

/**
 * Created by NCri on 2015. 11. 30..
 */
public abstract class RulePopUpViewCallFactor {
    protected IKARulePopUpViewController context;
    protected TableView tableView;

    public RulePopUpViewCallFactor(IKARulePopUpViewController context){
        this(context, null);
    }
    public RulePopUpViewCallFactor(IKARulePopUpViewController context, TableView tableView){
        this.context = context;
        this.tableView = tableView;
    }
}
