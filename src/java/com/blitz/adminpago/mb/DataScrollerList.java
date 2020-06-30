/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blitz.adminpago.mb;

import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.myfaces.custom.datascroller.ScrollerActionEvent;


public class DataScrollerList
{

    private Long rowCount = new Long(10);

    private List carList = new ArrayList();


    public Long getRowCount() {
        return rowCount;
    }

    public void setRowCount(Long rowCount) {
        this.rowCount = rowCount;
    }


    public DataScrollerList(List pobDatos){
        /*
        for (int i = 1; i < 995; i++)
        {
            carList.add(new SimpleCar(i, "Car Type " + i, "blue"));
        }
         *
         */
        carList = pobDatos;


    }

    public List getList()
    {
        return carList;
    }

    public void scrollerAction(ActionEvent event)
    {
        ScrollerActionEvent scrollerEvent = (ScrollerActionEvent) event;
        FacesContext.getCurrentInstance().getExternalContext().log("scrollerAction: facet: "        +
                scrollerEvent.getScrollerfacet()        + ", pageindex: "         + scrollerEvent.getPageIndex());
    }

}

